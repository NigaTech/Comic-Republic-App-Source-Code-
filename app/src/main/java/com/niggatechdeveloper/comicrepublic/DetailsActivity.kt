package com.niggatechdeveloper.comicrepublic

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.MenuItem
import android.webkit.WebView

import android.webkit.WebViewClient
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*




class DetailsActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (intent.getStringExtra("trende_img").isEmpty()) {
            blog_img.setImageResource(R.drawable.comicmainbglogo)
        } else {
            Picasso.get().load(intent.getStringExtra("trende_img")).into(blog_img)

        }

        val trende_title = intent.getStringExtra("trende_title")

        blog_title.text = trende_title

        blog_content.text = Html.fromHtml(intent.getStringExtra("trende_content"))
//        blog_content.settings.javaScriptEnabled = true
//        blog_content.loadUrl(intent.getStringExtra("trende_url"))
//        blog_content.webViewClient = myWebClient()

//        blog_content.loadDataWithBaseURL("", intent.getStringExtra("trende_content"), "text/html", "UTF-8", "")

//        var adRequest =  AdRequest.Builder().build()
//        adView.loadAd(adRequest)


        blog_author.text = intent.getStringExtra("trende_author")

        blog_published_time.text = intent.getStringExtra("trende_published")


//        val webView = findViewById<WebView>(R.id.blog_content);
//        webView.loadDataWithBaseURL(null, blog_con, "text/html", "utf-8", null);


        val collapse_toobar_photograph = findViewById<CollapsingToolbarLayout>(R.id.collapse_toolbar_photograph)
        collapse_toobar_photograph.title = trende_title
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {

            when (item.itemId) {
                android.R.id.home -> finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    inner class myWebClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            // TODO Auto-generated method stub

            view.loadUrl(url)
            return true

        }

    }

}
