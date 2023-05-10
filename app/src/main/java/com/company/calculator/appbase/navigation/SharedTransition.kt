package com.company.calculator.appbase.navigation

import android.os.Bundle
import android.transition.Transition
import android.view.View

class SharedTransition private constructor(builder: Builder) {

    var arguments = Bundle()
    var enterSharedAnimation: Transition? = null
    var returnSharedAnimation: Transition? = null
    var navigateFragment: androidx.fragment.app.Fragment? = null
    var views = mutableListOf<View>()

    init {
        enterSharedAnimation = builder.enterSharedAnimation
        returnSharedAnimation = builder.returnSharedAnimation
        navigateFragment = builder.navigateFragment
        arguments = builder.arguments
        views = builder.views
    }

    class Builder {

        var enterSharedAnimation: Transition? = null
        var returnSharedAnimation: Transition? = null
        var navigateFragment: androidx.fragment.app.Fragment? = null
        var arguments = Bundle()
        val views = mutableListOf<View>()

        fun enterSharedAnimation(transition: Transition) = apply { this.enterSharedAnimation = transition }

        fun returnSharedAnimation(transition: Transition) = apply { this.returnSharedAnimation = transition }

        fun navigateFragment(fragment: androidx.fragment.app.Fragment) = apply { this.navigateFragment = fragment }

        fun addViews(views: List<View>) = apply {
            views.forEach { this.views.add(it) }
            this.arguments.putStringArray("transition_id", views.map { it.transitionName }.toTypedArray())
        }

        fun build() = SharedTransition(this)
    }
}