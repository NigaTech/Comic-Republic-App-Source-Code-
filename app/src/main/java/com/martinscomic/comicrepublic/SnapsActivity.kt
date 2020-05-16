package com.martinscomic.comicrepublic

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
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_snaps.*
import android.Manifest
import android.os.Build
import android.os.Environment
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.webkit.WebViewClient
import android.os.Bundle
import android.webkit.DownloadListener
import android.content.BroadcastReceiver
import android.widget.*
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task


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

    var tv_image: ImageView? = null
    private val googleApiClient: GoogleApiClient? = null
    private val gso: GoogleSignInOptions? = null
    private val RC_SIGN_IN = 1

    lateinit var web_view: WebView

    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)
        baseContext.registerReceiver(ConnectionReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        MyApplications.instance.setConnectionListener(this)



        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
// Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        settinggggsss.setOnClickListener {
            startActivity(Intent(this, ProfileAccountActivity::class.java))

        }


        val mAuth = FirebaseAuth.getInstance().currentUser
        mAuth?.let {
            for (profile in it.providerData) {



                Glide.with(this).load(mAuth?.photoUrl).into(tv_imagessseee)



            }
        }

        tv_imagesss.setOnClickListener {
            startActivity(Intent(this, ProfileAccountActivity::class.java))

        }

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct != null) {
            Glide.with(this).load(acct.photoUrl).into(tv_imagesss)
        }


        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url



            Glide.with(this).load(user.photoUrl).into(tv_imagessseee)


        }



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
                        myWebView.setScrollbarFadingEnabled(true)

                    } else {
                        val intent = Intent(ACTION_VIEW, Uri.parse(url))
                        view?.context?.startActivity(intent)
                    }


                }else{
                    web_view.visibility = View.INVISIBLE
                    progress_barss.visibility = View.VISIBLE



                    Toast.makeText(view?.context,"NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show()

                }
                pageLoadingFinished = false
                return super.shouldOverrideUrlLoading(view, url)
            }



            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

                progress_bar.visibility = View.VISIBLE
                pageLoadingFinished = false
                progress_barss.visibility = View.INVISIBLE

            }


            override fun onPageFinished(view: WebView?, url: String?) {

                if (ConnectionReceiver.isConnected) {
                    view?.visibility = View.VISIBLE
                    progress_bar.visibility = View.INVISIBLE
                    pageLoadingFinished = true


                }
            }
        }


        web_view.loadUrl(url)




        val message = intent.getStringExtra("fcm_message")


        if (!message.isNullOrEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Comic Republic Notification!!")
                .setMessage(message)
                .setIcon(R.drawable.comicmainbg)
                // Set the intent that will fire when the user taps the notification
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


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) = try {
        val account =
            completedTask.getResult(ApiException::class.java)

        Glide.with(this).load(account!!.photoUrl).into(tv_imagesss)


    }
    catch (e: ApiException) {

        Toast.makeText(applicationContext, "image not found", Toast.LENGTH_LONG).show()


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
            progress_barss.visibility = View.VISIBLE



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

            super.onPageFinished(view, url)
        }
    }

    private inner class MyChrome internal constructor() : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        protected var mFullscreenContainer: FrameLayout? = null
        private val mDefaultVideoPoster: Bitmap?  = null
        private val mVideoProgressView: View?  = null

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

        /** getSettings() : Gets the WebSettings object used to control the settings for this WebView. */
        /** We will use it to enable the Java Script Support. */
        web_view.settings.javaScriptEnabled = true
        /** loadUrl : Loads the given URL. */


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




