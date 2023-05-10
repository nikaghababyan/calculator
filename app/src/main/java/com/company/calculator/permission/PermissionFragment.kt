package com.company.calculator.permission

import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.CompletableDeferred

class PermissionFragment : Fragment() {
    private var isEachRequest: Boolean = false
    var completableDeferred: CompletableDeferred<Boolean> = CompletableDeferred()
    var forEachCompletableDeferred: CompletableDeferred<List<Permission>> = CompletableDeferred()

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (isEachRequest) {
                forEachCompletableDeferred.complete(
                    permissions.map {
                        Permission(
                            permission = it.key,
                            isGranted = it.value,
                            shouldShowRational = showRequestPermissionRationale(it.key)
                        )
                    }
                )
                forEachCompletableDeferred = CompletableDeferred()
            } else {
                if (permissions.all { it.value }) {
                    completableDeferred.complete(true)
                } else {
                    permissions.map {
                        val shouldShowRational = showRequestPermissionRationale(it.key)
                        if (shouldShowRational) {
                            return@registerForActivityResult
                        }
                    }
                    completableDeferred.complete(false)
                }
                completableDeferred = CompletableDeferred()
            }
        }

    fun request(vararg permissions: String) {
        isEachRequest = false
        permissionRequest.launch(permissions as Array<String>?)
    }

    fun requestEach(vararg permissions: String) {
        isEachRequest = true
        permissionRequest.launch(permissions as Array<String>?)
    }

    private fun showRequestPermissionRationale(permission: String) =
        activity?.let {
            !isPermissionGranted(permission) && ActivityCompat.shouldShowRequestPermissionRationale(
                it,
                permission
            )
        } ?: false

    private fun isPermissionGranted(permission: String): Boolean =
        activity?.let {
            PermissionChecker.checkSelfPermission(
                it,
                permission
            ) == PermissionChecker.PERMISSION_GRANTED
        } ?: false

    override fun onDestroy() {
        super.onDestroy()
        if (completableDeferred.isActive) {
            completableDeferred.cancel()
        }
        if (forEachCompletableDeferred.isActive) {
            forEachCompletableDeferred.cancel()
        }
        completableDeferred = CompletableDeferred()
        forEachCompletableDeferred = CompletableDeferred()
    }

    companion object {
        private val FRAGMENT_TAG = PermissionFragment::class.java.simpleName

        fun newInstance(fragmentManager: FragmentManager): PermissionFragment {
            var fragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG)
            if (fragment != null) {
                fragmentManager.beginTransaction().remove(fragment).commit()
            }
            fragment = PermissionFragment()
            fragmentManager
                .beginTransaction()
                .apply {
                    if (fragment.isAdded) {
                        detach(fragment)
                    }
                }
                .add(fragment, FRAGMENT_TAG)
                .commitNow()
            return fragment
        }
    }
}
