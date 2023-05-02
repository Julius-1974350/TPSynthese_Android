package ca.qc.cstj.tpsynthese.ui.gateway.list

import ca.qc.cstj.tpsynthese.domain.models.Gateway


sealed class GatewayUIState {
    object Loading: GatewayUIState()
    class Success(val gateway: List<Gateway>): GatewayUIState()
    class Error(val exception: Exception? = null): GatewayUIState()
}