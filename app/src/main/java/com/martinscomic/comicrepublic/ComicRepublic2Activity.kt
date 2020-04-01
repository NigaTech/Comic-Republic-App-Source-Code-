package com.martinscomic.comicrepublic

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.martinscomic.comicrepublic.Fragements.HomeFragment
import com.martinscomic.comicrepublic.Fragements.NotificationsFragment
import com.martinscomic.comicrepublic.Fragements.ProfileFragment
import kotlinx.android.synthetic.main.activity_snaps.*

class ComicRepublic2Activity : AppCompatActivity(),ConnectionReceiver.ConnectionRecieverListener  {
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && !pageLoadingFinished)web_view.loadUrl(urlHistory[urlHistory.size - 1])

    }
    //To store Visited Urls
    var urlHistory = arrayListOf<String>()
    //To haandle mobile/wifi swap
    var pageLoadingFinished = true
    lateinit var homeFragment: HomeFragment
    lateinit var profileFragment: ProfileFragment
    lateinit var notificationsFragment: NotificationsFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comicrepublic2)



        baseContext.registerReceiver(ConnectionReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        MyApplications.instance.setConnectionListener(this)

        val url = "https://m.thecomicrepublic.com"
        web_view.loadUrl("https://m.thecomicrepublic.com")
        val myWebView: WebView = findViewById(R.id.web_view)
        myWebView.settings.javaScriptEnabled = true
        myWebView.loadUrl(getString(R.string.default_web_client_id))
        myWebView.webViewClient = WebViewClient()
        WebView.setWebContentsDebuggingEnabled(false)

        urlHistory.add(url)

        web_view.webViewClient = object : WebViewClient() {
            //Under API 21
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                urlHistory.add(url!!)
                if(ConnectionReceiver.isConnected) {

                    if (url!!.startsWith("https://m.thecomicrepublic.com")) {
                        myWebView.settings.javaScriptEnabled = true
                        myWebView.loadUrl(getString(R.string.default_web_client_id))
                        myWebView.webViewClient = WebViewClient()
                        WebView.setWebContentsDebuggingEnabled(false)
                        web_view.loadUrl(url)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        view?.context?.startActivity(intent)
                    }

                }else{
                    web_view.visibility = View.INVISIBLE


                    Toast.makeText(view?.context,"NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show()

                }
                pageLoadingFinished = false
                return super.shouldOverrideUrlLoading(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                view?.visibility = View.INVISIBLE

                pageLoadingFinished = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (ConnectionReceiver.isConnected) {
                    view?.visibility = View.VISIBLE

                    pageLoadingFinished = true

                }
            }
        }

        web_view.loadUrl(url)

        urlHistory.add(url)
        if (ConnectionReceiver.isConnected) {



        } else {
            web_view.visibility = View.INVISIBLE



        }


        val bottomNavigation : BottomNavigationView = findViewById(R.id.btm_nav)

        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){

                R.id.nav_home -> {
                    homeFragment = HomeFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, homeFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
                R.id.nav_profile -> {
                    profileFragment = ProfileFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_layout, profileFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }


            }
            true
        }
    }
    override fun onBackPressed() {
        if(ConnectionReceiver.isConnected) {

            if (urlHistory.size > 1) {
                urlHistory.removeAt(urlHistory.size - 1)
                web_view.loadUrl(urlHistory[urlHistory.size - 1])
            } else {
                super.onBackPressed()
                finish()
            }
        }else{
            if (urlHistory.size > 1){
                urlHistory.removeAt(urlHistory.size - 1)
            }
            web_view.visibility = View.INVISIBLE


            Toast.makeText(baseContext,"NO INTERNET CONNECTION!!", Toast.LENGTH_SHORT).show()
            pageLoadingFinished = false
        }

    }
}
