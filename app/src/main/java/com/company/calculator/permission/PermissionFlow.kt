package com.company.calculator.permission

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.flow.flow

internal object PermissionFlow {

    internal fun request(fragment: Fragment, vararg permissionsToRequest: String) =
        request(fragment.childFragmentManager, *permissionsToRequest)

    internal fun request(activity: FragmentActivity, vararg permissionsToRequest: String) =
        request(activity.supportFragmentManager, *permissionsToRequest)

    internal fun requestEach(fragment: Fragment, vararg permissionsToRequest: String) =
        requestEach(fragment.childFragmentManager, *permissionsToRequest)

    internal fun requestEach(activity: FragmentActivity, vararg permissionsToRequest: String) =
        requestEach(activity.supportFragmentManager, *permissionsToRequest)

    private fun request(fragmentManager: FragmentManager, vararg permissionsToRequest: String) =
        flow {
            permissionsToRequest.takeIf { it.isNotEmpty() }?.let {
                createFragment(fragmentManager).run {
                    request(*it)
                    val results = completableDeferred.await()
                    emit(results)
                }
            }
        }

    private fun requestEach(fragmentManager: FragmentManager, vararg permissionsToRequest: String) =
        flow {
            permissionsToRequest.takeIf { it.isNotEmpty() }?.let {
                createFragment(fragmentManager).run {
                    requestEach(*it)
                    val results = forEachCompletableDeferred.await()
                    results.forEach { emit(it) }
                }
            }
        }

    private fun createFragment(fragmentManager: FragmentManager) =
        PermissionFragment.newInstance(fragmentManager)
}

// Extensions
fun FragmentActivity.requestPermissions(vararg permissionsToRequest: String) =
    PermissionFlow.request(this, *permissionsToRequest)

fun FragmentActivity.requestEachPermissions(vararg permissionsToRequest: String) =
    PermissionFlow.requestEach(this, *permissionsToRequest)

fun Fragment.requestPermissions(vararg permissionsToRequest: String) =
    PermissionFlow.request(this, *permissionsToRequest)

fun Fragment.requestEachPermissions(vararg permissionsToRequest: String) =
    PermissionFlow.requestEach(this, *permissionsToRequest)
