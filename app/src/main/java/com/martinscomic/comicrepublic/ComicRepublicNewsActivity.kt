package com.martinscomic.comicrepublic

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.franklin.trende.adapters.RecyclerViewAdapter
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.martinscomic.comicrepublic.home.StatusHomeActivity
import com.martinscomic.comicrepublic.models.Trende
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException
import org.jsoup.Jsoup

class ComicRepublicNewsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val TAG = "ComicRepublicNewsActivity"

    val lstTrende = ArrayList<Trende>()
    var myadapter = RecyclerViewAdapter(this, lstTrende)

    var token = ""
    private var itShouldLoadMore = true


//    lateinit var drawer_layout: DrawerLayout

    lateinit var requestQueue: RequestQueue


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_republic_news)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_vector_hamburger)

        var navigation = findViewById<NavigationView>(R.id.navigation)
        navigation.setNavigationItemSelectedListener(this)

        var drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        var toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close)

        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()

        requestQueue = Volley.newRequestQueue(this)

        var linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerviewid.adapter = myadapter
        recyclerviewid.layoutManager = linearLayout
        recyclerviewid.setHasFixedSize(true)

        pgb.visibility = View.VISIBLE

        getData()

        recyclerviewid.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(applicationContext, "Loading...", Toast.LENGTH_LONG).show()
                    if (itShouldLoadMore) {
                        pgb_down.visibility = View.VISIBLE
                        getData()
                    }
                }
            }
        })

    }

    fun getData() {

        var url = "https://www.googleapis.com/blogger/v3/blogs/2224675911075973808/posts?key=AIzaSyBoDo8LjUtPKysCVVEzYuVsQ_OOcihUN9E"
        if (token !== "") {

            url = "$url&pageToken=$token"
        }

        if (token == null) {
            Toast.makeText(this, "nothing", Toast.LENGTH_SHORT).show()
            return
        }


        itShouldLoadMore = false
        val request = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->


            pgb.visibility = View.GONE
            // remember here we are in the main thread, that means,
            //volley has finished processing request, and we have our response.
            // What else are you waiting for? update itShouldLoadMore = true;
            itShouldLoadMore = true

            if (response.length() <= 0) {
                // no data available
                Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()

            }
            try {

                pgb_down.visibility = View.GONE
                val jsonArray = response.getJSONArray("items")


                for (i in 0 until jsonArray.length()) {

                    val items = jsonArray.getJSONObject(i)

                    val title = items.getString("title")

                    val content = items.getString("content")

                    val published = items.getString("published")

                    val displayName = items.getJSONObject("author").getString("displayName")

                    val trende_url = items.getString("url")

//                    var document_content = Jsoup.parse(content).body().text()


                    var document = Jsoup.parse(content)

                    val elementsImg = document.select("img")
                    val final_img_url = elementsImg.attr("src")

                    val trende = Trende(title, final_img_url, content, published, displayName,trende_url)

                    lstTrende.add(trende)
                    RecyclerViewAdapter(this, lstTrende).notifyDataSetChanged()


                }

            } catch (e: JSONException) {

                pgb.visibility = View.GONE
                Log.d("er", e.toString())
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }

            token = response.getString("nextPageToken")


            pgb.visibility = View.GONE
            if (response.length() <= 0) {
                // no data available
                Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()

            }


            myadapter.notifyDataSetChanged()


        }, Response.ErrorListener { it ->

            if (it.toString().contains("name")) {
                pgb.visibility = View.GONE
                pgb_down.visibility = View.GONE
                Toast.makeText(this, "Bad Internet Connection, Please try again", Toast.LENGTH_LONG).show()

            }


        })

        requestQueue.add(request)


    }


    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("InlinedApi")
    private fun rateApp(){


        val uri:Uri = Uri.parse("market://details?id=$packageName")

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK

        try {
            startActivity(intent)

        }catch (e: ActivityNotFoundException){
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        when (item.itemId) {
            R.id.nav_home111 -> {
                val home = Intent(this, CRWeatherActivity::class.java)
                startActivity(home)
            }



            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@ComicRepublicNewsActivity, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

            R.id.nav_rates -> {
                rateApp()
            }

            R.id.comic_republic_home -> {
                val intent = Intent(this, SnapsActivity::class.java)
                startActivity(intent)
            }
            R.id.statussaver -> {
                val intent = Intent(this, StatusHomeActivity::class.java)
                startActivity(intent)
            }

            R.id.cr_news_app -> {
                val intent = Intent(this, ComicRepublicNewsActivity::class.java)
                startActivity(intent)
            }



            R.id.nav_share -> {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT,"Download Comic Republic App link Below https://play.google.com/store/apps/details?id=com.niggatechdeveloper.comicrepublic")
                    type = "text/plain"
                }
                startActivity(sendIntent)
            }
//
            R.id.nav_contact_blog -> {
                val blogger_num = Constants.BLOOGER_NUM


                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$blogger_num"))
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 10)
                } else {
                    startActivity(intent)
                }


            }

            R.id.nav_contact_dev -> {
                val blogger_num = "+2349083037286"

                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$blogger_num"))
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 10)
                } else {
                    startActivity(intent)
                }


            }
//            R.id.nav_settings -> {
//                Toast.makeText(this, "Settings", Toast.LENGTH_LONG).show()
//            }
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@ComicRepublicNewsActivity, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.menu_exit -> {
                val intent = Intent(this@ComicRepublicNewsActivity, ComicRepublic2Activity::class.java)
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



}
