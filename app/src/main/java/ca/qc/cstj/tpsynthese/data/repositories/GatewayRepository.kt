package ca.qc.cstj.tpsynthese.data.repositories

import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tpsynthese.data.datasources.GatewayDataSource
import ca.qc.cstj.tpsynthese.data.datasources.TicketDataSource
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GatewayRepository {
    private val gatewayDataSource = GatewayDataSource()

    fun retrieveAllForCustomer(href: String) : Flow<ApiResult<List<Gateway>>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                emit(ApiResult.Success(gatewayDataSource.retrieveAllForCustomer(href)))
            }catch (ex:Exception){
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }
}