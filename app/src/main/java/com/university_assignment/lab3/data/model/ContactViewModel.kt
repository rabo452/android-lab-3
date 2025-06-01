package com.university_assignment.lab3.data.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university_assignment.lab3.domain.Contact
class ContactViewModel : ViewModel() {
    var contacts = MutableLiveData<Array<Contact>>(arrayOf())
}