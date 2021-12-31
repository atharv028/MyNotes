package com.tare.mynotes

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tare.mynotes.db.DBHandler
import com.tare.mynotes.entities.Notes
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CreateNoteFragment : BaseFragment(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {
    private lateinit var date: TextView
    private lateinit var back: ImageView
    private lateinit var done: ImageView
    private lateinit var text: EditText
    private lateinit var title: EditText
    private lateinit var subTitle: EditText
    private lateinit var more: ImageView
    private lateinit var colorView: View
    private lateinit var imgNote: ImageView
    private lateinit var llLink: LinearLayout
    private lateinit var url: EditText
    private lateinit var ok: Button
    private lateinit var cancel: Button
    private lateinit var tvLink: TextView
    private lateinit var rlImg: RelativeLayout
    private lateinit var delete: ImageView
    private lateinit var urlDel : ImageView
    private lateinit var llShowLink : LinearLayout
    private var webLink = ""
    private var noteId : Long = -1
    private var currDate: String? = null
    var selectedColor = "#171C26"
    private val READ_STORAGE_PERM = 123
    private val REQUEST_IMAGE = 456
    private var selectedImgPath = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (arguments != null)
            noteId = arguments?.getLong("noteId")!!

        val v = inflater.inflate(R.layout.fragment_create_note, container, false)
        date = v.findViewById(R.id.tvDateTime)
        back = v.findViewById(R.id.ivBack)
        done = v.findViewById(R.id.ivDone)
        text = v.findViewById(R.id.etNoteDesc)
        title = v.findViewById(R.id.etNoteTitle)
        subTitle = v.findViewById(R.id.etNoteSubTitle)
        more = v.findViewById(R.id.ivNoteMore)
        colorView = v.findViewById(R.id.colorView)
        imgNote = v.findViewById(R.id.imgNote)
        llLink = v.findViewById(R.id.llLinkk)
        url = v.findViewById(R.id.etNoteLink)
        ok = v.findViewById(R.id.btnOk)
        cancel = v.findViewById(R.id.btnCancel)
        tvLink = v.findViewById(R.id.tvNoteLink)
        delete = v.findViewById(R.id.ivDelete)
        rlImg = v.findViewById(R.id.rlNote)
        urlDel = v.findViewById(R.id.ivLinkDelete)
        llShowLink = v.findViewById(R.id.llShowLink)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ENGLISH)
        currDate = sdf.format(Date())
        date.text = currDate
        colorView.setBackgroundColor(Color.parseColor(selectedColor))

        if (noteId != (-1).toLong()) {
            launch {
                context?.let {
                    val curr = DBHandler(it).getNote(noteId)
                    colorView.setBackgroundColor(Color.parseColor(curr.color))
                    title.setText(curr.title)
                    subTitle.setText(curr.subTitle)
                    text.setText(curr.noteText)
                    if (!curr.imgPath.isNullOrBlank()) {
                        selectedImgPath = curr.imgPath!!
                        imgNote.setImageBitmap(BitmapFactory.decodeFile(curr.imgPath))
                        rlImg.visibility = View.VISIBLE
                        delete.visibility = View.VISIBLE
                        imgNote.visibility = View.VISIBLE
                    } else {
                        rlImg.visibility = View.GONE
                        delete.visibility = View.GONE
                        imgNote.visibility = View.GONE
                    }

                    if (curr.webLink.isNullOrBlank()) {
                        tvLink.visibility = View.GONE
                        urlDel.visibility = View.GONE
                    } else {
                        webLink = curr.webLink!!
                        tvLink.text = curr.webLink
                        llShowLink.visibility = View.VISIBLE
                        tvLink.visibility = View.VISIBLE
                        urlDel.visibility = View.VISIBLE
                    }
                }
            }
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            broadcastReceiver, IntentFilter("bottom_sheet_action")
        )

        back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        done.setOnClickListener {
            if(noteId != (-1).toLong())
            {
                updateNote()
            }else
                saveNote()
        }

        more.setOnClickListener {
            val noteBottomSheetFragment = NoteBottomSheetFragment.newInstance(noteId)
            noteBottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                "Note Bottom Sheet Fragment"
            )

        }

        ok.setOnClickListener {
            if (url.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter something in the url box",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            } else {
                checkWebUrl()
            }
        }

        delete.setOnClickListener {
            rlImg.visibility = View.GONE
            selectedImgPath = ""
        }

        cancel.setOnClickListener {
            if (noteId != (-1).toLong()) {
                tvLink.visibility = View.VISIBLE
                rlImg.visibility = View.GONE
            } else {
                selectedImgPath = ""
                llLink.visibility = View.GONE
            }
        }

        tvLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url.text.toString()))
            startActivity(intent)
        }

        urlDel.setOnClickListener {
            webLink = ""
            llShowLink.visibility = View.GONE

        }
    }

    private fun updateNote()
    {
        launch {
            context?.let {
                val note = DBHandler(it).getNote(noteId)
                Log.d("NOTE RETERIEVE", note.toString())
                note.title = title.text.toString()
                note.subTitle = subTitle.text.toString()
                note.noteText = text.text.toString()
                note.dateTime = currDate
                note.color = selectedColor
                note.imgPath = selectedImgPath
                note.webLink = webLink



                DBHandler(it).updateNote(note)
                Log.d("DB RES", DBHandler(it).allNotes().toString())
                title.setText("")
                subTitle.setText("")
                text.setText("")
                rlImg.visibility = View.GONE
                imgNote.visibility = View.GONE
                tvLink.visibility = View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun saveNote() {
        if (title.text.isNullOrEmpty()) {
            Toast.makeText(context, "Please Enter a Title", Toast.LENGTH_SHORT).show()
            return
        }
        if (subTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "Please Enter a Subtitle", Toast.LENGTH_SHORT).show()
            return
        }
        if (text.text.isNullOrEmpty()) {
            Toast.makeText(context, "Please Enter Text in Note", Toast.LENGTH_SHORT).show()
            return
        }

        launch {
            val note = Notes()
            note.title = title.text.toString()
            note.subTitle = subTitle.text.toString()
            note.noteText = text.text.toString()
            note.dateTime = currDate
            note.color = selectedColor
            note.imgPath = selectedImgPath
            note.webLink = webLink
            context?.let {
                DBHandler(it).insert(note)
                Log.d("DB RES", DBHandler(it).allNotes().toString())
                title.setText("")
                subTitle.setText("")
                text.setText("")
                rlImg.visibility = View.GONE
                imgNote.visibility = View.GONE
                tvLink.visibility = View.GONE
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }

    private fun checkWebUrl() {
        if (Patterns.WEB_URL.matcher(url.text.toString()).matches()) {
            llLink.visibility = View.GONE
            url.isEnabled = false
            webLink = url.text.toString()
            tvLink.visibility = View.VISIBLE
            tvLink.text = url.text.toString()
            urlDel.visibility = View.VISIBLE
        } else {
            Toast.makeText(requireContext(), "Please enter a valid url", Toast.LENGTH_SHORT).show()
        }
    }


    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val actionColor = intent!!.getStringExtra("actionColor")

            when (actionColor!!) {
                "blue" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "yellow" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "white" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "purple" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "green" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "orange" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "black" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "image" -> {
                    readStorageTask()
                    llLink.visibility = View.GONE
                }

                "link" -> {
                    llLink.visibility = View.VISIBLE
                }

                "delete" -> {
                    deleteNote()
                }

                else -> {
                    rlImg.visibility = View.GONE
                    imgNote.visibility = View.GONE
                    llLink.visibility = View.GONE
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorView.setBackgroundColor(Color.parseColor(selectedColor))
                }

            }
        }

    }

    private fun deleteNote()
    {
        launch {
            context?.let {
                val res = DBHandler(it).delete(noteId)
                Log.d("DELETE", res.toString())
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun hasStorageReadPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun readStorageTask() {
        if (hasStorageReadPermission()) {
            pickImageFromGallery()
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.read_storage),
                READ_STORAGE_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        //if(intent.resolveActivity(requireActivity().packageManager) != null)

        Log.d("IMG{I", "BRUH")
        startActivityForResult(intent, REQUEST_IMAGE)

    }

    private fun getPathFromUri(uri: Uri): String? {
        val filePath: String?
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        if (cursor == null) {
            filePath = uri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            Log.d("IMG", "BRUH")
            if (data != null) {
                val selectedImageUrl = data.data
                if (selectedImageUrl != null) {
                    try {
                        val inputStream =
                            requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imgNote.setImageBitmap(bitmap)
                        imgNote.visibility = View.VISIBLE
                        rlImg.visibility = View.VISIBLE
                        delete.visibility = View.VISIBLE
                        selectedImgPath = getPathFromUri(selectedImageUrl)!!
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            requireActivity()
        )
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRationaleAccepted(requestCode: Int) {
    }

    override fun onRationaleDenied(requestCode: Int) {
    }

}