package com.senijoshua.shared_test.util

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.internalToRoute
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Test rule that serves as a work-around for mocking [SavedStateHandle] so as to side-step its
 * dependency on the Bundle Android API that is internally employed
 * when passing arguments via a navigation route with [SavedStateHandle].
 *
 * This rule negates the need to use Robolectric to test ViewModels
 * that employ a [SavedStateHandle] for accessing route arguments which
 * would effectively make it an instrumented test.
 */
class SavedStateHandleRule(
    private val route: Any,
) : TestWatcher() {
    val savedStateHandleMock: SavedStateHandle = mockk()

    @SuppressLint("RestrictedApi")
    override fun starting(description: Description) {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { savedStateHandleMock.internalToRoute<Any>(any(), any()) } returns route
        super.starting(description)
    }

    override fun finished(description: Description) {
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
        super.finished(description)
    }
}
