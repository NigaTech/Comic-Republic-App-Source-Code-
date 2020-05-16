package com.martinscomic.comicrepublic

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.martinscomic.comicrepublic.util.FirestoreUtil
import kotlinx.android.synthetic.main.activity_email_sign_in.*

import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class EmailSignInActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    val  RC_SIGN_IN_EMAIL: Int = 1


    private val signInProviders =
        listOf(
            AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .setRequireName(true)
                .build()
        )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_in)

        signin_link_btnww.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        login_btn.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setLogo(R.drawable.comicloginppic)
                .build()
            startActivityForResult(intent,  RC_SIGN_IN_EMAIL)
        }

        mAuth = FirebaseAuth.getInstance()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)



        if (requestCode == RC_SIGN_IN_EMAIL) {
            if (resultCode == Activity.RESULT_OK) {
                FirestoreUtil.initCurrentUserIfFirstTime {
                    startActivity(intentFor<ComicRepublicSplashActivity>().newTask().clearTask())
                    Toast.makeText(this, "Email Sign in Sucessful", Toast.LENGTH_LONG).show()
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                }

            }
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)

            } catch (e: ApiException) {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show()
            }


        }

    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, SignInActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            startActivity(Intent(this, ComicRepublicSplashActivity::class.java))
            finish()

        }

    }


}
