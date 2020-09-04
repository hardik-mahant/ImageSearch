package com.hardikmahant.imagesearch.util

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast

fun Context.showToast(message: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, length).show()
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.GONE
}

fun View.makeInvisible(){
    this.visibility = View.INVISIBLE
}

fun logD(message: String, tag: String = Constants.TAG_APP) {
    Log.d(tag, message)
}

fun logI(message: String, tag: String = Constants.TAG_APP) {
    Log.i(tag, message)
}

fun logE(message: String, tag: String = Constants.TAG_APP) {
    Log.e(tag, message)
}