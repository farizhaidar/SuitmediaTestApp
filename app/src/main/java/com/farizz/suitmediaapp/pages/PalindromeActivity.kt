package com.farizz.suitmediaapp.pages

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.farizz.suitmediaapp.databinding.ActivityPalindromeBinding

class PalindromeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPalindromeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPalindromeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        binding.ivProfile.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

    }

    private fun setupListeners() {
        binding.btnCheck.setOnClickListener { handlePalindromeCheck() }
        binding.btnNext.setOnClickListener { handleNextButton() }
        binding.ivProfile.setOnClickListener { pickImageFromGallery() }

    }

    private fun handlePalindromeCheck() {
        val input = binding.etPalindrome.text.toString().trim()

        if (input.isEmpty()) {
            binding.etPalindrome.error = "Teks tidak boleh kosong"
            return
        }

        val processed = input.replace(" ", "", ignoreCase = true)
        val isPalindrome = processed.equals(processed.reversed(), ignoreCase = true)

        val message = if (isPalindrome) "isPalindrome" else "not palindrome"
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun handleNextButton() {
        val input = binding.etName.text.toString().trim()

        if (input.isEmpty()) {
            binding.etName.error = "Teks tidak boleh kosong"
            return
        }

        val name = input
        val intent = Intent(this, WelcomeActivity::class.java)
        intent.putExtra("EXTRA_NAME", name)
        startActivity(intent)
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                if (imageUri != null) {
                    binding.ivProfile.setImageURI(imageUri)
                } else {
                    Toast.makeText(this, "Gagal memilih gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        pickImageLauncher.launch(intent)
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            Glide.with(this)
                .load(it)
                .circleCrop()
                .into(binding.ivProfile)
        }
    }

}
