//
//  QuizViewModel.swift
//  iosApp
//
//  Copyright © 2026 orgName. All rights reserved.
//

import Foundation
import SharedDi
import KMPNativeCoroutinesAsync

enum QuizStep {
    case code(username: String?, score: Int?)
    case loadingQuestions
    case questions(questions: [QuizQuestion], selections: [String: String])
    case submitting
    case result(result: QuizSubmissionResult, labels: [String: String])
    case alreadyCompleted
    case error(String)
}

@MainActor
class QuizViewModel: ObservableObject {
    private let interactor: QuizInteractor = InteractorHelper().quizInteractor

    @Published var step: QuizStep = .code(username: nil, score: nil)

    private var currentCode: String = ""
    private var loadedQuestions: [QuizQuestion] = []
    private var tasks: [Task<(), Never>] = []

    func start() {
        // Observe stored username so the name prompt is skipped once registered.
        tasks.append(Task {
            do {
                let stream = asyncSequence(for: interactor.storedUsername())
                for try await name in stream {
                    if case let .code(_, score) = self.step {
                        self.step = .code(username: name as String?, score: score)
                    }
                }
            } catch {
                // ignore: username is optional
            }
        })
        // Load cumulative score (value-returning suspend -> asyncFunction).
        tasks.append(Task {
            do {
                let score = try await asyncFunction(for: interactor.cumulativeScore())
                if case let .code(username, _) = self.step {
                    self.step = .code(username: username, score: score.map { Int(truncating: $0) })
                }
            } catch {
                // ignore: score is optional display
            }
        })
    }

    func startQuiz(code: String, name: String?) {
        currentCode = code
        if let name = name, !name.isEmpty {
            tasks.append(Task { _ = await asyncError(for: interactor.register(username: name)) })
        }
        loadQuestions(code: code)
    }

    func loadQuestions(code: String) {
        currentCode = code
        step = .loadingQuestions
        tasks.append(Task {
            do {
                let questions = try await asyncFunction(for: interactor.questions(code: code))
                self.loadedQuestions = questions
                self.step = .questions(questions: questions, selections: [:])
            } catch {
                self.step = .error(NSLocalizedString("quizErrorCodeNotFound", comment: ""))
            }
        })
    }

    func select(questionId: String, answerId: String) {
        guard case let .questions(questions, selections) = step else { return }
        var updated = selections
        updated[questionId] = answerId
        step = .questions(questions: questions, selections: updated)
    }

    func submit() {
        guard case let .questions(_, selections) = step else { return }
        let answers = selections.map { QuizAnswerInput(questionId: $0.key, answerId: $0.value) }
        step = .submitting
        tasks.append(Task {
            do {
                let result = try await asyncFunction(
                    for: interactor.submit(code: currentCode, answers: answers)
                )
                self.step = .result(result: result, labels: self.buildLabels())
            } catch {
                // AlreadySubmitted -> show saved result if present, else alreadyCompleted.
                if let saved = self.interactor.savedResult(code: self.currentCode) {
                    self.step = .result(result: saved, labels: self.buildLabels())
                } else {
                    self.step = .alreadyCompleted
                }
            }
        })
    }

    private func buildLabels() -> [String: String] {
        var labels: [String: String] = [:]
        for question in loadedQuestions {
            labels[question.id] = question.question
        }
        return labels
    }

    func reset() {
        stop()
        step = .code(username: nil, score: nil)
        start()
    }

    func stop() {
        tasks.forEach { $0.cancel() }
        tasks = []
    }
}
