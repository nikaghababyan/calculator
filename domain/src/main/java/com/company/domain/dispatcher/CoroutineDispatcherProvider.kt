package com.company.domain.dispatcher

import kotlin.coroutines.CoroutineContext

interface CoroutineDispatcherProvider {
    val main: CoroutineContext
    val io: CoroutineContext
}
