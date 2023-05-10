package com.company.calculator.appbase

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.company.calculator.appbase.navigation.NavEvent
import com.company.calculator.appbase.viewmodel.BaseViewModel

abstract class FragmentBaseNCMVVM<ViewModel : BaseViewModel, ViewBind : ViewBinding> :
    FragmentBaseMVVM<ViewModel, ViewBind>() {

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        collectNavigationEvents()
    }

    private fun collectNavigationEvents() {
        collectFlow(viewModel.getNavEvents) {
            when (it) {
                is NavEvent.Back -> {
                    navController.popBackStack()
                }
                is NavEvent.Destination -> {
                    navController.navigate(it.destination)
                }
            }
        }
    }
}
