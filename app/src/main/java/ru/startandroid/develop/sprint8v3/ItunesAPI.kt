package ru.startandroid.develop.sprint8v3

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val getString = "/search?entity=song "
const val termForGet = "term"
interface ItunesAPI {
    @GET(getString)
    fun search(@Query(termForGet) text: String) : Call <ItunesResponse>
}