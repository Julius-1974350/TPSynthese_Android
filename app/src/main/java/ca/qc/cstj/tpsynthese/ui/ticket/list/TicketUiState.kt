package ca.qc.cstj.tpsynthese.ui.ticket.list

import ca.qc.cstj.tpsynthese.domain.models.Ticket

sealed class TicketUiState {
    object Loading: TicketUiState()
    class Success(val ticket: List<Ticket>): TicketUiState()
    class Error(val exception: Exception? = null): TicketUiState()
}