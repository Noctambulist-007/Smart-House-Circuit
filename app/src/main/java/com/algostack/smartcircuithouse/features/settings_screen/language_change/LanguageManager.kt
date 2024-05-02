package com.algostack.smartcircuithouse.features.settings_screen.language_change

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

class LanguageManager(context: Context) {

    fun updateLanguage(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}