package ca.qc.cstj.tpsynthese.ui.gateway.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tpsynthese.data.repositories.GatewayRepository
import kotlinx.coroutines.launch

class DetailGatewayViewModel(private val href : String): ViewModel() {
    private val gatewayRepository = GatewayRepository()

    fun rebootGateway(href: String){
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