package com.idw.project.kotlinnearbymaps.Model
import com.google.gson.annotations.SerializedName


data class PlaceDetail(
    @SerializedName("html_attributions")
    val htmlAttributions: List<Any>,
    @SerializedName("result")
    val result: Results,
    @SerializedName("status")
    val status: String
)

data class Results(
    @SerializedName("address_components")
    val addressComponents: List<AddressComponent>,
    @SerializedName("adr_address")
    val adrAddress: String,
    @SerializedName("formatted_address")
    val formattedAddress: String,
    @SerializedName("formatted_phone_number")
    val formattedPhoneNumber: String,
    @SerializedName("geometry")
    val geometry: Geometrys,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("international_phone_number")
    val internationalPhoneNumber: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("opening_hours")
    val openingHours: OpeningHourss,
    @SerializedName("photos")
    val photos: List<Photos>,
    @SerializedName("place_id")
    val placeId: String,
    @SerializedName("plus_code")
    val plusCode: PlusCodes,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("reviews")
    val reviews: List<Review>,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("types")
    val types: List<String>,
    @SerializedName("url")
    val url: String,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int,
    @SerializedName("utc_offset")
    val utcOffset: Int,
    @SerializedName("vicinity")
    val vicinity: String,
    @SerializedName("website")
    val website: String
)

data class AddressComponent(
    @SerializedName("long_name")
    val longName: String,
    @SerializedName("short_name")
    val shortName: String,
    @SerializedName("types")
    val types: List<String>
)

data class Geometrys(
    @SerializedName("location")
    val location: Locations,
    @SerializedName("viewport")
    val viewport: Viewports
)

data class Locations(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class Viewports(
    @SerializedName("northeast")
    val northeast: Northeasts,
    @SerializedName("southwest")
    val southwest: Southwests
)

data class Northeasts(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class Southwests(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)

data class OpeningHourss(
    @SerializedName("open_now")
    val openNow: Boolean,
    @SerializedName("periods")
    val periods: List<Period>,
    @SerializedName("weekday_text")
    val weekdayText: List<String>
)

data class Period(
    @SerializedName("close")
    val close: Close,
    @SerializedName("open")
    val `open`: Open
)

data class Close(
    @SerializedName("day")
    val day: Int,
    @SerializedName("time")
    val time: String
)

data class Open(
    @SerializedName("day")
    val day: Int,
    @SerializedName("time")
    val time: String
)

data class Photos(
    @SerializedName("height")
    val height: Int,
    @SerializedName("html_attributions")
    val htmlAttributions: List<String>,
    @SerializedName("photo_reference")
    val photoReference: String,
    @SerializedName("width")
    val width: Int
)

data class PlusCodes(
    @SerializedName("compound_code")
    val compoundCode: String,
    @SerializedName("global_code")
    val globalCode: String
)

data class Review(
    @SerializedName("author_name")
    val authorName: String,
    @SerializedName("author_url")
    val authorUrl: String,
    @SerializedName("language")
    val language: String,
    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String,
    @SerializedName("rating")
    val rating: Int,
    @SerializedName("relative_time_description")
    val relativeTimeDescription: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("time")
    val time: Int
)