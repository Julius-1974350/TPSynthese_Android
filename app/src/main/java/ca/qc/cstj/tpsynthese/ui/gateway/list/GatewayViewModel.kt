package ca.qc.cstj.tpsynthese.ui.gateway.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tpsynthese.data.repositories.GatewayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GatewayViewModel : ViewModel() {

    private val gatewayRepository = GatewayRepository()

    private val _gatewayUiState = MutableStateFlow<GatewayUIState>(GatewayUIState.Loading)
    val gatewayUIState = _gatewayUiState.asStateFlow()

    init {
        viewModelScope.launch {
            gatewayRepository.retrieveAll().collect {
                _gatewayUiState.update { _ ->
                    when(it) {
                        is ApiResult.Error -> GatewayUIState.Error(it.exception)
                        ApiResult.Loading -> GatewayUIState.Loading
                        is ApiResult.Success -> GatewayUIState.Success(it.data)
                    }
                }
            }
        }
    }

}