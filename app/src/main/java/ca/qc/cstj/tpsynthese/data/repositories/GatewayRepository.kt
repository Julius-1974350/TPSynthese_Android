package ca.qc.cstj.tpsynthese.data.repositories

import ca.qc.cstj.tenretni.core.ApiResult
import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tpsynthese.data.datasources.GatewayDataSource
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GatewayRepository {
    private val gatewayDataSource = GatewayDataSource()

    fun retrieveAll(customerId: Int) : Flow<ApiResult<List<Gateway>>> {
        return flow {
            while(true) {
                emit(ApiResult.Loading)
                try {
                    emit(ApiResult.Success(gatewayDataSource.retrieveAll(customerId)))
                } catch (ex:Exception) {
                    emit(ApiResult.Error(ex))
                }
                delay(Constants.RefreshDelay.LIST_GATEWAYS)
            }
        }.flowOn(Dispatchers.IO)
    }

    fun retrieveOne(customerId: Int) : Flow<ApiResult<Gateway>> {
        return flow {
            emit(ApiResult.Loading)
            try {
                emit(ApiResult.Success(gatewayDataSource.retrieveOne(customerId)))
            } catch(ex: Exception) {
                emit(ApiResult.Error(ex))
            }

        }.flowOn(Dispatchers.IO)
    }
    fun create(gateway: String) : Flow<ApiResult<Gateway>> {
        return flow {
            try {
                emit(ApiResult.Success(GatewayDataSource.create(gateway)))
            }catch (ex: java.lang.Exception) {
                emit(ApiResult.Error(ex))
            }
        }.flowOn(Dispatchers.IO)
    }

}