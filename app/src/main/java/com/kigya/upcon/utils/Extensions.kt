package com.kigya.upcon.utils

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

val Any.TAG: String
    get() {
        return if (!javaClass.isAnonymousClass) {
            javaClass.simpleName
        } else {
            javaClass.name
        }
    }

fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collector: suspend (T) -> Unit) {
    lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collector)
        }
    }
}

fun <T> T.mainScopeDelayLaunch(block: (T) -> Unit): Job {
    return MainScope().launch {
        delay(500L)
        block(this@mainScopeDelayLaunch)
    }
}
