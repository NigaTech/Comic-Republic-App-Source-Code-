package com.niggatechdeveloper.comicrepublic

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.niggatechdeveloper.comicrepublic.Fragements.HomeFragment
import com.niggatechdeveloper.comicrepublic.Fragements.MyAccountFragment
import com.niggatechdeveloper.comicrepublic.Fragements.PeopleFragment
import com.niggatechdeveloper.comicrepublic.Fragements.ProfileFragment
import kotlinx.android.synthetic.main.activity_crchat_app.*

class LatestMessagesActivity : AppCompatActivity() {
    private val TAG = "LatestMessagesActivity"
    lateinit var peopleFragment: PeopleFragment
    lateinit var myaccountFragment: MyAccountFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comic_rebublic_chat)

        val bottomNavigation : BottomNavigationView = findViewById(R.id.btm_nav_crchat)

        replaceFragment(PeopleFragment())


        bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId){

                R.id.navigation_people -> {
                    replaceFragment(PeopleFragment())
                    true


                }
                R.id.navigation_my_account -> {
                    replaceFragment(MyAccountFragment())
                    true

                }
                else -> false



            }


        }




    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@LatestMessagesActivity, ComicRepublic2Activity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@LatestMessagesActivity, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.menu_exit -> {
                val intent = Intent(this@LatestMessagesActivity, ComicRepublic2Activity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.comic_republic_home -> {
                val intent = Intent(this, SnapsActivity::class.java)
                startActivity(intent)
            }
            R.id.cr_chat_app -> {
                val intent = Intent(this, SnapsActivity::class.java)
                startActivity(intent)
            }
            R.id.cr_news_app -> {
                val intent = Intent(this, ComicRepublicNewsActivity::class.java)
                startActivity(intent)
            }



        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)






    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_layout, fragment)
            .commit()


    }


}
