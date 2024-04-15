package org.gdglille.devfest.backend.internals.helpers.drive

import com.google.api.services.drive.Drive

interface DriveDataSource {
    fun findDriveByName(name: String): String?
    fun findFolderByName(name: String, parentId: String? = null, driveId: String? = null): String?
    fun findFileByName(name: String, parentId: String? = null, driveId: String? = null): String?
    fun createFolder(name: String, parentId: String? = null, driveId: String? = null): String
    fun copyFile(name: String, fileId: String, driveId: String? = null): String
    fun moveFile(fileId: String, folderId: String): List<String>
    fun grantPermission(fileId: String, email: String): String?

    object Factory {
        fun create(service: Drive): DriveDataSource = GoogleDriveDataSource(service)
    }
}
