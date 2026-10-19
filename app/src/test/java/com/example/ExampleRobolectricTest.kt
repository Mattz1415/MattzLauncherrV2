package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.DefaultLauncherCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context and verify 52 tools catalog`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("MattzLauncher", appName)
        assertEquals(52, DefaultLauncherCatalog.all52Tools.size)
        assertTrue(DefaultLauncherCatalog.jvmPresets.size >= 4)
        assertTrue(DefaultLauncherCatalog.defaultInstances.size >= 4)
    }
}
