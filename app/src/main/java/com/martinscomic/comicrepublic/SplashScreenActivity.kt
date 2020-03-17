package com.martinscomic.comicrepublic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splashscreen.*


class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)





        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        val background = object :Thread() {
            override fun run() {

                try {
                    Thread.sleep(2700)


                    val intent = Intent(baseContext, SignInActivity::class.java)
                    startActivity(intent)
                }catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        background.start()


        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        splashscrerenid.startAnimation(animation)
        Handler().postDelayed({ splashscrerenid.visibility = View.GONE }, 2700)

        signin_link_btnuu.startAnimation(animation)
        signin_link_btndjhbh.startAnimation(animation)

        Handler().postDelayed({ signin_link_btnuu.visibility = View.GONE }, 2600)

        Handler().postDelayed({ signin_link_btndjhbh.visibility = View.GONE }, 2400)

    }

}
