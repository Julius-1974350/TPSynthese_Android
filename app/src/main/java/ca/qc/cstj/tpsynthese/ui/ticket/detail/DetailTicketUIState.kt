package ca.qc.cstj.tpsynthese.ui.ticket.detail

import ca.qc.cstj.tpsynthese.domain.models.Ticket

sealed class DetailTicketUIState {
    object Loading:DetailTicketUIState()
    class Success(val ticket:Ticket):DetailTicketUIState()
    class Error(val exception: Exception? = null) : DetailTicketUIState()
}