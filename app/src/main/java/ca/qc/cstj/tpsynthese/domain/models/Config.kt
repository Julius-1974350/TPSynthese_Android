package ca.qc.cstj.tpsynthese.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Config(
    val mac:String? = null,
    val SSID:String? = null,
    val version:String? = null,
    val kernel : List<String>? = null,
    val kernelRevision: Int? = null
)
