package com.company.calculator.extensions

import android.content.Context
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment

fun Fragment.toPx(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)
fun View.toPx(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)
fun Context.toPx(@DimenRes id: Int): Int = resources.getDimensionPixelSize(id)
