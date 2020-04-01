package com.martinscomic.comicrepublic

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.martinscomic.comicrepublic.util.FirestoreUtil
import kotlinx.android.synthetic.main.activity_comic_republic_splash.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1

    private val signInProviders =
        listOf(
            AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .setRequireName(true)
                .build()
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)



        account_sign_in.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setLogo(R.drawable.comicloginppic)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)

        }

        signup_link_btn.setOnClickListener {
            startActivity(Intent(this, EmailSignInActivity::class.java))
        }


        printKeyHash()
    }

    private fun printKeyHash() {
        try {
            val info = packageManager.getPackageInfo("com.martinscomic.comicrepublic.SignInActivity", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT))
            }
        }
        catch (e:PackageManager.NameNotFoundException)
        {

        }
        catch (e:NoSuchAlgorithmException)
        {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            startActivity(Intent(this, ComicRepublicSplashActivity::class.java))

            if (resultCode == Activity.RESULT_OK) {
                val progressDialog =
                    indeterminateProgressDialog("Setting up your Comic Republic Account, This Might Take a While.......")
                FirestoreUtil.initCurrentUserIfFirstTime {
                    startActivity(intentFor<ComicRepublicSplashActivity>().newTask().clearTask())
                    progressDialog.dismiss()
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return


                when (response.error?.errorCode) {

                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, ComicRepublicSplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }


}