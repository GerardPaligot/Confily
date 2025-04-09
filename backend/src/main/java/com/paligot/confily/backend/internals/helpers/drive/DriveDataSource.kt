package com.paligot.confily.backend.internals.helpers.drive

import com.google.api.services.drive.Drive

interface DriveDataSource {
    fun findDriveByName(name: String): String?
    fun findFolderById(id: String, name: String, parentId: String? = null, driveId: String? = null): String?
    fun findFolderByName(name: String, parentId: String? = null, driveId: String? = null): String?
    fun findFileByName(name: String, parentId: String? = null, driveId: String? = null): String?
    fun createFolder(name: String, parentId: String? = null, driveId: String? = null): String
    fun copyFile(name: String, fileId: String, driveId: String? = null): String
    fun moveFile(fileId: String, folderId: String): List<String>
    fun grantPermission(fileId: String, email: String): String?
    fun hasPermission(fileId: String, email: String): Boolean

    object Factory {
        fun create(service: Drive): DriveDataSource = GoogleDriveDataSource(service)
    }
}
