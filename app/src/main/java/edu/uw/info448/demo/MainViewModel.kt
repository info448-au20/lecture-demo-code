package edu.uw.info448.demo

import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.uw.info448.demo.network.GitHubApi
import edu.uw.info448.demo.network.GitHubSearchResponse
import edu.uw.info448.demo.network.Repo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val TAG = "MainViewModel"

    private var _repoData = MutableLiveData<List<Repo>>()

    val repoData: LiveData<List<Repo>>
        get() = _repoData

    fun searchRepos(query: String){
        GitHubApi.retrofitService.searchRepos(query).enqueue(object:
            Callback<GitHubSearchResponse> {
            override fun onResponse(call: Call<GitHubSearchResponse>, response: Response<GitHubSearchResponse>) {
                val body = response.body()
                Log.v(TAG, "$body")
                val repos = body!!.items

//                adapter.submitList(repos)
                _repoData.value = repos //put new data in the "watched container"
            }

            override fun onFailure(call: Call<GitHubSearchResponse>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }
        })
    }


}