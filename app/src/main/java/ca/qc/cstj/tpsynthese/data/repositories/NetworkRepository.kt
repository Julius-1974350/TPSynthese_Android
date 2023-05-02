package ca.qc.cstj.tpsynthese.data.repositories

import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.data.datasources.NetworkDataSource
import ca.qc.cstj.tpsynthese.domain.models.Network
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NetworkRepository {
    private val networkDataSource = NetworkDataSource()
    fun retrieveAll() : Flow<ApiResult<Network>> {
        return flow {
            while (true)
            {
                emit(ApiResult.Loading)
                try {
                    emit(ApiResult.Success(networkDataSource.retrieveNetwork()))
                } catch (ex:Exception) {
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.RefreshDelay.LIST_NETWORK)
            }
        }.flowOn(Dispatchers.IO)
    }
}