package ca.qc.cstj.tpsynthese.data.datasources

import ca.qc.cstj.tenretni.core.Constants
import ca.qc.cstj.tenretni.core.JsonDataSource
import ca.qc.cstj.tpsynthese.domain.models.Gateway
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.encodeToJsonElement

class GatewayDataSource : JsonDataSource() {

    fun retrieveAll(href: String): List<Gateway> {
        val (_, _, result) = href.httpGet().responseJson()

        return when (result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }

    fun retrieveOne(customerId : Int): Gateway {
        val (_, _ , result) = (Constants.BaseURL.CUSTOMERS + customerId.toString() + Constants.BaseURL.GATEWAYS).httpGet().responseJson()

        return when(result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }

    }
    fun create(gateway: String) : Gateway {
        //Mettre en JSON
        val body = json.encodeToJsonElement(gateway)
        //Envoie au serveur avec un POST
        val (_, _, result) = (Constants.BaseURL.CUSTOMERS + gateway.customer.customerId + Constants.BaseURL.GATEWAYS).jsonBody(body).responseJson()
        //Gérer la réponse
        return when(result) {
            is Result.Success -> json.decodeFromString(result.value.content)
            is Result.Failure -> throw result.error.exception
        }
    }
}