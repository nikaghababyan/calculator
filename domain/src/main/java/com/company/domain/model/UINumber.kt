package com.company.domain.model

data class UINumber(
    override val id: Int,
    val title: Int,
    val background: Int
) : DiffUtilModel<Int>()