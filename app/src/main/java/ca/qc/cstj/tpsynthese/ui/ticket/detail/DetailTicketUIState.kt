package ca.qc.cstj.tpsynthese.ui.ticket.detail

import ca.qc.cstj.tpsynthese.domain.models.Gateway
import ca.qc.cstj.tpsynthese.domain.models.Ticket

sealed class DetailTicketUIState {
    object Loading:DetailTicketUIState()
    class SuccessTicket(val ticket:Ticket):DetailTicketUIState()
    class SuccessGateways(val gateways:List<Gateway>):DetailTicketUIState()
    class GatewayInstallSuccess(val gateway: Gateway) : DetailTicketUIState()
    class GatewayInstallError(val exception: Exception? = null) : DetailTicketUIState()
    class Error(val exception: Exception? = null) : DetailTicketUIState()
}