package com.dicoding.picodiploma.loginwithanimation
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.dicoding.picodiploma.loginwithanimation.databinding.EmailtextBinding
import com.google.android.material.textfield.TextInputLayout
class EmailText  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    // Bind the XML layout (using ViewBinding or manually inflating)
    private val binding: EmailtextBinding =
        EmailtextBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        orientation = VERTICAL
        setupEmailValidation()
    }

    private fun setupEmailValidation() {
        binding.emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail(binding.emailTextInputLayout, binding.emailEditText.text.toString())
            }
        }

        // Add a TextWatcher for real-time validation
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Validate email on every text change
                validateEmail(binding.emailTextInputLayout, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    val text: String
        get() = binding.emailEditText.text.toString()

    private fun validateEmail(inputLayout: TextInputLayout, email: String) {
        if (!email.contains("@") || !email.endsWith(".com")) {
            inputLayout.error = "Invalid email address"
            inputLayout.boxStrokeColor = Color.RED
        } else {
            inputLayout.error = null // Clear the error
            inputLayout.boxStrokeColor = Color.BLUE
        }
    }

    // Method to get the entered email text
    fun getEmail(): String {
        return binding.emailEditText.text.toString()
    }

    // Method to manually set the error message
    fun setEmailError(errorMessage: String?) {
        binding.emailTextInputLayout.error = errorMessage
    }
}