package com.varol.findmycar.data.remote.model

import com.squareup.moshi.Json

const val INVALID_ENUM = "invalid_enum"

data class CarResponseModel(
    val id: String? = null,
    val modelIdentifier: String? = null,
    val modelName: String? = null,
    val name: String? = null,
    val make: String? = null,
    val group: String? = null,
    val color: ColorEnumEntity? = null,
    val series: String? = null,
    val fuelType: FuelTypesEnumEntity? = null,
    val fuelLevel: Double? = null,
    val transmission: TransmissionEnumEntity? = null,
    val licensePlate: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val innerCleanliness: CleanlinessEnumEntity? = null,
    val carImageUrl: String? = null
)


enum class FuelTypesEnumEntity(val value: String) {
    INVALID(INVALID_ENUM),

    @Json(name = "D")
    DIESEL("DIESEL"),

    @Json(name = "P")
    PETROLIUM("PETROLIUM"),

    @Json(name = "E")
    ELECTRICITY("ELECTRICITY"),
}

enum class CleanlinessEnumEntity(val value: String) {
    INVALID(INVALID_ENUM),

    @Json(name = "REGULAR")
    REGULAR("REGULAR"),

    @Json(name = "CLEAN")
    CLEAN("CLEAN"),

    @Json(name = "VERY_CLEAN")
    VERY_CLEAN("VERY CLEAN"),
}

enum class TransmissionEnumEntity(val value: String) {
    INVALID(INVALID_ENUM),

    @Json(name = "M")
    MANUAL("MANUAL"),

    @Json(name = "A")
    AUTOMATIC("AUTOMATIC")
}

enum class ColorEnumEntity(val value: String) {
    INVALID(INVALID_ENUM),

    @Json(name = "midnight_black")
    MIDNIGHT_BLACK("midnight_black"),

    @Json(name = "midnight_black_metal")
    MIDNIGHT_BLACK_METAL("midnight_black_metal"),

    @Json(name = "hot_chocolate")
    HOT_CHOCOLATE("hot_chocolate"),

    @Json(name = "light_white")
    LIGHT_WHITE("light_white"),

    @Json(name = "iced_chocolate")
    ICED_CHOCALATE("iced_chocolate"),

    @Json(name = "alpinweiss")
    ALPS_WHITE("alpinweiss"),

    @Json(name = "saphirschwarz")
    SAPPHIRE_BLACK("saphirschwarz"),

    @Json(name = "iced_chocolate_metal")
    ICED_CHOCOLATE_METAL("iced_chocolate_metal"),

    @Json(name = "absolute_black_metal")
    ABSOLUTE_BLACK_METAL("absolute_black_metal"),

    @Json(name = "schwarz")
    BLACK("schwarz"),

}