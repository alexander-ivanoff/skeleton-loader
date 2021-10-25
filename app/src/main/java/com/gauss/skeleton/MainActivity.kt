package com.gauss.skeleton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val content : View = findViewById(R.id.container)

        findViewById<View>(R.id.button_object).apply {
            setOnClickListener {
                content.configureObjectAnimatorsSkeletonLoading {
                    colorStart = 0xFFBABABA.toInt()
                    colorEnd = 0xFFE8E8E8.toInt()
                    cornerRadius = 16
                    skeletonViewMarkerTag = resources.getString(R.string.skeleton_loading_view)
                }
                content.showSkeletonLoading()
                content.postDelayed(
                    {
                        content.hideSkeletonLoading()
                    } ,
                    3000
                )
            }
        }

        findViewById<View>(R.id.button_single).apply {
            setOnClickListener {
                content.configureSingleAnimatorSkeletonLoading {
                    colorStart = 0xFFBABABA.toInt()
                    colorEnd = 0xFFE8E8E8.toInt()
                    cornerRadius = 16
                    skeletonViewMarkerTag = resources.getString(R.string.skeleton_loading_view)
                }
                content.showSkeletonLoading()
                content.postDelayed(
                    {
                        content.hideSkeletonLoading()
                    } ,
                    3000
                )
            }
        }

    }


    override fun onDestroy() {
        findViewById<View>(R.id.container).resetSkeletonLoading()
        super.onDestroy()
    }
}
