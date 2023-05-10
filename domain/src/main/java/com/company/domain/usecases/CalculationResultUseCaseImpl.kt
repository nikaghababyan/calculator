package com.company.domain.usecases

import com.company.domain.dispatcher.CoroutineDispatcherProvider
import com.company.domain.utils.DEFAULT_INT
import com.company.domain.utils.emitFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.annotation.Factory

interface CalculationResultUseCase {
    operator fun invoke(input: String): Flow<String>
}

@Factory
internal class CalculationResultUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider
) : CalculationResultUseCase {

    override operator fun invoke(input: String): Flow<String> =
        emitFlow {
            if(input.last().isDigit()) {
                calculate(input)
            }else{
                DEFAULT_INT.toString()
            }
        }.flowOn(dispatcher.io)

    private fun calculate(input: String): String {
        val digitsAndOperators = calcDigitsAndOperators(input)
        if (digitsAndOperators.isEmpty()) return ""

        val calcDivision = calcDivision(digitsAndOperators)
        if (calcDivision.isEmpty()) return ""

        val result = calcAdditionSubtract(calcDivision)
        return if (result.toString().endsWith(".0")) {
            result.toDouble().toInt().toString()
        } else {
            result.toString()
        }
    }

    private fun calcAdditionSubtract(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun calcDivision(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('*') || list.contains('÷')) {
            list = calcDivAndMultiply(list)
        }
        return list
    }

    private fun calcDivAndMultiply(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    '*' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '÷' -> {
                        if(nextDigit==0F){
                            throw ArithmeticException("Can't divide by zero")
                        }
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun calcDigitsAndOperators(input: String): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (char in input) {
            if (char.isDigit() || char == '.')
                currentDigit += char
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(char)
            }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

}
