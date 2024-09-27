package com.paligot.confily.core.fs

import okio.FileSystem

actual val fileSystem: FileSystem
    get() = throw IllegalStateException("There is no SYSTEM filesystem on JS")
