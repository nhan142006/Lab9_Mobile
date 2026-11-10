package vn.edu.vlu.lab09

import android.content.Intent
import android.content.res.Configuration
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

internal fun AppCompatActivity.fitSystemBars(root: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val lightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK !=
        Configuration.UI_MODE_NIGHT_YES
    WindowCompat.getInsetsController(window, root).apply {
        isAppearanceLightStatusBars = lightMode
        isAppearanceLightNavigationBars = lightMode
    }
    ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
        val padding = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() or
                WindowInsetsCompat.Type.ime()
        )
        view.updatePadding(padding.left, padding.top, padding.right, padding.bottom)
        insets
    }
    ViewCompat.requestApplyInsets(root)
}

internal fun AppCompatActivity.openAsRoot(destination: Class<out AppCompatActivity>) {
    val intent = Intent(this, destination)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}
