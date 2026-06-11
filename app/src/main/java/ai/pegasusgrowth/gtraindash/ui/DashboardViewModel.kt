package ai.pegasusgrowth.gtraindash.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ai.pegasusgrowth.gtraindash.data.MtaDataService
import ai.pegasusgrowth.gtraindash.data.SettingsManager
import ai.pegasusgrowth.gtraindash.data.TrainArrivals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed interface DashboardUiState {
    object NoApiKey : DashboardUiState
    object Loading : DashboardUiState
    data class Success(
        val arrivals: TrainArrivals,
        val isRefreshing: Boolean = false,
        val lastSuccessfulFetchEpochSeconds: Long = 0L
    ) : DashboardUiState
    data class Error(
        val message: String,
        val cachedArrivals: TrainArrivals? = null
    ) : DashboardUiState
}

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsManager = SettingsManager(application)
    private val mtaService = MtaDataService()

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var pollingJob: Job? = null

    init {
        checkApiKeyAndStartPolling()
    }

    fun checkApiKeyAndStartPolling() {
        if (!settingsManager.hasApiKey()) {
            _uiState.value = DashboardUiState.NoApiKey
            stopPolling()
        } else {
            startPolling()
        }
    }

    fun saveApiKey(key: String) {
        settingsManager.saveApiKey(key)
        checkApiKeyAndStartPolling()
    }

    fun clearApiKey() {
        settingsManager.clearApiKey()
        checkApiKeyAndStartPolling()
    }

    fun getSavedApiKey(): String {
        return settingsManager.getApiKey().orEmpty()
    }

    fun refreshData() {
        val apiKey = settingsManager.getApiKey()
        if (apiKey.isNullOrBlank()) {
            _uiState.value = DashboardUiState.NoApiKey
            return
        }

        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is DashboardUiState.Success) {
                _uiState.value = currentState.copy(isRefreshing = true)
            } else if (currentState is DashboardUiState.Error) {
                _uiState.value = DashboardUiState.Loading
            }

            fetchDataImmediately(apiKey)
        }
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            val apiKey = settingsManager.getApiKey() ?: return@launch
            
            // Set loading state only if we don't have success state already
            if (_uiState.value !is DashboardUiState.Success) {
                _uiState.value = DashboardUiState.Loading
            }

            while (true) {
                fetchDataImmediately(apiKey)
                // Poll every 30 seconds
                delay(30000L)
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    private suspend fun fetchDataImmediately(apiKey: String) {
        val result = withContext(Dispatchers.IO) {
            mtaService.fetchArrivals(apiKey)
        }

        if (result.errorMessage != null) {
            val currentSuccess = _uiState.value as? DashboardUiState.Success
            _uiState.value = DashboardUiState.Error(
                message = result.errorMessage,
                cachedArrivals = currentSuccess?.arrivals
            )
        } else {
            _uiState.value = DashboardUiState.Success(
                arrivals = result,
                isRefreshing = false,
                lastSuccessfulFetchEpochSeconds = result.lastUpdatedEpochSeconds
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopPolling()
    }
}
