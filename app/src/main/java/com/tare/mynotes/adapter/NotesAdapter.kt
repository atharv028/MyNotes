package com.tare.mynotes.adapter

import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.tare.mynotes.R
import com.tare.mynotes.entities.Notes

class NotesAdapter() : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    private var onClickListener : OnItemClicked? = null
    private var list =  ArrayList<Notes>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(v)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val curr = list[position]
        holder.text.text = curr.noteText
        holder.title.text = curr.title
        holder.dateTime.text = curr.dateTime

        holder.cardView.setOnClickListener {
            onClickListener?.onClick(curr.id)
        }
        if(curr.color != null)
        {
            holder.cardView.setCardBackgroundColor(Color.parseColor(curr.color))
        }else
        {
            holder.cardView.setCardBackgroundColor(Color.BLACK)
        }

        if(curr.imgPath != null)
        {
            holder.img.setImageBitmap(BitmapFactory.decodeFile(curr.imgPath))
            holder.img.visibility = View.VISIBLE
        }
        else
            holder.img.visibility = View.GONE

        if(curr.webLink.isNullOrEmpty())
        {
            holder.link.visibility = View.GONE
        }
        else
        {
            holder.link.text = curr.webLink
            holder.link.visibility = View.VISIBLE
        }



    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(updated : List<Notes>) {
        list = updated as ArrayList<Notes>
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener : OnItemClicked)
    {
        onClickListener = listener
    }

    inner class NotesViewHolder(item: View) : RecyclerView.ViewHolder(item)
    {
        val title : TextView = item.findViewById(R.id.tvNoteTitle)
        val dateTime : TextView = item.findViewById(R.id.tvNoteDateTime)
        val text : TextView = item.findViewById(R.id.tvNoteDesc)
        val cardView : CardView = item.findViewById(R.id.cvNote)
        val img : RoundedImageView = item.findViewById(R.id.rivImgNote)
        val link : TextView = item.findViewById(R.id.tvLink)
    }

    interface OnItemClicked{
        fun onClick(noteId : Long)
    }
}