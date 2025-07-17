package com.farizz.suitmediaapp.pages

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.farizz.suitmediaapp.UserPreference
import com.farizz.suitmediaapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var userPref: UserPreference

    companion object {
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_SELECTED_USER = "EXTRA_SELECTED_USER"
        const val REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initUserPreference()
        loadDataFromIntent()
        setupUI()
        setupListeners()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Second Screen"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initUserPreference() {
        userPref = UserPreference(this)
    }

    private fun loadDataFromIntent() {
        intent.getStringExtra(EXTRA_NAME)?.let {
            userPref.saveName(it)
        }
    }

    private fun setupUI() {
        val nameFromPref = userPref.getName()
        val selectedUser = intent.getStringExtra(EXTRA_SELECTED_USER) ?: ""

        binding.tvName.text = nameFromPref
        binding.tvSelectedUser.text = if (selectedUser.isEmpty()) {
            "Selected User Name"
        } else {
            selectedUser
        }
    }

    private fun setupListeners() {
        binding.btnChooseUser.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivity(intent)
        }
    }
}
