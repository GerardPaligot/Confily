package com.paligot.confily.core.networking.entities

import com.paligot.confily.networking.ui.models.ExportNetworkingUi
import kotlin.native.ObjCName

@ObjCName("ExportUsersEntity")
class ExportUsers(val mailto: String?, val filePath: String)

fun ExportUsers.mapToExportNetworkingUi(): ExportNetworkingUi = ExportNetworkingUi(mailto, filePath)
