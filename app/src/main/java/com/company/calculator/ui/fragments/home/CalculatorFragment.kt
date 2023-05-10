package com.company.calculator.ui.fragments.home

import android.widget.Toast
import com.company.calculator.R
import com.company.calculator.appbase.FragmentBaseNCMVVM
import com.company.calculator.appbase.utils.viewBinding
import com.company.calculator.databinding.FragmentCalculatorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : FragmentBaseNCMVVM<CalculatorViewModel, FragmentCalculatorBinding>() {

    override val viewModel: CalculatorViewModel by viewModel()
    override val binding: FragmentCalculatorBinding by viewBinding()

    private var boardAdapter = CalculatorBoardAdapter {
        viewModel.inputText(it, binding.input.selectionStart)
    }

    override fun initView() {
        binding.rvBoard.adapter = boardAdapter
    }

    override fun initListeners() {
        with(binding) {
            delete.setOnClickListener {
                viewModel.inputText(getString(R.string.c), input.selectionStart)
            }
            delete.setOnLongClickListener {
                viewModel.inputText(getString(R.string.ac), input.selectionStart)
                return@setOnLongClickListener true
            }
        }
    }
    override fun collectFlows() {
        with(binding) {
            collectFlow(viewModel.numberList) {
                boardAdapter.submitList(it)
            }

            collectFlow(viewModel.result) {
                result.text = it
            }
            collectFlow(viewModel.resultError) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
            collectFlow(viewModel.input) {
                input.setText(it.first)
                input.setSelection(it.second)
            }
        }
    }

}
