package com.company.calculator.ui.fragments.home

import android.text.SpannableStringBuilder
import androidx.lifecycle.viewModelScope
import com.company.calculator.R
import com.company.calculator.appbase.utils.subscribe
import com.company.calculator.appbase.viewmodel.BaseViewModel
import com.company.domain.model.UINumber
import com.company.domain.usecases.CalculationResultUseCase
import com.company.domain.utils.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class CalculatorViewModel(
    private val calculationResultUseCase: CalculationResultUseCase,
) : BaseViewModel() {


    private val _numberList: MutableStateFlow<List<UINumber>?> = MutableStateFlow(null)
    val numberList: StateFlow<List<UINumber>?> = _numberList

    private val _result: MutableStateFlow<String?> = MutableStateFlow(null)
    val result: StateFlow<String?> = _result

    private val _resultError: MutableSharedFlow<String> = MutableSharedFlow()
    val resultError: SharedFlow<String> = _resultError
    private val _input: MutableStateFlow<Pair<String, Int>> by lazy {
        MutableStateFlow(Pair(EMPTY_STRING, EMPTY_STRING.length))
    }

    val input: StateFlow<Pair<String, Int>> = _input

    init {
        initNumber()
    }

    private fun initNumber() {
        _numberList.value = mutableListOf<UINumber>().apply {
            add(UINumber(1, R.string.seven, R.color.black))
            add(UINumber(2, R.string.eight, R.color.black))
            add(UINumber(3, R.string.nine, R.color.black))
            add(UINumber(4, R.string.divide, R.color.secondary_button_color))
            add(UINumber(5, R.string.four, R.color.black))
            add(UINumber(6, R.string.five, R.color.black))
            add(UINumber(7, R.string.six, R.color.black))
            add(UINumber(8, R.string.multiply, R.color.secondary_button_color))
            add(UINumber(9, R.string.one, R.color.black))
            add(UINumber(10, R.string.two, R.color.black))
            add(UINumber(11, R.string.three, R.color.black))
            add(UINumber(12, R.string.plus, R.color.secondary_button_color))
            add(UINumber(13, R.string.dot, R.color.black))
            add(UINumber(14, R.string.zero, R.color.black))
            add(UINumber(15, R.string.equal, R.color.first_button_color))
            add(UINumber(16, R.string.minus, R.color.secondary_button_color))
        }
    }

    fun inputText(strToAdd: String, selectionStart: Int) {
        val (oldValue, _) = _input.value
        var expression = ""
        if (strToAdd == getString(R.string.equal)) {
            val result = _result.value.orEmpty()
            _input.update { Pair(result, result.length) }
            _result.update { EMPTY_STRING }
            return
        }
        if (strToAdd == getString(R.string.ac)) {
            _input.update { Pair(EMPTY_STRING, EMPTY_STRING.length) }
            _result.update { EMPTY_STRING }
            return
        }

        if (strToAdd == getString(R.string.c)) {
            val textLen = oldValue.length

            if (selectionStart != 0 && textLen != 0) {
                val selection = SpannableStringBuilder(oldValue)
                SpannableStringBuilder(selection)
                selection.replace(selectionStart - 1, selectionStart, "")
                expression = selection.toString()
                _input.update { Pair(selection.toString(), selectionStart - 1) }
            }
        } else {
            if(oldValue.isEmpty() && !strToAdd.last().isDigit()){
                viewModelScope.launch {
                    _resultError.emit(getString(R.string.invalid_format_error))
                }
                return
            }else if (!strToAdd.last().isDigit() && !oldValue.last().isDigit()) {
                return
            }
            val leftStr = oldValue.substring(0, selectionStart)
            val rightStr = oldValue.substring(selectionStart)
            if (EMPTY_STRING == oldValue) {
                _input.update { Pair(strToAdd, strToAdd.length) }
            } else {

                expression = String.format("%s%s%s", leftStr, strToAdd, rightStr)
                _input.update {
                    Pair(expression, selectionStart + 1)
                }
            }

        }

        calculationResultUseCase(expression).subscribe(viewModelScope,
            success = {
                _result.value = it
            }, error = {
                _result.value = EMPTY_STRING
                if(it is ArithmeticException) {
                    _resultError.emit(it.message.orEmpty())
                }
            })
    }
}
