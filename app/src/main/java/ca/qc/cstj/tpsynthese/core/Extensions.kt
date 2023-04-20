package ca.qc.cstj.tenretni.core

import android.content.Context
import android.widget.ImageView
import com.google.android.material.textfield.TextInputLayout

var TextInputLayout.text
    get() = editText!!.text.toString()
    set(value) = editText!!.setText(value)


fun ImageView.loadFromResource(context: Context, imageName:String) {
    val imageId = resources.getIdentifier(imageName, "drawable", context.packageName)
    this.setImageResource(imageId)
}

fun Context.getStringWithIdentifier(identifier:String) : String {
    val res = this.resources
    return res.getString(res.getIdentifier(identifier.lowercase(), "string", packageName))
}