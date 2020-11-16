package edu.uw.info448.demo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.uw.info448.demo.network.GitHubApi
import edu.uw.info448.demo.network.GitHubSearchResponse
import edu.uw.info448.demo.network.Repo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var adapter: RepoListAdapter

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //set up the watcher (listener)
        val repoObserver = Observer<List<Repo>> {
            Log.v(TAG, "Updating: $it")
            adapter.submitList(it)
        }

        //start watching
        viewModel.repoData.observe(this, repoObserver)
        //if in a fragment: observe(viewLifecycleOwner, myObserver)

        /** RecyclerView **/
        adapter = RepoListAdapter()
        val recycler = findViewById<RecyclerView>(R.id.recycler_list)
        recycler.adapter = adapter

        /** Image Logo **/
        Glide.with(this).load("https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png")
            .into(findViewById<ImageView>(R.id.img_logo))



    }

    /** Button handlers **/
    fun getEmoji(v: View){
        Log.v(TAG, "Getting emojis...")

        //send the fetch request to get data from the Emojis endpoint
        //fetch(getEmojiEndpoint)
        GitHubApi.retrofitService.getEmoji().enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val body = response.body()
                Log.v(TAG, "$body")
//                findViewById<TextView>(R.id.txt_misc).text = body
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }
        })
    }

    fun getRepos(v: View){
        val input = findViewById<EditText>(R.id.edit_input).text.toString()
        Log.v(TAG, "Getting repos for $input")

        GitHubApi.retrofitService.getReposForOrg(input).enqueue(object: Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                val body = response.body()
                Log.v(TAG, "$body")

                adapter.submitList(body)

//                findViewById<TextView>(R.id.txt_misc).text = body
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                Log.e(TAG, "Failure: ${t.message}")
            }
        })
    }

    fun searchRepos(v: View){
        val input = findViewById<EditText>(R.id.edit_input).text.toString()
        Log.v(TAG, "Searching repos for $input")

        viewModel.searchRepos(input)


    }
}


class RepoListAdapter() : ListAdapter<Repo, RepoListAdapter.ViewHolder>(RepoDiffCallback()) {

    //Store references to the views (that the adapter connects to the data to)
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //get a reference to any Views we want to modify _per element_ in RecyclerView
        val txtView: TextView = view.findViewById<TextView>(R.id.txt_item)
    }

    //Called by the RecyclerView widget when we need to create a new View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //turn XML into Java elements (instantiated)
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item, //this xml
            parent,
            false
        )
        return ViewHolder(inflatedView)
    }

    //called whenever the data is connected to the View
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val theItem = getItem(position) //get the position-th element from the data

        //how do I want the data to show up in the view?
        //dynamically assign View attributes!
        holder.txtView.text = theItem!!.name //theItem is a Repo!

        holder.txtView.setOnClickListener {
            Toast.makeText(holder.txtView.context, "You clicked on ${theItem.name}", Toast.LENGTH_SHORT).show()
        }

    }
}

class RepoDiffCallback : DiffUtil.ItemCallback<Repo>() {
    override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
        return oldItem == newItem
    }
}