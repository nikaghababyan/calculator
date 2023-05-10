package com.company.calculator.appbase.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.calculator.App
import com.company.calculator.appbase.navigation.NavEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    protected val navEvents = MutableSharedFlow<NavEvent>()
    val getNavEvents: Flow<NavEvent> = navEvents

    fun getString(@StringRes strId: Int, vararg fmtArgs: Any?) =
        App.appContext.getString(strId, *fmtArgs)

    fun setNavigate(destination: NavEvent){
        viewModelScope.launch {
            navEvents.emit(destination)
        }
    }
}