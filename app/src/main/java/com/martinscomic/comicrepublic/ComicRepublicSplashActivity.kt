package com.martinscomic.comicrepublic


import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.billkainkoom.quickdialog.QuickDialog
import com.billkainkoom.quickdialog.QuickDialogType
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_comic_republic_splash.*


class ComicRepublicSplashActivity : AppCompatActivity() {

    private var button: Button? = null
    var context: Context? = null
    private val RC_SIGN_IN = 1

    var logoutBtn: Button? = null
    var userName: TextView? = null
    var userEmail:TextView? = null
    var userId:TextView? = null

    var tv_image: ImageView? = null

    private val googleApiClient: GoogleApiClient? = null
    private val gso: GoogleSignInOptions? = null
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_republic_splash)

        button = findViewById(R.id.jumpin) as Button




        firebaseAuth = FirebaseAuth.getInstance()





        val mAuth = FirebaseAuth.getInstance().currentUser
        mAuth?.let {
            for (profile in it.providerData) {

                tv_emailems.text = mAuth.email

                Glide.with(this).load(mAuth?.photoUrl).into(tv_imagesssssss)



            }
        }





// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
// Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)



        movingtt.setSingleLine()
        movingtt.isSelected = true
        movingtt.text=""


        context= this



        tv_images.setOnClickListener {
            startActivity(Intent(this, ProfileAccountActivity::class.java))

        }

        settingggg.setOnClickListener {
            startActivity(Intent(this, ProfileAccountActivity::class.java))

        }


        jumpin.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
            logoshs.startAnimation(animation)
            Handler().postDelayed({ logoshs.visibility = View.GONE }, 2800)
            Toast.makeText(applicationContext,R.string.opening_message,Toast.LENGTH_LONG).show()
            startActivity(Intent(this, SnapsActivity::class.java))
            d2(context as ComicRepublicSplashActivity)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 10)
            }


            val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibratorService.vibrate(200)
        }

        openwallpp.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
            logoshs.startAnimation(animation)
            Handler().postDelayed({ logoshs.visibility = View.GONE }, 2800)
            Toast.makeText(applicationContext,R.string.opening_message,Toast.LENGTH_LONG).show()
            startActivity(Intent(this, ComicWallpaperActivity::class.java))
            d2(context as ComicRepublicSplashActivity)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 10)
            }


            val vibratorService = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibratorService.vibrate(200)
        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        jumpin.startAnimation(animation)
        Handler().postDelayed({ jumpin.visibility = View.GONE }, 2300)
        logoshs.startAnimation(animation)

        Handler().postDelayed({ logoshs.visibility = View.GONE }, 2500)


















        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {

            tv_email.text = acct.email
            tv_email.visibility = View.VISIBLE

            Glide.with(this).load(acct.photoUrl).into(tv_images)



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
            image = R.drawable.comiclogohome,
            message = "Opening the World Of Heroes")
            .overrideButtonNames("OK").overrideClicks({ ->

            }).show()
    }

    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification For Exit")
        builder.setIcon(R.drawable.comiclogohome)
        builder.setMessage("Are you sure you want to Exit Comic Republic?")
        builder.setPositiveButton("Yes",{ dialogInterface: DialogInterface, i: Int ->
            finish()
        })
        builder.setNegativeButton("No, Stay! \uD83D\uDE0A",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()




    }



    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) = try {
        val account =
            completedTask.getResult(ApiException::class.java)

        tv_email.text = account?.email
        tv_email.visibility = View.VISIBLE
        Glide.with(this).load(account?.photoUrl).into(tv_images)





    }
    catch (e: ApiException) {

        tv_email.text = ""
        tv_email.visibility = View.VISIBLE
        tv_image!!.visibility = View.VISIBLE
        Toast.makeText(applicationContext, "image not found", Toast.LENGTH_LONG).show()


    }


    fun onConnectionFailed(connectionResult: ConnectionResult) {}


}




