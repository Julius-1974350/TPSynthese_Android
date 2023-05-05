package ca.qc.cstj.tpsynthese.ui.gateway.detail

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tpsynthese.data.repositories.GatewayRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailGatewayViewModel(private val href: String) : ViewModel() {
    private val detailGatewayRepository = GatewayRepository()

    private val _detailGatewayUiState = MutableStateFlow<DetailGatewayUIState>(DetailGatewayUIState.Loading)
    val detailGatewayUiState = _detailGatewayUiState.asStateFlow()

    init {
        viewModelScope.launch {
            detailGatewayRepository.retrieveOne(href).collect() {apiResult->
                _detailGatewayUiState.update {
                    when(apiResult) {
                        is ApiResult.Error -> DetailGatewayUIState.Error(apiResult.exception)
                        ApiResult.Loading -> DetailGatewayUIState.Loading
                        is ApiResult.Success -> DetailGatewayUIState.Success(apiResult.data)
                    }
                }
            }
        }
    }

    class Factory(private val href: String) : ViewModelProvider.Factory {
        override fun <T: ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java).newInstance(href)
        }
    }
    fun rebootGateway(href: String) {
        // viewModelScope.launch {
        //     gatewayRepository.reboot(href).collect(){ apiResult ->
        //         _detailGatewayUiState.update{
        //            when(apiResult){
        //                is ApiResult.Error -> TODO()
        //                ApiResult.Loading -> TODO()
        //                is ApiResult.Success -> TODO()
        //           }
        //       }
        //   }
        //  }
    }
        fun updateGateway(href: String){
            //  viewModelScope.launch {
            //     gatewayRepository.update(href).collect(){ apiResult ->
            //         _detailGatewayUiState.update{
            //            when(apiResult){
            //                is ApiResult.Error -> TODO()
            //                ApiResult.Loading -> TODO()
            //                is ApiResult.Success -> TODO()
            //           }
            //       }
            //   }
            // }
        }
}