//
//  QrCodeGenerator.swift
//  iosApp
//
//  Created by GERARD on 23/02/2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

class QrCodeGeneratoriOS: QrCodeGenerator {
    func generate(text: String) -> UIImage {
        let data = text.data(using: String.Encoding.utf8)
        let qrFilter = CIFilter(name: "CIQRCodeGenerator")!
        qrFilter.setValue(data, forKey: "inputMessage")
        let qrImage = qrFilter.outputImage!

        // Scale the image
        let transform = CGAffineTransform(scaleX: 10, y: 10)
        let scaledQrImage = qrImage.transformed(by: transform)

        // Do some processing to get the UIImage
        let context = CIContext()
        let cgImage = context.createCGImage(scaledQrImage, from: scaledQrImage.extent)!
        return UIImage(cgImage: cgImage)
    }
}
