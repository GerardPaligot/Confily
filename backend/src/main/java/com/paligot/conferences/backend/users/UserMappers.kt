package com.paligot.conferences.backend.users

import com.paligot.conferences.backend.storage.Upload
import com.paligot.conferences.models.EmailQrCode

fun Upload.convertToModel() = EmailQrCode(
    url = this.url
)
