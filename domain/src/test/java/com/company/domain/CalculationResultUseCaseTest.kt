package com.company.domain

import com.company.domain.util.TestDispatchersProvider
import com.company.domain.util.assertEquals
import com.company.domain.dispatcher.CoroutineDispatcherProvider
import com.company.domain.usecases.CalculationResultUseCase
import com.company.domain.usecases.CalculationResultUseCaseImpl
import com.company.domain.util.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CalculationResultUseCaseTest {
    private val input = "6+6*2"

    private val dispatchersProvider: CoroutineDispatcherProvider = TestDispatchersProvider()

    private val useCase: CalculationResultUseCase = CalculationResultUseCaseImpl(
        dispatcher = dispatchersProvider
    )

    @Test
    fun `when execute is pass`() {
        useCase(
            input
        ).assertEquals("18", dispatchersProvider.io)
    }

    @Test
    fun `when execute is failure`() {
        useCase(
            input
        ).assertNotEquals("24", dispatchersProvider.io)
    }
}
