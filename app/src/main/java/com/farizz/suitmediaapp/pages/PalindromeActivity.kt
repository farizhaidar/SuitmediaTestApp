package com.farizz.suitmediaapp.pages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.farizz.suitmediaapp.databinding.ActivityPalindromeBinding

class PalindromeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPalindromeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPalindromeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnCheck.setOnClickListener { handlePalindromeCheck() }
        binding.btnNext.setOnClickListener { handleNextButton() }
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
}
