package com.senijoshua.pods.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Flow intermediary operator extension function that converts Flow<T> to Flow<Result<T>>
 * for better error handling in the presentation layer.
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> = this.map {
    Result.success(it)
}.catch {
    emit(Result.failure(it))
}
