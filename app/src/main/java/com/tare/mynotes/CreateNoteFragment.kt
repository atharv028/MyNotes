package com.tare.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.tare.mynotes.db.NotesDatabase
import com.tare.mynotes.entities.Notes
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteFragment : BaseFragment() {
    private lateinit var date : TextView
    private lateinit var back : ImageView
    private lateinit var done : ImageView
    private lateinit var text : EditText
    private lateinit var title : EditText
    private lateinit var subTitle : EditText
    private var currDate : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_create_note, container, false)
        date = v.findViewById(R.id.tvDateTime)
        back = v.findViewById(R.id.ivBack)
        done = v.findViewById(R.id.ivDone)
        text = v.findViewById(R.id.etNoteDesc)
        title = v.findViewById(R.id.etNoteTitle)
        subTitle = v.findViewById(R.id.etNoteSubTitle)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ENGLISH)
        currDate = sdf.format(Date())
        date.text = currDate

        back.setOnClickListener{
            replaceFragment(HomeFragment(), false)
        }
        done.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote()
    {
        if(title.text.isNullOrEmpty())
        {
            Toast.makeText(context, "Please Enter a Title", Toast.LENGTH_SHORT).show()
            return
        }
        if(subTitle.text.isNullOrEmpty())
        {
            Toast.makeText(context, "Please Enter a Subtitle", Toast.LENGTH_SHORT).show()
            return
        }
        if(text.text.isNullOrEmpty())
        {
            Toast.makeText(context, "Please Enter Text in Note", Toast.LENGTH_SHORT).show()
            return
        }

        launch {
            val note = Notes()
            note.title = title.text.toString()
            note.subTitle = subTitle.text.toString()
            note.noteText = text.text.toString()
            note.dateTime = currDate
            context?.let {
                NotesDatabase.getDatabase(it).noteDao().insert(note)
                title.setText("")
                subTitle.setText("")
                text.setText("")
            }
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