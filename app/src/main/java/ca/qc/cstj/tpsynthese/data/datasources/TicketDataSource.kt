package ca.qc.cstj.tpsynthese.data.datasources

import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.core.JsonDataSource
import ca.qc.cstj.tpsynthese.domain.models.Ticket
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString

class TicketDataSource:JsonDataSource() {
    fun retrieveOne(href: String):Ticket{
        val (_,_,result)= href.httpGet().responseJson()

        return when (result){
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }
    fun retrieveAll(): List<Ticket> {
        val (_,_,result) = (Constants.BaseURL.BASE_API + Constants.BaseURL.TICKETS).httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

}