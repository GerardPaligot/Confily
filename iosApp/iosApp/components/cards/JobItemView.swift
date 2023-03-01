//
//  JobItemView.swift
//  iosApp
//
//  Created by GERARD on 26/02/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

struct JobItemView: View {
    let jobUi: JobUi
    let onClick: (_: String) -> ()
    
    var body: some View {
        Button {
            onClick(jobUi.url)
        } label: {
            VStack(alignment: .leading) {
                Text(jobUi.title)
                    .foregroundColor(Color.c4hOnBackground)
                    .font(.headline)
                Text("\(jobUi.companyName) - \(jobUi.location)")
                    .foregroundColor(Color.c4hOnBackground)
                    .font(.subheadline)
                HStack {
                    if (jobUi.salary != nil) {
                        TagUnStyledView(
                            text: "\(jobUi.salary!.min)K - \(jobUi.salary!.max)K/\(jobUi.salary!.recurrence)",
                            icon: "creditcard"
                        )
                    }
                    let textJobRequirements: String = LocalizedStringKey("textJobRequirements").stringValue()
                    TagUnStyledView(
                        text: "\(jobUi.requirements) \(textJobRequirements)",
                        icon: "suitcase"
                    )
                }
                let textJobPropulsed: String = LocalizedStringKey("textJobPropulsed").stringValue()
                TagView(
                    text: "\(textJobPropulsed) \(jobUi.propulsed)",
                    containerColor: Color.decorativeGravel,
                    contentColor: Color.decorativeOnGravel
                )
            }
            .frame(maxWidth: .infinity, alignment: .topLeading)
            .padding()
            .background(Color.c4hSurface)
            .modifier(CardModifier(elevation: 2))
        }
    }
}

struct JobItemView_Previews: PreviewProvider {
    static var previews: some View {
        JobItemView(
            jobUi: JobUi.companion.fake,
            onClick: { url in }
        )
    }
}
