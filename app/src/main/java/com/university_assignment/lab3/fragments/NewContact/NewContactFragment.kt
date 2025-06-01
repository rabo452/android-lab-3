package com.university_assignment.lab3.fragments.NewContact

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.textfield.TextInputEditText
import com.university_assignment.lab3.R
import com.university_assignment.lab3.data.model.ContactViewModel
import kotlin.getValue
import androidx.core.net.toUri
import com.university_assignment.lab3.domain.Contact
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class NewContactFragment : Fragment() {
    private val contactViewModel: ContactViewModel by activityViewModels()
    private lateinit var photoBlock: ImageView
    private lateinit var requestUri: Uri
    private lateinit var defaultAvatarUri: Uri
    private var userAvatarUri: Uri? = null

    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        if (result) {
            runCamera()
        } else {
            Toast
                .makeText(context, R.string.grant_camera_permission, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private val cameraLaunchRequest = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
        userAvatarUri = if (result) requestUri else defaultAvatarUri

        Glide.with(requireContext())
            .load(userAvatarUri)
            .apply(RequestOptions.circleCropTransform())
            .into(photoBlock)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context
        defaultAvatarUri = "android.resource://${requireContext().packageName}/${R.drawable.profile_icon}".toUri()

        val addContactBtn = view.findViewById<Button>(R.id.adding_contact_btn)
        val cancelContactBtn = view.findViewById<Button>(R.id.cancel_adding_btn)
        val takePhotoBtn = view.findViewById<Button>(R.id.take_photo_btn)

        val usernameInput = view.findViewById<TextInputEditText>(R.id.username_input)
        val emailInput = view.findViewById<TextInputEditText>(R.id.user_email_input)
        val phoneInput = view.findViewById<TextInputEditText>(R.id.user_phone_input)

        photoBlock = view.findViewById<ImageView>(R.id.user_avatar)

        cancelContactBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        addContactBtn.setOnClickListener {
            if (usernameInput.text!!.isEmpty() || !emailInput.text!!.toString().contains("@") || phoneInput.text!!.length < 10) {
                Toast
                    .makeText(context, R.string.invalid_contact_info, Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            contactViewModel.contacts.value = arrayOf(
                *(contactViewModel.contacts.value!!),
                Contact(
                    name = usernameInput.text!!.toString(),
                    email = emailInput.text!!.toString(),
                    phone = phoneInput.text!!.toString(),
                    avatar = userAvatarUri ?: defaultAvatarUri
                )
            )
            parentFragmentManager.popBackStack()
        }

        takePhotoBtn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionRequest.launch(Manifest.permission.CAMERA)
                return@setOnClickListener
            }

            runCamera()
        }
    }

    private fun runCamera() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/.thumbnail")
        }

        requestUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
        cameraLaunchRequest.launch(requestUri)
    }
}