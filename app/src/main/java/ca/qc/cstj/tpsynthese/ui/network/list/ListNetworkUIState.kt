package ca.qc.cstj.tpsynthese.ui.network.list

import ca.qc.cstj.tpsynthese.domain.models.Network

sealed class ListNetworkUIState {
    object Loading: ListNetworkUIState()
    class Success(val network: Network): ListNetworkUIState()
    class Error(val exception: Exception? = null): ListNetworkUIState()
}