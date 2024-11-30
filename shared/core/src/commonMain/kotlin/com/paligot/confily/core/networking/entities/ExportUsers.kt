package com.paligot.confily.core.networking.entities

import com.paligot.confily.models.ui.ExportNetworkingUi
import kotlin.native.ObjCName

@ObjCName("ExportUsersEntity")
class ExportUsers(val mailto: String?, val filePath: String)

fun ExportUsers.mapToUi(): ExportNetworkingUi = ExportNetworkingUi(mailto, filePath)
