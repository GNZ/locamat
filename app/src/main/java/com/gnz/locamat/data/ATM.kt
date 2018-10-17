package com.gnz.locamat.data

import com.squareup.moshi.Json

data class ATM(
        val address: Address,
        val openingHours: OpeningHours,
        val ratings: Ratings,
        val category: String,
        val categoryText: String,
        val imagePath: String,
        val latitude: String,
        val longitude: String,
        val name: String,
        val sonectId: String,
        val tagline: String
)

data class Address(
        val city: String,
        val zip: String,
        val formatted: String
)

data class OpeningHours(
        val sunday: Sunday,
        val monday: Monday,
        val tuesday: Tuesday,
        val wednesday: Wednesday,
        val thursday: Thursday,
        val friday: Friday,
        val saturday: Saturday,
        val active: Boolean
)

data class Sunday(
        val open: String,
        val close: String
)

data class Monday(
        val open: String,
        val close: String
)

data class Tuesday(
        val open: String,
        val close: String
)

data class Wednesday(
        val open: String,
        val close: String
)

data class Thursday(
        val open: String,
        val close: String
)

data class Friday(
        val open: String,
        val close: String
)

data class Saturday(
        val open: String,
        val close: String
)

data class Overall(
        @Json(name = "1") val one: Int,
        @Json(name = "2") val two: Int,
        @Json(name = "3") val three: Int,
        @Json(name = "4") val four: Int,
        @Json(name = "5") val five: Int
)

data class Ratings(
        val overall: Overall,
        val all: List<Any>
)