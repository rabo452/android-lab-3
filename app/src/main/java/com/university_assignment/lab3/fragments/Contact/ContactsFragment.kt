package com.university_assignment.lab3.fragments.Contact

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.university_assignment.lab3.R
import com.university_assignment.lab3.domain.Contact
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.university_assignment.lab3.data.model.ContactViewModel
import com.university_assignment.lab3.fragments.NewContact.NewContactFragment

class ContactsFragment : Fragment() {
    private lateinit var emptyContactListBlock: ConstraintLayout
    private lateinit var addContactBtn: Button
    private lateinit var contactListBLock: RecyclerView
    private val contactViewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val context = view.context
//        contactViewModel.contacts.value = arrayOf(
//            Contact(
//                name = "Alice Johnson",
//                email = "alice.johnson@example.com",
//                phone = "+1 555 123 4567",
//                avatar = Uri.parse("android.resource://${context.packageName}/${R.drawable.profile_icon}")
//            ),
//            Contact(
//                name = "Bob Smith",
//                email = "bob.smith@example.com",
//                phone = "+1 555 987 6543",
//                avatar = Uri.parse("android.resource://${context.packageName}/${R.drawable.profile_icon}")
//            ),
//            Contact(
//                name = "Carol Davis",
//                email = "carol.davis@example.com",
//                phone = "+1 555 246 8101",
//                avatar = Uri.parse("android.resource://${context.packageName}/${R.drawable.profile_icon}")
//            ),
//            Contact(
//                name = "David Lee",
//                email = "david.lee@example.com",
//                phone = "+1 555 369 1215",
//                avatar = Uri.parse("android.resource://${context.packageName}/${R.drawable.profile_icon}")
//            )
//        )

        emptyContactListBlock = view.findViewById<ConstraintLayout>(R.id.empty_contact_list_block)
        addContactBtn = view.findViewById<Button>(R.id.add_contact_btn)
        contactListBLock = view.findViewById<RecyclerView>(R.id.contact_list)
        addContactBtn.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_left_right, R.anim.stay_still,
                    0, R.anim.slide_left_right_back
                )
                .add(R.id.fragment_container, NewContactFragment())
                .hide(this)
                .addToBackStack(null)
                .commit()
        }

        contactViewModel.contacts.observe(viewLifecycleOwner, Observer { contacts ->
            // if no contacts, show empty Contact list block and hide contact list block
            contactListBLock.visibility = if (contacts.isEmpty()) View.GONE else View.VISIBLE
            emptyContactListBlock.visibility = if (contacts.isNotEmpty()) View.GONE else View.VISIBLE

            var adapter: ContactAdapter? = null
            adapter = ContactAdapter(contacts.toList()) { contactPosition ->
                val deletedContact = contacts[contactPosition]
                contactViewModel.contacts.value = contacts.filter { contact -> contact !== deletedContact }.toTypedArray()
            }
            contactListBLock.layoutManager = LinearLayoutManager(view.context)
            contactListBLock.adapter = adapter
        })
    }
}