package com.markrap

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.android.volley.RequestQueue
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.markrap.placepicker.VolleySingleton

import fontspackageForTextView.DefineYourAppFont

class MyApp : Application() {

    companion object {
        lateinit var volleyQueueInstance: VolleySingleton

        lateinit var mInstance :MyApp

    }


    private var mRequestQueue: RequestQueue? = null
    private val mImageLoader: ImageLoader? = null

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        instantiateVolleyQueue()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        DefineYourAppFont.fontNameRegular = "fonts/Montserrat-Regular.ttf"
        DefineYourAppFont.fontNameBold = "fonts/Montserrat-Bold.ttf"
        DefineYourAppFont.fontNameBoldExtra = "fonts/Montserrat-ExtraBold.ttf"
        DefineYourAppFont.fontNameItalic = "fonts/Montserrat-Italic.ttf"
        DefineYourAppFont.fontNameBoldItalic = "fonts/Montserrat-BoldItalic.ttf"
        DefineYourAppFont.fontNameLiteItalic = "fonts/vivaldi.ttf"
        DefineYourAppFont.fontNameBoldMedium = "fonts/Montserrat-SemiBold.ttf"
    }


    @Synchronized
    fun getInstance():MyApp? {
        return mInstance
    }



    fun instantiateVolleyQueue() {
        volleyQueueInstance = VolleySingleton.getInstance(getApplicationContext())
    }

    fun getRequestQueue(): RequestQueue? {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(
                getApplicationContext(),
                HurlStack()
            )
        }
        return mRequestQueue
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}