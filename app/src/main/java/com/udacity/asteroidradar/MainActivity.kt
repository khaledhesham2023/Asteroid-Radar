package com.udacity.asteroidradar

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var appMusic:MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // initializing the music file
        appMusic = MediaPlayer.create(this,R.raw.space)

    }

    override fun onResume() {
        super.onResume()
        // start the music
        appMusic.start()
        appMusic.setOnCompletionListener {
            it.start()
        }
    }

    override fun onPause() {
        super.onPause()
        // pause the music
        appMusic.pause()
    }

    override fun onStop() {
        super.onStop()
        // stop the music
        appMusic.stop()
        appMusic = MediaPlayer.create(this,R.raw.space)
    }
}
