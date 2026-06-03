//
//  QuizScreens.swift
//  iosApp
//
//  Copyright © 2026 orgName. All rights reserved.
//

import SwiftUI
import SharedDi

struct QuizVM: View {
    @ObservedObject var viewModel: QuizViewModel

    init(viewModel: QuizViewModel) {
        self.viewModel = viewModel
    }

    var body: some View {
        NavigationView {
            Group {
                switch viewModel.step {
                case let .code(username, score):
                    QuizCodeView(
                        username: username,
                        score: score,
                        onStart: { code, name in
                            viewModel.startQuiz(code: code, name: name)
                        }
                    )
                case .loadingQuestions, .submitting:
                    Text("textLoading")
                case let .questions(questions, selections):
                    QuizQuestionsView(
                        questions: questions,
                        selections: selections,
                        onSelect: { questionId, answerId in
                            viewModel.select(questionId: questionId, answerId: answerId)
                        },
                        onSubmit: {
                            viewModel.submit()
                        }
                    )
                case let .result(result, labels):
                    QuizResultView(
                        result: result,
                        labels: labels,
                        onDone: {
                            viewModel.reset()
                        }
                    )
                case .alreadyCompleted:
                    VStack(spacing: 16) {
                        Text("quizAlreadyCompleted")
                        Button("quizActionBackToCode") {
                            viewModel.reset()
                        }
                    }
                    .padding()
                case let .error(message):
                    VStack(spacing: 16) {
                        Text(message)
                        Button("quizActionBackToCode") {
                            viewModel.reset()
                        }
                    }
                    .padding()
                }
            }
            .navigationTitle("screenQuiz")
        }
        .onAppear {
            viewModel.start()
        }
        .onDisappear {
            viewModel.stop()
        }
    }
}

struct QuizCodeView: View {
    let username: String?
    let score: Int?
    let onStart: (String, String?) -> Void

    @State private var code: String = ""
    @State private var name: String = ""

    private var needsName: Bool {
        username == nil
    }

    private var canStart: Bool {
        !code.isEmpty && (!needsName || !name.isEmpty)
    }

    var body: some View {
        Form {
            if let score = score {
                Section {
                    TagView(
                        text: String(format: NSLocalizedString("quizScoreLabel", comment: ""), score),
                        icon: "star.fill"
                    )
                }
            }
            Section {
                Text("quizEnterCodeTitle")
                    .foregroundColor(.secondary)
                    .font(.caption)
                TextField("quizCodeLabel", text: $code)
                    .autocapitalization(.none)
                if needsName {
                    TextField("quizNameLabel", text: $name)
                }
            }
            Section {
                Button("quizActionStart") {
                    UIApplication.shared.endEditing()
                    onStart(code, username ?? name)
                }
                .disabled(!canStart)
            }
        }
    }
}

struct QuizQuestionsView: View {
    let questions: [QuizQuestion]
    let selections: [String: String]
    let onSelect: (String, String) -> Void
    let onSubmit: () -> Void

    private var allAnswered: Bool {
        questions.allSatisfy { selections[$0.id] != nil }
    }

    var body: some View {
        Form {
            ForEach(questions, id: \.id) { question in
                Section(header: Text(question.question)) {
                    ForEach(question.options, id: \.id) { option in
                        let isSelected = selections[question.id] == option.id
                        Button {
                            onSelect(question.id, option.id)
                        } label: {
                            HStack {
                                Image(systemName: isSelected ? "largecircle.fill.circle" : "circle")
                                    .foregroundColor(isSelected ? Color.c4hPrimary : .secondary)
                                Text(option.label)
                                    .foregroundColor(Color.c4hOnBackground)
                                Spacer()
                            }
                        }
                    }
                }
            }
            Section {
                Button("quizActionSubmit") {
                    onSubmit()
                }
                .disabled(!allAnswered)
            }
        }
    }
}

struct QuizResultView: View {
    let result: QuizSubmissionResult
    let labels: [String: String]
    let onDone: () -> Void

    private var scoreText: String {
        String(
            format: NSLocalizedString("quizResultScore", comment: ""),
            Int(result.correctCount),
            Int(result.totalCount)
        )
    }

    var body: some View {
        Form {
            Section {
                HStack {
                    Spacer()
                    TagView(
                        text: scoreText,
                        icon: "checkmark.seal.fill"
                    )
                    Spacer()
                }
            }
            Section {
                ForEach(result.perQuestion, id: \.questionId) { answered in
                    HStack(alignment: .top) {
                        Image(systemName: answered.isCorrect ? "checkmark.circle.fill" : "xmark.circle.fill")
                            .foregroundColor(answered.isCorrect ? Color.c4hPrimary : .red)
                        Text(labels[answered.questionId] ?? "")
                            .foregroundColor(Color.c4hOnBackground)
                    }
                }
            }
            Section {
                Button("quizActionBackToCode", action: onDone)
            }
        }
    }
}
