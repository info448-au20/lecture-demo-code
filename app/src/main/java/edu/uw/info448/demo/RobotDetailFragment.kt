package edu.uw.info448.demo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ROBOT_NAME_PARAM = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [RobotDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RobotDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var robotName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: RobotDetailFragmentArgs by navArgs() //object { robotName: "" }

        robotName = args.robotName;

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_robot_detail, container, false)

        rootView.findViewById<TextView>(R.id.txt_robot_name).text = "$robotName"

        return rootView
    }
}