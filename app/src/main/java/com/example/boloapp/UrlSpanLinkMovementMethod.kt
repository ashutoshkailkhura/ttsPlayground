package com.example.boloapp

import android.text.Layout
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.MotionEvent
import android.widget.TextView

abstract class UrlSpanLinkMovementMethod : LinkMovementMethod() {

    //handle URLSpan click event
    override fun onTouchEvent(widget: TextView, buffer: Spannable?, event: MotionEvent): Boolean {
        val action = event.action
        if (buffer != null && (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN)) {
            var x = event.x.toInt()
            var y = event.y.toInt()
            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop
            x += widget.scrollX
            y += widget.scrollY
            val layout = widget.layout
            if (layout != null) {
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())
                val links = buffer.getSpans(off, off, ClickableSpan::class.java) as Array<ClickableSpan>?
                if (links != null && links.isNotEmpty()) {
                    val link = links[0]
                    if (action == MotionEvent.ACTION_UP) {
                        if (link is URLSpan) {
                            val url = link.url
                            onLinkClicked(url)
                            return true
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }

    abstract fun onLinkClicked(url: String?)
}