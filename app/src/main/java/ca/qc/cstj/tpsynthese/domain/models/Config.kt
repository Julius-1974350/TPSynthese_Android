package ca.qc.cstj.tpsynthese.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Config(
    val mac:String,
    val SSID:String,
    val version:Float,
    val kernel : List<String>,
    val kernelRevision: String
)
