package com.android.visitnow

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.visitnow.DataModel.DatabaseModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_destination_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlin.reflect.typeOf

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DestinationListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DestinationListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_destination_list, container, false)

        database = FirebaseDatabase.getInstance()

        val city = arrayOf("Bali", "Bandung", "Jakarta", "Jogja")
        val arrayAdapter = ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_item, city)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        val spinner = root.findViewById<Spinner>(R.id.destlist_spinner)

        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object :

            AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val selected = city[p2].toLowerCase()
                    reference = database.getReference("area/$selected")
                    getData()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    reference = database.getReference("area/jogja")
                    getData()
                }

            }

        /*val adapter = ArrayAdapter.createFromResource(
            this.requireActivity(),
            R.array.city_array,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter*/

        return root
    }

    private fun getData() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<DatabaseModel>()
                for (data in snapshot.children)
                {
                    val model = data.getValue(DatabaseModel::class.java)
                    list.add(model as DatabaseModel)
                }
                if (list.size > 0)
                {
                    val adapter = DestinationAdapter(list)
                    destlist_recycler_view.adapter = adapter
                    destlist_recycler_view.layoutManager = LinearLayoutManager(context)
                    destlist_recycler_view.setHasFixedSize(true)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Cancel", error.toString())
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment destinationListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DestinationListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}