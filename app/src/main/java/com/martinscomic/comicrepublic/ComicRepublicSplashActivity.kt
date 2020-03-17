package com.martinscomic.comicrepublic

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_comic_republic_splash.*

class ComicRepublicSplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_republic_splash)


        movingtt.setSingleLine()
        movingtt.isSelected = true
        movingtt.text=""



        jumpin.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.zoom_out)
            logoshs.startAnimation(animation)
            Handler().postDelayed({ logoshs.visibility = View.GONE }, 2800)
            Toast.makeText(applicationContext,R.string.opening_message,Toast.LENGTH_LONG).show()

            startActivity(Intent(this, SnapsActivity::class.java))
        }

        val animation = AnimationUtils.loadAnimation(this, R.anim.bounce)
        jumpin.startAnimation(animation)
        Handler().postDelayed({ jumpin.visibility = View.GONE }, 2300)
        logoshs.startAnimation(animation)

        Handler().postDelayed({ logoshs.visibility = View.GONE }, 2500)







        siiiiiggnout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            Toast.makeText(applicationContext,R.string.signing_out,Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()
        }

    }


    override fun onBackPressed() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification For Exit")
        builder.setMessage("Are you sure you want to Exit Comic Republic?")
        builder.setPositiveButton("Yes",{ dialogInterface: DialogInterface, i: Int ->
            finish()

        })
        builder.setNegativeButton("No, Stay! \uD83D\uDE0A",{ dialogInterface: DialogInterface, i: Int -> })
        builder.show()




    }


}
