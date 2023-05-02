package ca.qc.cstj.tpsynthese.ui.network.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tpsynthese.data.repositories.NetworkRepository
import ca.qc.cstj.tpsynthese.ui.ticket.list.TicketUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NetworkViewModel : ViewModel() {
    private val networkRepository = NetworkRepository()
    private val _networkUiState = MutableStateFlow<ListNetworkUIState>(ListNetworkUIState.Loading)
    val networkUiState = _networkUiState.asStateFlow()

    init {
        viewModelScope.launch {
            networkRepository.retrieveAll().collect {
                _networkUiState.update { _ ->
                    when(it) {
                        is ApiResult.Error -> ListNetworkUIState.Error(it.exception)
                        ApiResult.Loading -> ListNetworkUIState.Loading
                        is ApiResult.Success -> ListNetworkUIState.Success(it.data)
                    }
                }
            }
        }
    }
}