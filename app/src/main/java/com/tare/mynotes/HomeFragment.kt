package com.tare.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {
    private lateinit var fab : FloatingActionButton

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
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener {
            replaceFragment(CreateNoteFragment(), true)
        }
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

}