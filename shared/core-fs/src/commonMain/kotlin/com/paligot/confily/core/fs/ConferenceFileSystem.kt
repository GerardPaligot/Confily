package com.paligot.confily.core.fs

import kotlinx.datetime.Clock
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

data class User(
    val email: String,
    val firstname: String,
    val lastname: String,
    val company: String
)

expect val fileSystem: FileSystem

class ConferenceFileSystem private constructor(
    private val fileSystem: FileSystem,
    private val tempFolderPath: Path
) {
    fun exportUsers(users: List<User>): String {
        val path = "$tempFolderPath/${Clock.System.now().epochSeconds}.csv"
        fileSystem.write(path.toPath()) {
            users.forEach { user ->
                writeUtf8("${user.email};${user.firstname};${user.lastname};${user.company}")
            }
        }
        return path
    }

    companion object {
        fun create(
            tempFolderPath: Path
        ): ConferenceFileSystem = ConferenceFileSystem(
            fileSystem = fileSystem,
            tempFolderPath = tempFolderPath
        )
    }
}
