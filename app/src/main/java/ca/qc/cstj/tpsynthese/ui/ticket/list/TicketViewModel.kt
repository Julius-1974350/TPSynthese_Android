package ca.qc.cstj.tpsynthese.ui.ticket.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tpsynthese.data.repositories.TicketRepository
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicketViewModel : ViewModel() {

    private val ticketRepository = TicketRepository()

    private val _ticketUiState = MutableStateFlow<TicketUiState>(TicketUiState.Loading)
    val ticketUiState = _ticketUiState.asStateFlow()

    init {
        refreshTickets()
    }

    fun refreshTickets() {
        viewModelScope.launch {
            ticketRepository.retrieveAll().collect {
                _ticketUiState.update { _ ->
                    when(it) {
                        is ApiResult.Error -> TicketUiState.Error(it.exception as Exception)
                        ApiResult.Loading -> TicketUiState.Loading
                        is ApiResult.Success -> TicketUiState.Success(it.data)
                    }
                }
            }
        }
    }
}