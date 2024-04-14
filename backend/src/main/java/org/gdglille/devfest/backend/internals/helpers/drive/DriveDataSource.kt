package org.gdglille.devfest.backend.internals.helpers.drive

import com.google.api.services.drive.Drive

interface DriveDataSource {
    fun findDriveByName(name: String): String?
    fun findFolderByName(driveId: String, parentId: String?, name: String): String?
    fun findFileByName(driveId: String, parentId: String?, name: String): String?
    fun createFolder(driveId: String, parentId: String?, name: String): String
    fun copyFile(driveId: String, fileId: String, name: String): String
    fun moveFile(driveId: String, fileId: String, folderId: String): List<String>
    fun grantPermission(fileId: String, email: String)

    object Factory {
        fun create(service: Drive): DriveDataSource = GoogleDriveDataSource(service)
    }
}
