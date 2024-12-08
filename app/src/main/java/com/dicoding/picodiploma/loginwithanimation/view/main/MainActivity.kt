package com.dicoding.picodiploma.loginwithanimation.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginActivity
import com.dicoding.picodiploma.loginwithanimation.view.stories.AddStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private val storyAdapter = StoryAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.show()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupView()
        setupAction()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLogoutConfirmationDialog() // Implement this function to handle logout
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Logout")
            setMessage("Are you sure you want to log out?")
            setPositiveButton("Yes") { _, _ ->
                logout() // Call your logout logic
            }
            setNegativeButton("Cancel", null)
            create()
            show()
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            // Clear the user session
            viewModel.logout()

            // Navigate to LoginActivity
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setupView() {
        binding.rvStory.apply {
            adapter = storyAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        viewModel.getAllStories().observe(this) { result ->
            when (result) {
                is com.dicoding.picodiploma.loginwithanimation.data.Result.Error -> {
                    binding.linearProgressBar.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }

                is com.dicoding.picodiploma.loginwithanimation.data.Result.Loading -> {
                    binding.linearProgressBar.visibility = View.VISIBLE
                }

                is com.dicoding.picodiploma.loginwithanimation.data.Result.Success -> {
                    binding.linearProgressBar.visibility = View.GONE
                    if (result.data.error == true) {
                        Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    } else {
                        storyAdapter.submitList(result.data.listStory)
                    }
                }
            }
        }

    }
    private fun setupAction() {
        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }}