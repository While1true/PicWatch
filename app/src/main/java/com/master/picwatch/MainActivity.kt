package com.master.picwatch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.master.picwatchlib.PicFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var urls = arrayListOf<String>()
        for (i in 0..8) {
            urls.add("https://wx1.sinaimg.cn/thumb150/608185adgy1fjzihrmx0zj20qo0zme81.jpg")
        }
        val view:View=findViewById(android.R.id.content)
        view.transitionName="pic"
        PicFragment.Go(this,urls,2,view)
    }
}
