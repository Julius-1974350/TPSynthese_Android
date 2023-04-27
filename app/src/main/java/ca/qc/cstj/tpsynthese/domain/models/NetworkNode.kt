package ca.qc.cstj.tpsynthese.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class NetworkNode(
    val name:String,
    val connection: Connection
)
