package ca.qc.cstj.tpsynthese.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Connection(
    val status:String,
    val ip:String? = null,
    val download:Float? = null,
    val upload:Float? = null,
    val signal:Int? = null,
    val ping:Int? = null
)
