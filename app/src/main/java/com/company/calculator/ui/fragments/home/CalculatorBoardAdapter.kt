package com.company.calculator.ui.fragments.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import com.company.calculator.R
import com.company.calculator.appbase.adapter.BaseAdapter
import com.company.calculator.appbase.adapter.BaseViewHolder
import com.company.calculator.databinding.ItemCalculatorBinding
import com.company.domain.model.UINumber
import com.company.domain.utils.DEFAULT_INT

class CalculatorBoardAdapter(private val item: (String) -> Unit) :
    BaseAdapter<ViewBinding, UINumber, BaseViewHolder<UINumber, ViewBinding>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<UINumber, ViewBinding> {
        return ItemViewHolder(
            ItemCalculatorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private inner class ItemViewHolder(private val binding: ItemCalculatorBinding) :
        BaseViewHolder<UINumber, ViewBinding>(binding) {

        override fun bind(item: UINumber, context: Context) {
            with(binding) {
                if (item.title != DEFAULT_INT && item.background != DEFAULT_INT) {
                    itemText.text = context.getString(item.title)
                    itemText.background = ContextCompat.getDrawable(context, R.drawable.bg_circle)
                    itemText.background.setTint(ContextCompat.getColor(context, item.background))
                }else{
                    itemText.background = null
                }

            }
        }

        override fun onItemClick(item: UINumber, context: Context) {
            item(context.getString(item.title))
        }
    }
}
