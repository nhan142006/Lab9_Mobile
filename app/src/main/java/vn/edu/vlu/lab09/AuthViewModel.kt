package vn.edu.vlu.lab09

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class AuthViewModel : ViewModel() {
    sealed interface State {
        data object Idle : State
        data object Loading : State
        data object SignedIn : State
        data object ResetRequested : State
        data class Error(val message: Int) : State
    }

    private val auth = FirebaseAuth.getInstance()
    private val mutableState = MutableLiveData<State>(State.Idle)
    val state: LiveData<State> = mutableState

    fun submit(email: String, password: String, register: Boolean) {
        runRequest(reset = false) {
            if (register) auth.createUserWithEmailAndPassword(email, password)
            else auth.signInWithEmailAndPassword(email, password)
        }
    }

    fun requestPasswordReset(email: String) {
        runRequest(reset = true) { auth.sendPasswordResetEmail(email) }
    }

    fun consumeResult() {
        mutableState.value = State.Idle
    }

    // Keep the pending request across rotation. The Activity only observes while active.
    private fun runRequest(reset: Boolean, start: () -> Task<*>) {
        if (mutableState.value == State.Loading) return
        mutableState.value = State.Loading
        start().addOnCompleteListener { task ->
            val failure = task.exception
            mutableState.value = when {
                task.isSuccessful -> if (reset) State.ResetRequested else State.SignedIn
                // Use the same reset response for an unknown email.
                reset && failure is FirebaseAuthInvalidUserException &&
                    failure.errorCode == "ERROR_USER_NOT_FOUND" -> State.ResetRequested
                else -> {
                    val code = (failure as? FirebaseAuthException)?.errorCode
                        ?: failure?.javaClass?.simpleName
                    Log.w("Lab09Auth", "Firebase request failed ($code): ${failure?.message}")
                    State.Error(errorMessage(failure))
                }
            }
        }
    }

    private fun errorMessage(error: Exception?): Int = when (error) {
        is FirebaseAuthWeakPasswordException -> R.string.error_weak_password
        is FirebaseAuthUserCollisionException -> R.string.error_duplicate_email
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> R.string.error_invalid_credentials
        is FirebaseNetworkException -> R.string.error_network
        is FirebaseTooManyRequestsException -> R.string.error_rate_limit
        else -> R.string.error_generic
    }
}
