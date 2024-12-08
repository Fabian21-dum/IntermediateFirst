package com.dicoding.picodiploma.loginwithanimation

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.dicoding.picodiploma.loginwithanimation.databinding.PasswordtextBinding
import com.google.android.material.textfield.TextInputLayout

class PasswordText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: PasswordtextBinding =
        PasswordtextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        orientation = VERTICAL
        setupPasswordValidation()
    }

    val text: String
        get() = binding.passwordEditText.text.toString()

    private fun setupPasswordValidation() {
        binding.passwordEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword(binding.passwordTextInputLayout, binding.passwordEditText.text.toString())
            }
        }

        // Add a TextWatcher for real-time validation
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Validate password on every text change
                validatePassword(binding.passwordTextInputLayout, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(inputLayout: TextInputLayout, password: String) {
        if (password.length < 8) {
            inputLayout.error = "Password must be at least 8 characters"
            inputLayout.boxStrokeColor = Color.RED
        } else {
            inputLayout.error = null // Clear the error
            inputLayout.boxStrokeColor = Color.BLUE
        }
    }

    // Utility functions
    fun getPassword(): String {
        return binding.passwordEditText.text.toString()
    }

    fun setPasswordError(errorMessage: String?) {
        binding.passwordTextInputLayout.error = errorMessage
    }
}