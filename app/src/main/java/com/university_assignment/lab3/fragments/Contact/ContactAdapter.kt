package com.university_assignment.lab3.fragments.Contact

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.university_assignment.lab3.R
import com.university_assignment.lab3.domain.Contact

class ContactAdapter(private var items: List<Contact>, private val deleteCallback: (position: Int) -> Unit): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    lateinit var context: Context
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val avatarBlock = itemView.findViewById<ImageView>(R.id.contact_icon_block)
        val usernameBlock = itemView.findViewById<TextView>(R.id.contact_name_block)
        val phoneBlock = itemView.findViewById<TextView>(R.id.contact_phone_block)
        val emailBlock = itemView.findViewById<TextView>(R.id.contact_email_block)
        val deleteContactBtn = itemView.findViewById<ImageView>(R.id.contact_delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = items[position]
        holder.usernameBlock.text = contact.name
        holder.phoneBlock.text = contact.phone
        holder.emailBlock.text = contact.email

        Glide.with(context)
            .load(contact.avatar)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.avatarBlock)

        holder.deleteContactBtn.setOnClickListener {
            deleteCallback(position)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<Contact>) {
        items = newItems
        notifyDataSetChanged()
    }
}