package vn.edu.vlu.lab09

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import vn.edu.vlu.lab09.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private lateinit var viewModel: AuthViewModel
    private var register = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser != null) {
            openAsRoot(MainActivity::class.java)
            return
        }

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fitSystemBars(binding.root)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        register = savedInstanceState?.getBoolean("register") ?: false
        binding.modeToggle.check(if (register) R.id.modeRegister else R.id.modeLogin)
        renderMode()

        binding.modeToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                register = checkedId == R.id.modeRegister
                clearErrors()
                binding.etConfirmation.text?.clear()
                renderMode()
            }
        }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnReset.setOnClickListener {
            clearErrors()
            val email = validatedEmail() ?: return@setOnClickListener
            viewModel.requestPasswordReset(email)
        }
        viewModel.state.observe(this) { state ->
            setLoading(state == AuthViewModel.State.Loading)
            when (state) {
                AuthViewModel.State.SignedIn -> {
                    viewModel.consumeResult()
                    openAsRoot(MainActivity::class.java)
                }
                AuthViewModel.State.ResetRequested -> {
                    viewModel.consumeResult()
                    showMessage(R.string.reset_requested)
                }
                is AuthViewModel.State.Error -> {
                    viewModel.consumeResult()
                    showMessage(state.message)
                }
                else -> Unit
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("register", register)
        super.onSaveInstanceState(outState)
    }

    private fun renderMode() {
        binding.tilConfirmation.isVisible = register
        binding.btnReset.isVisible = !register
        binding.btnSubmit.setText(if (register) R.string.create_account else R.string.sign_in)
    }

    private fun submit() {
        clearErrors()
        val email = validatedEmail() ?: return
        val password = binding.etPassword.text.toString()
        if (password.length < 6) {
            binding.tilPassword.error = getString(R.string.error_weak_password)
            binding.etPassword.requestFocus()
            return
        }
        if (register && password != binding.etConfirmation.text.toString()) {
            binding.tilConfirmation.error = getString(R.string.error_password_mismatch)
            binding.etConfirmation.requestFocus()
            return
        }
        viewModel.submit(email, password, register)
    }

    private fun validatedEmail(): String? {
        val email = binding.etEmail.text.toString().trim()
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) return email

        binding.tilEmail.error = getString(R.string.error_invalid_email)
        binding.etEmail.requestFocus()
        return null
    }

    private fun clearErrors() {
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilConfirmation.error = null
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.isVisible = loading
        binding.modeLogin.isEnabled = !loading
        binding.modeRegister.isEnabled = !loading
        binding.tilEmail.isEnabled = !loading
        binding.tilPassword.isEnabled = !loading
        binding.tilConfirmation.isEnabled = !loading
        binding.btnSubmit.isEnabled = !loading
        binding.btnReset.isEnabled = !loading
    }

    private fun showMessage(message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}
