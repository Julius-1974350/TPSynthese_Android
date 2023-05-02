package ca.qc.cstj.tpsynthese.ui.ticket.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.data.repositories.GatewayRepository
import ca.qc.cstj.tpsynthese.data.repositories.TicketRepository
import ca.qc.cstj.tpsynthese.domain.models.Customer
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailTicketViewModel(private val href : String):ViewModel() {
    private val ticketRepository = TicketRepository()
    private val gatewayRepository = GatewayRepository()
    private val _detailTicketUIState = MutableStateFlow<DetailTicketUIState>(DetailTicketUIState.Loading)
    private var gateways = mutableListOf<Gateway>()
    val detailTicketUiState = _detailTicketUIState.asStateFlow()

    init {
        viewModelScope.launch {
            ticketRepository.retrieveOne(href).collect(){ apiResult ->
                _detailTicketUIState.update {
                    when(apiResult){
                        is ApiResult.Error -> DetailTicketUIState.Error(apiResult.exception)
                        ApiResult.Loading -> DetailTicketUIState.Loading
                        is ApiResult.Success -> DetailTicketUIState.SuccessTicket(apiResult.data)
                    }
                }
            }
        }
    }
    fun addGateway(rawValue: String, href: String) {
        viewModelScope.launch {
            gatewayRepository.create(rawValue, href).collect{ apiResult ->
                _detailTicketUIState.update {
                    when(apiResult) {
                        is ApiResult.Error -> DetailTicketUIState.GatewayInstallError(apiResult.exception)
                        ApiResult.Loading -> DetailTicketUIState.Loading
                        is ApiResult.Success -> {
                            gateways.add(apiResult.data)
                            DetailTicketUIState.GatewayInstallSuccess(gateways)
                        }
                    }
                }

            }
        }
    }
    fun getGateways(customer: Customer){
        viewModelScope.launch {
            gatewayRepository.retrieveAllForCustomer(customer.href).collect(){apiResult->
                _detailTicketUIState.update {
                    when(apiResult){
                        is ApiResult.Error -> DetailTicketUIState.Error(apiResult.exception)
                        ApiResult.Loading -> DetailTicketUIState.Loading
                        is ApiResult.Success ->{
                            gateways = apiResult.data.toMutableList()
                            DetailTicketUIState.SuccessGateways(apiResult.data)
                        }
                    }
                }
            }

        }
    }
    class Factory(private val href: String): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java).newInstance(href)
        }
    }
}