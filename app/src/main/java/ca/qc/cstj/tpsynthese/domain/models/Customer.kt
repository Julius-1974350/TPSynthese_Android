package ca.qc.cstj.tpsynthese.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Customer(
    val href:String,
    val firstName: String? = null,
    val lastName:String? = null,
    val email:String? = null,
    val address:String? = null,
    val city:String? = null,
    val country:String? = null,
    val postalCode:String? = null,
    val phone:String? = null,
    val coord: Coordinate? = null,
)
