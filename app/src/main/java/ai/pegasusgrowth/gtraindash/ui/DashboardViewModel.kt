package ai.pegasusgrowth.gtraindash.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import ai.pegasusgrowth.gtraindash.data.MtaDataService
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
    private val mtaService = MtaDataService()

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var pollingJob: Job? = null

    init {
        startPolling()
    }

    fun refreshData() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState is DashboardUiState.Success) {
                _uiState.value = currentState.copy(isRefreshing = true)
            } else if (currentState is DashboardUiState.Error) {
                _uiState.value = DashboardUiState.Loading
            }

            fetchDataImmediately()
        }
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            if (_uiState.value !is DashboardUiState.Success) {
                _uiState.value = DashboardUiState.Loading
            }

            while (true) {
                fetchDataImmediately()
                delay(30000L) // Poll every 30 seconds
            }
        }
    }

    private fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    private suspend fun fetchDataImmediately() {
        val result = withContext(Dispatchers.IO) {
            mtaService.fetchArrivals()
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
