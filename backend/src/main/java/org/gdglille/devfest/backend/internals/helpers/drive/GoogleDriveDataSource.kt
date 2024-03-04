package org.gdglille.devfest.backend.internals.helpers.drive

import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.Permission

private const val PageSize = 10

class GoogleDriveDataSource(private val service: Drive) : DriveDataSource {
    override fun findDriveByName(name: String): String? {
        val drives = service.drives().list().apply {
            pageSize = PageSize
            fields = "nextPageToken, drives(id, name)"
        }.execute()
        return drives.drives.find { it.name == name }?.id
    }

    override fun findFolderByName(driveId: String, parentId: String?, name: String): String? {
        return service.files().list().apply {
            this.driveId = driveId
            includeItemsFromAllDrives = true
            supportsAllDrives = true
            corpora = "drive"
            pageSize = PageSize
            fields = "nextPageToken, files(id)"
            this.q = "name='$name'${parentId?.let { " and parents='$parentId'" } ?: run { "" }}"
        }.execute().files.firstOrNull()?.id
    }

    override fun findFileByName(driveId: String, parentId: String?, name: String): String? {
        return service.files().list().apply {
            this.driveId = driveId
            includeItemsFromAllDrives = true
            supportsAllDrives = true
            corpora = "drive"
            pageSize = PageSize
            fields = "nextPageToken, files(id)"
            this.q = "name='$name'${parentId?.let { " and parents='$parentId'" } ?: run { "" }}"
        }.execute().files.firstOrNull()?.id
    }

    override fun copyFile(driveId: String, fileId: String, name: String): String {
        val file = File().apply {
            this.name = name
            this.mimeType = "application/vnd.google-apps.spreadsheet"
            parents = listOf(driveId)
        }
        return service.files().copy(fileId, file).apply {
            supportsAllDrives = true
            fields = "id"
        }.execute().id
    }

    override fun moveFile(driveId: String, fileId: String, folderId: String): List<String> {
        val file = service.files()[fileId].apply {
            supportsAllDrives = true
            fields = "parents"
        }.execute()
        return service.files().update(fileId, null).apply {
            supportsAllDrives = true
            addParents = folderId
            removeParents = file.parents.joinToString(",")
            fields = "parents"
        }.execute().parents
    }

    override fun grantPermission(fileId: String, email: String) {
        val userPermission: Permission = Permission()
            .setType("user")
            .setRole("writer")
        userPermission.setEmailAddress(email)
        service.permissions().create(fileId, userPermission).setFields("id").execute()
    }
}
