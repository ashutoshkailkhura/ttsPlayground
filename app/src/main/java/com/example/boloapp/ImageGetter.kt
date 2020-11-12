package com.example.boloapp

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.text.Html
import android.util.Log
import android.widget.TextView
import java.io.InputStream
import java.net.URL

class ImageGetter(
    val reso: Resources,
    val htmlTextView: TextView,
    val ScreenW: Int,
    val ScreenH: Int,
    val context: Context
) : Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable? {
        var drawable: Drawable? = null
        val fiu = FetchImageUrl(context, source!!)
        drawable = try {
            fiu.execute().get()
            fiu.GetImage()
        } catch (e: Exception) {
            Log.e("TAG", "Exception Exception!")
            reso.getDrawable(R.drawable.ic_launcher_foreground)
        }
        // to display image,center of screen
        val imgH = drawable?.intrinsicHeight
        val imgW = drawable?.intrinsicWidth
        val padding = 20
//        val realWidth: Int = ScreenW - 2 * padding
//        val realHeight = (imgH?.times(realWidth) ?: 15) / imgW
        drawable?.setBounds(padding, 0, 500, 500)
        return drawable
    }

//    /**
//     * Function needs to overridden when extending [Html.ImageGetter] ,which will download the image
//     */
//    override fun getDrawable(url: String): Drawable {
//        Log.e("TAG", "getDrawable $url")
//        val holder = BitmapDrawablePlaceHolder(reso, null)
//
//        //Coroutine Scope to download image in Background
//        GlobalScope.launch(Dispatchers.IO) {
//            runCatching {
//
//                /**
//                 * downloading image in bitmap format using [Picasso] Library
//                 */
//                val bitmap = Picasso.get().load(url).get()
//                val drawable = BitmapDrawable(reso, bitmap)
//
//                //To make sure Images don't go out of screen , Setting width less than screen width, You can change image size if you want
//                val width = getScreenWidth() - 150
//
//                //Images may stretch out if you will only resize width,hence resize height to according to aspect ratio
//                val aspectRatio: Float =
//                    (drawable.intrinsicWidth.toFloat()) / (drawable.intrinsicHeight.toFloat())
//                val height = width / aspectRatio
//
//                drawable.setBounds(10, 20, width, height.toInt())
//
//                holder.setDrawable(drawable)
//                holder.setBounds(10, 20, width, height.toInt())
//                withContext(Dispatchers.Main) {
//                    htmlTextView.text = htmlTextView.text
//                }
//            }
//        }
//        return holder
//    }
//
//    //    Actually Putting images
//    internal class BitmapDrawablePlaceHolder(res: Resources, bitmap: Bitmap?) :
//        BitmapDrawable(res, bitmap) {
//        private var drawable: Drawable? = null
//
//        override fun draw(canvas: Canvas) {
//            drawable?.run { draw(canvas) }
//        }
//
//        fun setDrawable(drawable: Drawable) {
//            this.drawable = drawable
//        }
//    }
//
//    //Function to get screenWidth used above
//    fun getScreenWidth() =
//        Resources.getSystem().displayMetrics.widthPixels
}
class FetchImageUrl(var context: Context, var imageUrl: String) :
    AsyncTask<String?, String?, Boolean>() {
    protected var image: Drawable? = null
    fun GetImage(): Drawable? {
        return image
    }

    protected override fun doInBackground(vararg p0: String?): Boolean? {
        try {
            val input_stream = URL(imageUrl).content as InputStream
            image = Drawable.createFromStream(input_stream, "src name")
            return true
        } catch (e: java.lang.Exception) {
            image = null
        }
        return false
    }

    override fun onPostExecute(result: Boolean) {

    }
}