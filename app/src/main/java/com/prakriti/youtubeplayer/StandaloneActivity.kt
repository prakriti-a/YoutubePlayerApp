package com.prakriti.youtubeplayer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeStandalonePlayer
import kotlinx.android.synthetic.main.activity_standalone.*
import java.lang.IllegalArgumentException

class StandaloneActivity: AppCompatActivity(), View.OnClickListener { // activity is a listener

    private val YOUTUBE_VIDEO_ID = "DnQnteSF35Q"
    private val YOUTUBE_PLAYLIST = "PLonJJ3BVjZW6hYgvtkaWvwAVvOFB7fkLa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_standalone)

        buttonPlayVideo.setOnClickListener(this)
        buttonPlaylist.setOnClickListener(this)
        // can also set via anon inner class, or lambda expression, or assign listener var (which also creates anon inner class)
    }

    override fun onClick(view: View) { // ? was removed here
        val intent =
            when(view.id) {
                // YouTubeStandalonePlayer class creates the intents, and it runs at full screen -> landscape
                    // it also doesn't provide any callbacks
                        // these activities are written so that they're not destroyed while orientation changes

                // here to play the video on click, not just cue, we pass 0 ms - time, true - autoPlay, false - lightBox (non-fullscreen) mode
                R.id.buttonPlayVideo -> YouTubeStandalonePlayer.createVideoIntent(this, getString(R.string.GOOGLE_API_KEY), YOUTUBE_VIDEO_ID, 0, true, false)
                R.id.buttonPlaylist -> YouTubeStandalonePlayer.createPlaylistIntent(this, getString(R.string.GOOGLE_API_KEY), YOUTUBE_PLAYLIST, 0, 0, true, true)
                        // ^ here, pass 0 - start index, then time, autoPlay & lightBox mode
                else -> throw IllegalArgumentException("Undefined button clicked")
            }
        startActivity(intent)
        // intents can be used to start activities, send data to broadcast receivers, start a service, etc to request an action from an app component
    }
}