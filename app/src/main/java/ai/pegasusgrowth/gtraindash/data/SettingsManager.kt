package ai.pegasusgrowth.gtraindash.data

import android.content.Context
import android.content.SharedPreferences

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("g_train_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_MTA_API_KEY = "mta_api_key"
    }

    fun getApiKey(): String? {
        return prefs.getString(KEY_MTA_API_KEY, null)
    }

    fun saveApiKey(key: String) {
        prefs.edit().putString(KEY_MTA_API_KEY, key.trim()).apply()
    }
    
    fun clearApiKey() {
        prefs.edit().remove(KEY_MTA_API_KEY).apply()
    }

    fun hasApiKey(): Boolean {
        return !getApiKey().isNullOrBlank()
    }
}
