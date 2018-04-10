package com.chokus.konye.packmyload.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import com.chokus.konye.packmyload.R
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_get_started.*

class GetStartedActivity : AppCompatActivity() {
    private var callbackManager : CallbackManager? = null
    private var firebaseAuth : FirebaseAuth? = null
    private var ifConnected : Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        checkNetworkConnection()
        setFullScreen()
        viewActions()
        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()
    }
    private fun viewActions(){
        facebookLoginAction()
        use_phone_number_button.setOnClickListener{
            val intent = Intent(applicationContext, PhoneNumbersActivity:: class.java)
            startActivity(intent)
        }
    }
    private fun setFullScreen() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun facebookLoginAction(){
        callbackManager = CallbackManager.Factory.create()
        login_button.setReadPermissions("email")
        //call back registration

        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                // App code
                handleFacebookAccessToken(loginResult.accessToken)
            }
            override fun onCancel() {
                // App code
            }
            override fun onError(exception: FacebookException) {
                // App code
            }
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)

    }

    private fun handleFacebookAccessToken(token : AccessToken){
        Log.d("handle access title", "handleFacebookAccessToken:" + token)
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("sign in success title", "signInWithCredential:success")
                        val user = firebaseAuth!!.currentUser
                        startActivity(Intent(this, HomePageActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("sign in failure title", "signInWithCredential:failure", task.getException())
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    override fun onStart() {
        super.onStart()
        //check if there is a user currently signed in.. then take action
        val currentUser : FirebaseUser? = firebaseAuth?.currentUser
        if(currentUser != null){
            startActivity(Intent(this, HomePageActivity::class.java))
        }
    }

    private fun checkNetworkConnection() {
        val backgroundLayout = findViewById(R.id.get_started_activity_layout) as RelativeLayout
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            //Snackbar.make(backgroundLayout, "Connection successful", Snackbar.LENGTH_SHORT).show()
        } else {
            //we are not connected to a network
            Snackbar.make(backgroundLayout, "Oops! No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY") {
                        val intent = intent
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        finish()
                    }.setActionTextColor(resources.getColor(R.color.colorPrimary)).show()
        }
    }
}
