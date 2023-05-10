package com.company.calculator.appbase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.company.calculator.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class FragmentBaseMVVM<ViewModel : BaseViewModel, ViewBind : ViewBinding> :
    Fragment() {

    abstract val viewModel: ViewModel
    abstract val binding: ViewBind

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlows()
        initView()
        initListeners()
    }

    protected abstract fun initView()

    protected abstract fun collectFlows()

    protected open fun initListeners() {}

    protected inline fun <reified T> collectFlow(flow: Flow<T?>, crossinline action: (T) -> Unit) {
        if (!this@FragmentBaseMVVM.isAdded) return
        viewLifecycleOwner.lifecycleScope.launch {
            flow.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                action.invoke(it ?: return@collect)
            }
        }
    }
}
