package com.martinscomic.comicrepublic

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_email_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.signup_link_btn

class EmailSignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_sign_in)

        signup_link_btn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        login_btn.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser()
    {
        val email = email_login.text.toString()
        val password = password_login.text.toString()

        when
        {
            TextUtils.isEmpty(email) -> Toast.makeText(this, "Email is required.", Toast.LENGTH_LONG).show()
            TextUtils.isEmpty(password) -> Toast.makeText(this, "Password  is required.", Toast.LENGTH_LONG).show()
            else ->
            {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Login")
                progressDialog.setMessage("Please wait, this might take a moment...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->
                    if (task.isSuccessful)
                    {
                        progressDialog.dismiss()
                        val intent = Intent(this, ComicRepublicSplashActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        val  message = task.exception!!.toString()
                        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
                        FirebaseAuth.getInstance().signOut()
                        progressDialog.dismiss()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null)
        {
            val intent = Intent(this, ComicRepublicSplashActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
