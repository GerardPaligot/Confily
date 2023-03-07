//
//  ComposeMailData.swift
//  iosApp
//
//  Created by GERARD on 18/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

struct ComposeMailData {
    let subject: String
    let recipients: [String]?
    let message: String
    let attachments: [AttachmentData]?

    static let empty = ComposeMailData(subject: "", recipients: nil, message: "", attachments: nil)
}
