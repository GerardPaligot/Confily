package com.paligot.confily.core.fs

import okio.FileSystem

actual val fileSystem: FileSystem
    get() = FileSystem.SYSTEM
