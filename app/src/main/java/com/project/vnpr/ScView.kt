package com.project.vnpr

import android.content.Context
import android.graphics.*
import android.view.View
import com.project.vnpr.Activity.MainActivity
import com.project.vnpr.Fragment.Scanner

class ScView(context: Context?) : View(context) {
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        var  paint=Paint()
        paint.color=if(Scanner.detected)
            Color.GREEN
        else
            Color.RED

        paint.style=Paint.Style.STROKE
        paint.strokeWidth=3.0f
        canvas?.drawRect(Rect(40,40,
            MainActivity.size.x-20,
            MainActivity.size.y-180-10),paint)
    }
}