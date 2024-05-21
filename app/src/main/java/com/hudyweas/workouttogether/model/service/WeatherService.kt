package com.hudyweas.workouttogether.model.service


import com.google.gson.Gson
import com.hudyweas.workouttogether.api.ForecastResponse
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class WeatherService @Inject constructor(private val httpClient: HttpClient) {
    suspend fun sendGet(city:String, date:String): ForecastResponse = withContext(Dispatchers.IO) {
        val API_KEY = "ETLHFFUVFZGYUNGTMZJDUR4EG"

        //convert date variable to format yyyy-MM-dd
        val formattedDate = convertDateString(date)
        val cityEn = replacePolishChars(city)
        val responseString = httpClient.get<String>("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/$cityEn/$formattedDate/$formattedDate?unitGroup=metric&include=days&key=$API_KEY&contentType=json")

        // use Gson
        val gson = Gson()
        val forecastResponse = gson.fromJson(responseString, ForecastResponse::class.java)
        return@withContext forecastResponse
    }
}

fun replacePolishChars(city: String): String {
    return city.replace("ą", "a").replace("ć", "c").replace("ę", "e").replace("ł", "l").replace("ń", "n").replace("ó", "o").replace("ś", "s").replace("ż", "z").replace("ź", "z")
}

fun convertDateString(inputDate: String): String {
    val inputFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    val date: Date? = inputFormat.parse(inputDate)
    return if (date != null) {
        outputFormat.format(date)
    } else {
        ""
    }
}