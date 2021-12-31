package com.tare.mynotes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tare.mynotes.adapter.NotesAdapter
import com.tare.mynotes.db.DBHandler
import com.tare.mynotes.entities.Notes
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment(), NotesAdapter.OnItemClicked {
    private lateinit var fab : FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var search : SearchView
    private var adapter = NotesAdapter()
    private lateinit var arrList : ArrayList<Notes>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        fab = v.findViewById(R.id.fabNoteAdd)
        recyclerView = v.findViewById(R.id.rvHome)
        search = v.findViewById(R.id.search_view)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        arrList = DBHandler(requireContext()).allNotes() as ArrayList<Notes>
        Log.d("RV", arrList.toString())
        adapter.update(arrList)

        adapter.setOnClickListener(this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            replaceFragment(CreateNoteFragment(), true)
        }
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var tempArr = ArrayList<Notes>()
                for(item in arrList)
                {
                    if(item.title!!.lowercase(Locale.getDefault()).contains(newText.toString()
                            .lowercase(Locale.getDefault())))
                    {
                        tempArr.add(item)
                    }
                }
                adapter.update(tempArr)
                return true
            }

        })
    }

    private fun replaceFragment(fragment: Fragment, isTransition : Boolean)
    {
        val manager = requireActivity().supportFragmentManager.beginTransaction()
        if(isTransition)
        {
            manager.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        }
        manager.replace(R.id.flHome, fragment).addToBackStack(fragment.javaClass.simpleName)
        manager.commit()
    }

    override fun onClick(id: Long) {
        var fragment : Fragment
        var bundle = Bundle()
        bundle.putLong("noteId", id)
        fragment = CreateNoteFragment()
        fragment.arguments = bundle
        replaceFragment(fragment, false)

    }

}