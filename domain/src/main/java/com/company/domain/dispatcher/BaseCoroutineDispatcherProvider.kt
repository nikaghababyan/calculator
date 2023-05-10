package com.company.domain.dispatcher

import kotlinx.coroutines.Dispatchers
import org.koin.core.annotation.Single

@Single
class BaseCoroutineDispatcherProvider : CoroutineDispatcherProvider {
    override val main by lazy { Dispatchers.Main }
    override val io by lazy { Dispatchers.IO }
}
