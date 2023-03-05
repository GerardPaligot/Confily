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
                    TextField("inputEmail", text: $email)
                        .keyboardType(.emailAddress)
                    TextField("inputFirstname", text: $firstName)
                    TextField("inputLastname", text: $lastName)
                    TextField("inputCompany", text: $company)
                }
                if let uiImage = qrCode {
                    Section {
                        HStack {
                            Spacer()
                            Image(uiImage: uiImage)
                                .resizable()
                                .frame(width: 250, height: 250, alignment: .center)
                                .accessibilityLabel("semanticProfileQrcode")
                            Spacer()
                        }
                    }
                }
                Section {
                    Button("actionGenerateQrcode") {
                        UIApplication.shared.endEditing()
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
            email: NetworkingUi.companion.fake.userProfileUi!.email,
            firstName: NetworkingUi.companion.fake.userProfileUi!.firstName,
            lastName: NetworkingUi.companion.fake.userProfileUi!.lastName,
            company: NetworkingUi.companion.fake.userProfileUi!.company,
            qrCode: nil,
            onValidation: { _, _, _, _ in }
        )
    }
}
