package com.martinscomic.comicrepublic


import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.billkainkoom.quickdialog.QuickDialog
import com.billkainkoom.quickdialog.QuickDialogType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_comic_republic_splash.*


class ComicRepublicSplashActivity : AppCompatActivity() {

    private var button: Button? = null
    var context: Context? = null
    private val RC_SIGN_IN = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_republic_splash)





// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
// Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);




        movingtt.setSingleLine()
        movingtt.isSelected = true
        movingtt.text=""


        button = findViewById(R.id.jumpin) as Button

        context= this


        jumpin.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
            logoshs.startAnimation(animation)
            Handler().postDelayed({ logoshs.visibility = View.GONE }, 2800)
            Toast.makeText(applicationContext,R.string.opening_message,Toast.LENGTH_LONG).show()

            startActivity(Intent(this, SnapsActivity::class.java))



            d2(context as ComicRepublicSplashActivity)

        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        jumpin.startAnimation(animation)
        Handler().postDelayed({ jumpin.visibility = View.GONE }, 2300)
        logoshs.startAnimation(animation)

        Handler().postDelayed({ logoshs.visibility = View.GONE }, 2500)

        siiiiiggnout.startAnimation(animation)


        texxxt.startAnimation(animation)

        siiiiiggnout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            Toast.makeText(applicationContext,R.string.signing_out,Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()
        }

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            tv_name.text = acct.displayName
            tv_name.visibility = View.VISIBLE
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }


    fun d2(context: Context) {

        QuickDialog(

            context = context,
            style = QuickDialogType.Progress,
            title = "Please wait",
            image = R.drawable.littlelogo,
            message = "Opening the World Of Heroes")
            .overrideButtonNames("OK").overrideClicks({ ->

            }).show()
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification For Exit")
        builder.setIcon(R.drawable.comicmainbg)
        builder.setMessage("Are you sure you want to Exit Comic Republic?")
        builder.setPositiveButton("Yes",{ dialogInterface: DialogInterface, i: Int ->
            finish()
        })
        builder.setNegativeButton("No, Stay! \uD83D\uDE0A",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()




    }



    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)

            tv_name.text = account!!.displayName
            tv_name.visibility = View.VISIBLE
        } catch (e: ApiException) {
           tv_name.text = ""
            tv_name.visibility = View.VISIBLE
        }
    }

}
