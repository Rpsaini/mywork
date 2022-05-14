package com.markrap

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base2)
    }


    fun showErrorDialog(msg:String) {
        val dialog: Dialog = dialog(R.layout.showerror_dialog, false)
        val txtError = dialog.findViewById<TextView>(R.id.txtError)
        txtError.text = msg


        dialog.findViewById<View>(R.id.txtNext).setOnClickListener {
            dialog.dismiss()

        }
    }


    fun dialog(layout:Int,isHide:Boolean): Dialog
    {
        var commonDialog =  Dialog(this);
        commonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        commonDialog = Dialog(this)
        commonDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        commonDialog.setContentView(layout)
        val lp = WindowManager.LayoutParams()
        val window = commonDialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = lp
        commonDialog.setCancelable(isHide)
        commonDialog.window!!.setBackgroundDrawableResource(R.color.transuleant_black)
        commonDialog.window!!.setDimAmount(0f)
        commonDialog.show()

        return commonDialog;

    }
}