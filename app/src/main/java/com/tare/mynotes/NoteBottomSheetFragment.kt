package com.tare.mynotes

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoteBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var flNote1 : FrameLayout
    private lateinit var flNote2 : FrameLayout
    private lateinit var flNote3 : FrameLayout
    private lateinit var flNote4 : FrameLayout
    private lateinit var flNote5 : FrameLayout
    private lateinit var flNote6 : FrameLayout
    private lateinit var flNote7 : FrameLayout
    private lateinit var imgNote1 : ImageView
    private lateinit var imgNote2 : ImageView
    private lateinit var imgNote3 : ImageView
    private lateinit var imgNote4 : ImageView
    private lateinit var imgNote5 : ImageView
    private lateinit var imgNote6 : ImageView
    private lateinit var imgNote7 : ImageView
    private lateinit var imgAdd : LinearLayout
    private lateinit var linkAdd : LinearLayout
    private lateinit var noteDel : LinearLayout
    var selectedColor = "#171C26"

    companion object{
        private var noteId : Long = -1
        fun newInstance(id: Long): NoteBottomSheetFragment {
            val args = Bundle()
            val fragment = NoteBottomSheetFragment()
            fragment.arguments = args
            noteId = id
            return fragment
        }
    }
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_notes_bottom_sheet, null)
        dialog.setContentView(view)
        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behaviour = param.behavior
        if(behaviour is BottomSheetBehavior<*>)
        {
            behaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state = ""
                    when(newState){
                        BottomSheetBehavior.STATE_DRAGGING -> {
                            state = "DRAGGING"
                        }
                        BottomSheetBehavior.STATE_SETTLING -> {
                            state = "SETTLING"
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            state = "EXPANDED"
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            state = "COLLAPSED"
                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            state = "HIDDEN"
                            
                            behaviour.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_notes_bottom_sheet, container, false)
        flNote1 = v.findViewById(R.id.flNote1)
        flNote2 = v.findViewById(R.id.flNote2)
        flNote3 = v.findViewById(R.id.flNote3)
        flNote4 = v.findViewById(R.id.flNote4)
        flNote5 = v.findViewById(R.id.flNote5)
        flNote6 = v.findViewById(R.id.flNote6)
        flNote7 = v.findViewById(R.id.flNote7)
        imgNote1 = v.findViewById(R.id.imgNote1)
        imgNote2 = v.findViewById(R.id.imgNote2)
        imgNote3 = v.findViewById(R.id.imgNote3)
        imgNote4 = v.findViewById(R.id.imgNote4)
        imgNote5 = v.findViewById(R.id.imgNote5)
        imgNote6 = v.findViewById(R.id.imgNote6)
        imgNote7 = v.findViewById(R.id.imgNote7)
        imgAdd = v.findViewById(R.id.llImg)
        linkAdd = v.findViewById(R.id.llLink)
        noteDel = v.findViewById(R.id.llDeleteNote)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(noteId != (-1).toLong())
        {
            noteDel.visibility = View.VISIBLE
        }else
            noteDel.visibility = View.GONE

        setListener()
    }

    private fun setListener()
    {
        flNote1.setOnClickListener {
            imgNote1.setImageResource(R.drawable.ic_tick)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#4e33ff"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "blue")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            
        }
        flNote2.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(R.drawable.ic_tick)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#ffd633"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "yellow")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            
        }
        flNote3.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(R.drawable.ic_tick)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#ffffff"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "white")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            
        }
        flNote4.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(R.drawable.ic_tick)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#ae3b76"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "purple")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            
        }
        flNote5.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(R.drawable.ic_tick)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(0)
            selectedColor = "#0aebaf"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "green")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            
        }
        flNote6.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(R.drawable.ic_tick)
            imgNote7.setImageResource(0)
            selectedColor = "#ff7746"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "orange")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            
        }
        flNote7.setOnClickListener {
            imgNote1.setImageResource(0)
            imgNote2.setImageResource(0)
            imgNote3.setImageResource(0)
            imgNote4.setImageResource(0)
            imgNote5.setImageResource(0)
            imgNote6.setImageResource(0)
            imgNote7.setImageResource(R.drawable.ic_tick)
            selectedColor = "#202734"
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "black")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            
        }

        imgAdd.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "image")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }

        linkAdd.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "link")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }

        noteDel.setOnClickListener {
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("actionColor", "delete")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            dismiss()
        }

    }
}