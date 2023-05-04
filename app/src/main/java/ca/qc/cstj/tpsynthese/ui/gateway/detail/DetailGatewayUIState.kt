package ca.qc.cstj.tpsynthese.ui.gateway.detail

import ca.qc.cstj.tpsynthese.domain.models.Gateway

sealed class DetailGatewayUIState {
    object Loading:DetailGatewayUIState()
    object Empty:DetailGatewayUIState()
    class Success(val gateway: Gateway) : DetailGatewayUIState()
    class Error(val exception: Exception? = null) : DetailGatewayUIState()
}