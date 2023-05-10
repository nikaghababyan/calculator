package com.company.calculator.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.company.calculator.appbase.utils.viewBinding
import com.company.calculator.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewBinding: ActivityMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}
