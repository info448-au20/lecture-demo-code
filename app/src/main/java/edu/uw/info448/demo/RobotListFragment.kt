package edu.uw.info448.demo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.ClassCastException

class RobotListFragment() : Fragment() {


    private var voteCount:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initializing instance variables
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_list, container, false)

        /** Vote Button **/
        arguments?.let{
            val initialVote = it.getInt(RobotListFragment.INITIAL_VOTE_PARAM_KEY)
            voteCount = initialVote
            rootView.findViewById<TextView>(R.id.txt_vote_count).text = "Current Votes: $voteCount"
        }

        rootView.findViewById<Button>(R.id.btn_vote).setOnClickListener {
            voteCount++ //increment
            rootView.findViewById<TextView>(R.id.txt_vote_count).text = "Current Votes: $voteCount"
        }

        /** RecyclerView **/
        //model
        val robots = resources.getStringArray(R.array.robot_array).toList()

        //controller (adapter)
        val adapter = StringListAdapter(robots)
        val recycler = rootView.findViewById<RecyclerView>(R.id.recycler_list)
        recycler.layoutManager = LinearLayoutManager(activity) //not the best
        recycler.adapter = adapter

        //set up our view
        return rootView
    }

    companion object {
        private val INITIAL_VOTE_PARAM_KEY = "InitialVote"
    }

    inner class StringListAdapter(private val theData: List<String>) : RecyclerView.Adapter<StringListAdapter.ViewHolder>() {

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
            val theItem = theData[position] //get the position-th element from the data

            //how do I want the data to show up in the view?
            //dynamically assign View attributes!
            holder.txtView.text = theItem //theItem is a string!

            holder.txtView.setOnClickListener {

                //CLICKING ON A ROBOT
                //val argBundle = Bundle()
                //argBundle.putString("robotName", theItem)
                //findNavController().navigate(R.id.RobotDetailFragment, argBundle)

                val action = RobotListFragmentDirections.actionToRobotDetailFragment(theItem)
                findNavController().navigate(action)


                    //Toast.makeText(holder.txtView.context, "You clicked on $theItem", Toast.LENGTH_SHORT).show()
            }

        }

        //how many things are in the data (as function expression / concise arrow func)
        override fun getItemCount() = theData.size
    }

}

