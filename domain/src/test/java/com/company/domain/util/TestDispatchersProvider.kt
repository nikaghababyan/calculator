package com.company.domain.util

import com.company.domain.dispatcher.CoroutineDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatchersProvider : CoroutineDispatcherProvider {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testCoroutineDispatcher by lazy { StandardTestDispatcher() }

    override val main  = testCoroutineDispatcher

    override val io = testCoroutineDispatcher

}
