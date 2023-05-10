package com.company.calculator.appbase.navigation

import androidx.navigation.NavDirections

sealed class NavEvent {

    object Back : NavEvent()

    data class Destination(val destination: NavDirections) : NavEvent()

    /*
   * TODO Will be initialized later
   *   data class DeepLink(val link: Uri) : NavEvent()
   */

}

fun NavDirections.toDestination(): NavEvent.Destination {
    return NavEvent.Destination(this)
}

//fun String.toDeepLink(): NavEvent.DeepLink {
//    return NavEvent.DeepLink(this)
//}