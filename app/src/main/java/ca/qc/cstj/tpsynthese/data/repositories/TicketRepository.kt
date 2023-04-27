package ca.qc.cstj.tpsynthese.data.repositories

import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.data.datasources.TicketDataSource
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TicketRepository {
    private val ticketDataSource = TicketDataSource()
    fun retrieveAll() : Flow<ApiResult<List<Ticket>>> {
        return flow {
            while (true)
            {
                emit(ApiResult.Loading)
                try {
                    emit(ApiResult.Success(ticketDataSource.retrieveAll()))
                } catch (ex:Exception) {
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.RefreshDelay.LIST_TICKET)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun retrieveOne(href: String) : Flow<ApiResult<Ticket>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                emit(ApiResult.Success(ticketDataSource.retrieveOne(href)))
            }catch (ex:Exception){
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

}