package kz.nkaiyrken.newsapp2025.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update

fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
    return merge(this, another)
}

fun <T> StateFlow<T>.tryToUpdate(function: (T) -> T) {
    if (this is MutableStateFlow<T>) {
        this.update(function)
    }
}