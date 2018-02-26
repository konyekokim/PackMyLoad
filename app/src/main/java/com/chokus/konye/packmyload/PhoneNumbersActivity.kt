package com.chokus.konye.packmyload

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_phone_numbers.*
import java.util.concurrent.TimeUnit

class PhoneNumbersActivity : AppCompatActivity() {
    var mVerificationId : String? = null
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var mAuth : FirebaseAuth? = null
    var mResendToken : PhoneAuthProvider.ForceResendingToken? = null
    var mCredential : PhoneAuthCredential? = null
    lateinit var userPhoneNumber : String
    var numberEditText : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_numbers)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.phone_numb)
    }

    private fun viewActions(){
        numberEditText = findViewById(R.id.number_editText) as EditText
        verify_number_butn.setOnClickListener {
            //do stuff here
            userPhoneNumber = numberEditText!!.text.toString()
            startPhoneNumberVerification(userPhoneNumber)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_phone_number,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.next){
            val intent = Intent(applicationContext, NumberVerificationActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startPhoneNumberVerification(phoneNumber : String){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, //phone number to verify
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks)

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // verification completed
                mCredential = credential
                signInWithPhoneAuthCredential(mCredential!!)
                showToast("verification completed")
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked if an invalid request for verification is made,
                // for instance if the the phone number format is invalid.
                showToast("Verification failed")
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    showToast("invalid phone number")
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    showToast("Quota exceeded")
                }

            }
            override fun onCodeSent(verificationId: String?,
                                    token: PhoneAuthProvider.ForceResendingToken?) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId
                mResendToken = token

            }



            override fun onCodeAutoRetrievalTimeOut(verificationId: String?) {
                // called when the timeout duration has passed without triggering onVerificationCompleted
                super.onCodeAutoRetrievalTimeOut(verificationId)
            }
        }
    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        showToast("signInWithCredential issasuccess")
                    }else{
                        // Sign in failed, display a message and update the UI
                        showToast("signInWithCredential issafailure")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            showToast("Invalid code was entered")
                        }
                        // Sign in failed
                    }
                }
    }

    private fun resendVerificationCode(phoneNumber: String,
                                       token: PhoneAuthProvider.ForceResendingToken?) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this, // Activity (for callback binding)
                mCallbacks, // OnVerificationStateChangedCallbacks
                token)             // ForceResendingToken from callbacks
    }

    private fun showToast(toastText : String){
        Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()

    }

}
