package com.nitap.attende.activities


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.nitap.attende.LoginUtils
import com.nitap.attende.MyUtils
import com.nitap.attende.R
import com.nitap.attende.databinding.ActivityOnboardingFinishBinding

class OnboardingFinishActivity : AppCompatActivity() {
   // private lateinit var btnStart: LinearLayout

    private lateinit var binding: ActivityOnboardingFinishBinding

    public lateinit var l:LoginUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingFinishBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        l =  LoginUtils(this)





        MyUtils.removeConfigurationBuilder(this)



        l.hasLeft = false
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        l.mGoogleSignInClient = GoogleSignIn.getClient(l.context, gso)
        l.mGoogleSignInClient.signOut()

        l.mAuth = FirebaseAuth.getInstance()
        if (l.mAuth.currentUser == null) {
            l.isEnabled = true
        } else {
            l.isEnabled = false
        }





    }


    fun getStartedClicked(view: View) {
        /*
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
         */
        Toast.makeText(this,"SignIn using your authorised account.",Toast.LENGTH_LONG).show()
        l.signOut(applicationContext)
        val signInIntent = l.mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, l.RC_SIGN_IN)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int,  resultCode: Int, data: Intent?) {
         val requestCodeNew = data?.getIntExtra("requestCode",123456)
        if (requestCodeNew != null) {
            super.onActivityResult(requestCodeNew, resultCode, data)
        }
        if (requestCode == l.RC_SIGN_IN && l.mAuth.currentUser == null) {
            Toast.makeText(applicationContext, "Got Google account", Toast.LENGTH_SHORT).show()
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account == null) {
                    Toast.makeText(this, "Failed to get the account", Toast.LENGTH_SHORT).show()
                }
                l.email = account!!.email
                l.display(this,l.email)
                l.firebaseAuthWithGoogle(this,this,account.idToken)
                //firebaseAuthWithGoogle(account.getIdToken());
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show()
                l.isEnabled = true
                //updateUI(null);
            }
        }
    }


    override fun onDestroy() {
       l.cleanData()
        super.onDestroy()
    }

}
