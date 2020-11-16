package edu.uw.info448.demo.network

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Where to find the API
private const val BASE_URL = "https://api.github.com/"

//The API Interface
interface GitHubApiService {
    @GET("emojis")
    fun getEmoji(): Call<String>

    @GET("orgs/{org}/repos")
    fun getReposForOrg(@Path("org") organization: String): Call<List<Repo>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): Call<GitHubSearchResponse>
}

//initialize Moshi
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//intialize Retrofit
private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object GitHubApi {
    val retrofitService: GitHubApiService by lazy {
        retrofit.create(GitHubApiService::class.java)
    }
}

data class Repo(
    val id: Int,
    val name: String,

    @Json(name = "full_name")
    val fullName: String,

    @Json(name = "html_url")
    val url: String
)

data class GitHubSearchResponse(
    @Json(name = "total_count")
    val totalCount: Int,
    val items:List<Repo>
)