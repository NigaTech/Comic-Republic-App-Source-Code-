package com.niggatechdeveloper.comicrepublic

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore.Video.Thumbnails.VIDEO_ID
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.webkit.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.youtube.player.YouTubeStandalonePlayer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_snaps.*
import org.jetbrains.anko.toast
import android.Manifest
import android.os.Build
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebViewClient
import android.os.Bundle
import android.Manifest.permission
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.webkit.URLUtil.guessFileName
import android.webkit.*
import android.webkit.URLUtil.guessFileName
import android.webkit.DownloadListener
import android.content.BroadcastReceiver
import androidx.annotation.RequiresApi









class SnapsActivity : AppCompatActivity(),ConnectionReceiver.ConnectionRecieverListener {
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && !pageLoadingFinished)web_view.loadUrl(urlHistory[urlHistory.size - 1])
    }
    //To store Visited Urls
    var urlHistory = arrayListOf<String>()
    //To haandle mobile/wifi swap
    var pageLoadingFinished = true
    val TAG = "FCM Service"
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    lateinit var signOutBtn: Button
    lateinit var mGoogleSignInClient: GoogleSignInClient
    var mAuth = FirebaseAuth.getInstance()
    var myWebView :WebView?=null
    var progressBar :ProgressBar?=null
    lateinit var context: Context
    lateinit var activity: Activity
    lateinit var downloadListener: DownloadListener
    var writeAccess = false
    /** Permission Request Code */
    private val PERMISSION_REQUEST_CODE = 1234
    private val PermissionsRequestCode = 123
    var counter = 5



    lateinit var web_view: WebView

    





    /** Permission Request Code */



    /** Sample Page from which we will download the file */
    private val downloadPage = "https://m.thecomicrepublic.com"


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
        baseContext.registerReceiver(ConnectionReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        MyApplications.instance.setConnectionListener(this)




        /** Application Context and Main Activity */
        context = applicationContext
        activity = this

        /** Initialize main layout and web view */
        web_view = findViewById(R.id.web_view)

        /** Check permission to write in external storage */
        checkWriteAccess()
        /** Create a Download Listener */
        createDownloadListener()
        /** Display Toast Message When Download Complete */
        onDownloadComplete()
        /** Configure Web View */
        configureWebView()



        swiperefresh.setOnRefreshListener {

            myWebView?.loadUrl( "javascript:window.location.reload( true )" )

            Toast.makeText(applicationContext,R.string.refreshcr,Toast.LENGTH_LONG).show()

            Handler(Looper.getMainLooper()).postDelayed({
                counter = 5

                swiperefresh.isRefreshing = false
            },1000)


        }

        web_view.setDownloadListener { url, userAgent, contentDisposition, mimeType,  contentLength ->


            val request = DownloadManager.Request(Uri.parse(url))
            request.setMimeType(mimeType)
            request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
            request.addRequestHeader("User-Agent", userAgent)
            request.setDescription("Downloading file...")
            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalFilesDir(this@SnapsActivity, Environment.DIRECTORY_DOWNLOADS, ".png")
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(applicationContext, "Downloading  file.... \uD83D\uDE0A", Toast.LENGTH_LONG).show()


            //checking Runtime permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    //Do this, if permission granted
                    downloadDialog(url, userAgent, contentDisposition, mimeType)

                } else {

                    //Do this, if there is no permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )

                }
            } else {
                //Code for devices below API 23 or Marshmallow
                downloadDialog(url, userAgent, contentDisposition, mimeType)

            }
        }
        myWebView = findViewById(R.id.web_view)
        progressBar?.visibility = View.VISIBLE
        myWebView?.webViewClient = Browser_home()
        myWebView?.webChromeClient = MyChrome()
        val webSettings = myWebView?.settings


        val url = "https://m.thecomicrepublic.com"
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



                        myWebView.getSettings().setSupportZoom(true)
                        myWebView.getSettings().setBuiltInZoomControls(true)
                        myWebView.getSettings().setDisplayZoomControls(false)

                        myWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY)
                        myWebView.setScrollbarFadingEnabled(false)

                    } else {
                        val intent = Intent(ACTION_VIEW, Uri.parse(url))
                        view?.context?.startActivity(intent)
                    }


                }else{
                    web_view.visibility = View.INVISIBLE
                    progress_bar.visibility = View.VISIBLE

                    Toast.makeText(view?.context,"NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show()

                }
                pageLoadingFinished = false
                return super.shouldOverrideUrlLoading(view, url)
            }



            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Page loading started
                // Do something

                view?.visibility = View.INVISIBLE
                progress_bar.visibility = View.VISIBLE
                pageLoadingFinished = false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // Page loading finished
                // Display the loaded page title in a toast message
                if (view != null) {
                    toast(" ${view.title} \uD83D\uDE0A")
                }
                if (ConnectionReceiver.isConnected) {
                    view?.visibility = View.VISIBLE
                    progress_bar.visibility = View.INVISIBLE
                    pageLoadingFinished = true


                }else {

                    val intent = YouTubeStandalonePlayer.createVideoIntent(
                        Activity(),
                        getString(R.string.developer_key),
                        VIDEO_ID
                    )
                    startActivity(intent)
                }
            }
        }

        web_view.loadUrl(url)

        val intent = intent

        val message = intent.getStringExtra("fcm_message")

        if (!message.isNullOrEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Comic Republic Notification!!")
                .setMessage(message)
                .setPositiveButton("OKAY! \uD83D\uDE0A", DialogInterface.OnClickListener { dialogInterface, i -> })
                .show()
        }

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast

            })

        urlHistory.add(url)
        if (ConnectionReceiver.isConnected) {


        } else {
            web_view.visibility = View.INVISIBLE
            progress_bar.visibility = View.VISIBLE


        }


    }


    class MyClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, Url: String): Boolean {
            view.loadUrl(Url)
            return true

        }
    }





    override fun onBackPressed() {
        if(ConnectionReceiver.isConnected) {

            if (urlHistory.size > 1) {
                urlHistory.removeAt(urlHistory.size - 1)
                web_view.loadUrl(urlHistory[urlHistory.size - 1])

            } else {
                super.onBackPressed()
                val intent = Intent(this@SnapsActivity, ComicRepublicSplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }else{
            if (urlHistory.size > 1){
                urlHistory.removeAt(urlHistory.size - 1)
            }
            web_view.visibility = View.INVISIBLE
            progress_bar.visibility = View.VISIBLE


            Toast.makeText(baseContext,"NO INTERNET CONNECTION!!", Toast.LENGTH_SHORT).show()
            pageLoadingFinished = false
        }

    }

    internal inner class Browser_home : WebViewClient() {
        override fun onPageStarted(
            view: WebView,
            url: String,
            favicon: Bitmap?
        ) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView, url: String) {
            title = view.title
            progressBar!!.visibility = View.GONE
            super.onPageFinished(view, url)
        }
    }
    private inner class MyChrome internal constructor() : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        protected var mFullscreenContainer: FrameLayout? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0
        override fun getDefaultVideoPoster(): Bitmap? {
            return if (mCustomView == null) {
                null
            } else BitmapFactory.decodeResource(applicationContext.resources, 2130837573)
        }

        override fun onHideCustomView() {
            (window.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            requestedOrientation = mOriginalOrientation
            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null
        }

        override fun onShowCustomView(
            paramView: View,
            paramCustomViewCallback: CustomViewCallback
        ) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = paramView
            mOriginalSystemUiVisibility = window.decorView.systemUiVisibility
            mOriginalOrientation = requestedOrientation
            mCustomViewCallback = paramCustomViewCallback
            (window.decorView as FrameLayout).addView(
                mCustomView,
                FrameLayout.LayoutParams(-1, -1)
            )
            window.decorView.systemUiVisibility = 3846
        }
    }







    fun downloadDialog(url:String,userAgent:String,contentDisposition:String,mimetype:String)

    {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setMimeType(mimetype)
        request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
        request.addRequestHeader("User-Agent", userAgent)
        
        request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimetype))
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(this@SnapsActivity, Environment.DIRECTORY_DOWNLOADS, ".png")
        val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager


        //getting file name from url
        val filename = URLUtil.guessFileName(url, contentDisposition, mimetype)
        //Alertdialog
        val builder = AlertDialog.Builder(this@SnapsActivity)
        //title for AlertDialog
        builder.setTitle("Downloading... \uD83D\uDE0A")
        //message of AlertDialog
        builder.setMessage(" $filename has started Downloading. \n  If the File has not started downloading, \n Click Download! Below. \n \n Click Ignore if file has started Downloading ")
        //if YES button clicks
        builder.setPositiveButton("Download! \uD83D\uDE0A") { dialog, which ->
            //DownloadManager.Request created with url.
            val request = DownloadManager.Request(Uri.parse(url))
            //cookie
            val cookie = CookieManager.getInstance().getCookie(url)
            //Add cookie and User-Agent to request
            request.addRequestHeader("Cookie",cookie)
            request.addRequestHeader("User-Agent",userAgent)
            //file scanned by MediaScannar
            request.allowScanningByMediaScanner()
            //Download is visible and its progress, after completion too.
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            //DownloadManager created
            val downloadmanager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            //Saving file in Download folder
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,filename)
            //download enqued
            downloadmanager.enqueue(request)
        }
        //If Cancel button clicks
        builder.setNegativeButton("Ignore")
        {dialog, which ->
            //cancel the dialog if Cancel clicks
            dialog.cancel()
        }

        val dialog:AlertDialog = builder.create()
        //alertdialog shows
        dialog.show()

    }





    @SuppressLint("SetJavaScriptEnabled")
    private fun configureWebView()
    {

        /** Web View General Setup */

        /**
        When user clicks on an URL the default behaviour is android open the default application
        which handles URL. It means android will open a default browser. We need to handle this.
        Why? Because we need to open the URL in the same web view. In our MyWebViewClient we
        will override shouldOverrideUrlLoading function to again load the new url in our
        web view.
         */
        web_view.webViewClient = MyWebViewClient()
        /** getSettings() : Gets the WebSettings object used to control the settings for this WebView. */
        /** We will use it to enable the Java Script Support. */
        web_view.settings.javaScriptEnabled = true
        /** loadUrl : Loads the given URL. */
        web_view.loadUrl(downloadPage)

        /** File Download Listener */
        web_view.setDownloadListener(downloadListener)
    }


    /**
     * Custom WebViewClient to override URL Loading.
     */

    private inner class MyWebViewClient : WebViewClient() {

        /**
         * Override to open URL in WebView
         * */
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        /**
         * Override to open URL in WebView
         * */
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }
    }

    private fun onDownloadComplete()
    {
        /**  Code that receives and handles broadcast intents sent by Context.sendBroadcast(Intent) */
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                Toast.makeText(context,"File Downloaded Successfully \uD83D\uDE0A",Toast.LENGTH_LONG).show()
            }
        }

        /** Register to receives above broadcast */
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    private fun createDownloadListener()
    {

        /** A New Download Listener for our WebView */
        downloadListener = DownloadListener { url, userAgent, contentDescription, mimetype, contentLength ->

            /**
             * This class contains all the information necessary to request a new download.
             * The URI is the only required parameter. Note that the default download destination
             * is a shared volume where the system might delete your file if it needs to reclaim
             * space for system use.
             * */
            val request = DownloadManager.Request(Uri.parse(url))

            /**
             * If the file to be downloaded is to be scanned by MediaScanner, this method should
             * be called before DownloadManager.enqueue(Request) is called.
             */
            request.allowScanningByMediaScanner()

            /**
             * Control whether a system notification is posted by the download manager while this
             * download is running or when it is completed.
             * */
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

            /**
             * Guesses canonical filename that a download would have, using the
             * URL and contentDisposition.
             * */
            val fileName = URLUtil.guessFileName(url, contentDescription, mimetype)

            /**
             * Set the local destination for the downloaded file to a path within the public
             * external storage directory (as returned by Environment.getExternalStoragePublicDirectory(String)).
             * */
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

            /**
             * Get Download Manager Service
             * */
            val dManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            /**
             * Enqueue a new request to Download our File.
             * */

            if(writeAccess)
                dManager.enqueue(request)
            else
            {
                Toast.makeText(context,"Unable to download file. Required Privileges are not available.",Toast.LENGTH_LONG).show()
                checkWriteAccess()
            }

        }
    }

    private fun checkWriteAccess()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            /**
             * Check for permission status.
             * */
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                {
                    val builder = AlertDialog.Builder(activity)
                    builder.setMessage("Required permission to write external storage to save downloaded file.")
                    builder.setTitle("Please Grant Write Permission")
                    builder.setPositiveButton("OK") { _, _->
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PERMISSION_REQUEST_CODE
                        )
                    }
                    builder.setNeutralButton("Cancel", null)
                    val dialog = builder.create()
                    dialog.show()
                } else {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_REQUEST_CODE
                    )
                }
            }
            else {
                /**
                 * Already have required permission.
                 * */
                writeAccess = false
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeAccess=true
                } else {
                    // Permission denied
                    writeAccess=false
                    Toast.makeText(context,"Permission Denied. This app will not work with right permission.",Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}




