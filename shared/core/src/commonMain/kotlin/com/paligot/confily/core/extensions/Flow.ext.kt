package com.paligot.confily.core.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

fun <T> Flow<List<T>>.fallbackIfEmpty(fallback: () -> Flow<List<T>>): Flow<List<T>> = flow {
    collect { list ->
        if (list.isEmpty()) {
            emitAll(fallback())
        } else {
            emit(list)
        }
    }
}
