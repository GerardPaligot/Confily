//
//  EmailInputView.swift
//  iosApp
//
//  Created by GERARD on 18/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct ProfileInputView: View {
    @State var email: String = ""
    @State var firstName: String = ""
    @State var lastName: String = ""
    @State var company: String = ""
    var qrCode: UIImage?
    var color: Color = Color.c4hOnBackground
    var onValidation: (String, String, String, String) -> ()
    
    var body: some View {
        VStack {
            Form {
                Section {
                    TextField("Your email address*", text: $email)
                        .keyboardType(.emailAddress)
                    TextField("Your first name*", text: $firstName)
                    TextField("Your last name*", text: $lastName)
                    TextField("Your company", text: $company)
                }
                if let uiImage = qrCode {
                    Section {
                        Image(uiImage: uiImage)
                            .resizable()
                            .frame(width: 250, height: 250, alignment: .center)
                    }
                }
                Section {
                    Button("Create QRCode") {
                        onValidation(email, firstName, lastName, company)
                    }
                }
            }
        }
    }
    
    var disabledForm: Bool {
        return email != "" && firstName != "" && lastName != ""
    }
}

struct EmailInputView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileInputView(
            email: NetworkingUi.companion.fake.userProfileUi.email,
            firstName: NetworkingUi.companion.fake.userProfileUi.firstName,
            lastName: NetworkingUi.companion.fake.userProfileUi.lastName,
            company: NetworkingUi.companion.fake.userProfileUi.company,
            qrCode: nil,
            onValidation: { _, _, _, _ in }
        )
    }
}
