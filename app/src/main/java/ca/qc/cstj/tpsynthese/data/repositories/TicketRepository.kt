package ca.qc.cstj.tpsynthese.data.repositories

import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tpsynthese.data.datasources.TicketDataSource
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class TicketRepository {
    private val planetDataSource = TicketDataSource()

    fun retrieveOne(href: String) : Flow<ApiResult<Ticket>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                emit(ApiResult.Success(planetDataSource.retrieveOne(href)))
            }catch (ex:Exception){
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }
}