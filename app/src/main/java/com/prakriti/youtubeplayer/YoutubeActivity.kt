package com.prakriti.youtubeplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

// Youtube Player API jar file copied into libs folder
// to use youtube player api ->
class YoutubeActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val TAG = "YoutubeActivity"
    private val DIALOG_REQ_CODE = 1

    // put above class defn
    private val YOUTUBE_VIDEO_ID = "DnQnteSF35Q"
    private val YOUTUBE_PLAYLIST = "PLonJJ3BVjZW6hYgvtkaWvwAVvOFB7fkLa"

    val playerView by lazy { YouTubePlayerView(this) } // instantiate after onCreate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube)
        //val layout = findViewById<ConstraintLayout>(R.id.layout_youtube)

        // above 2 lines can be written as
        val layout = layoutInflater.inflate(R.layout.activity_youtube, null) as ConstraintLayout // cast & assign view to val
            // here we are passing the root view itself, so its root is null^
//        setContentView(layout) // pass layout to setContentView()

        // create widget in code
//        val button1 = Button(this)
//        button1.layoutParams = ConstraintLayout.LayoutParams(600, 180) // in dp
//        button1.text = "Button added"
//        layout.addView(button1)

//        val playerView = YouTubePlayerView(this) // global
        playerView.layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layout.addView(playerView)

        // init player
        playerView.initialize(getString(R.string.GOOGLE_API_KEY), this) // (string, listener)

    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        // TO-DO (Not yet implemented) is a function call that throws an exception if called
        Log.i(TAG, "onInitializationSuccess: provider is ${provider?.javaClass}") // use .javaClass to find what kind of object it is
        Log.i(TAG, "onInitializationSuccess: player is ${player?.javaClass}")
        Toast.makeText(this, "YouTube Player initialized successfully", Toast.LENGTH_SHORT).show()

        // set listeners
        player?.setPlayerStateChangeListener(playerStateChangeListener)
        player?.setPlaybackEventListener(playbackEventListener)

        if(!wasRestored) { // if not resuming an existing playback
//            player?.cueVideo(YOUTUBE_VIDEO_ID) // to cue it up (wont play yet), pass ID from url
            // video can start playing only after its loaded -> so to load instead of cue
            player?.loadVideo(YOUTUBE_VIDEO_ID)
        } else { // player will be restored after orientation changes
            player?.play()
        } // log^
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, initResult: YouTubeInitializationResult?) {
//        val REQUEST_CODE = 0
        if(initResult?.isUserRecoverableError == true) { // here we do == true, bcoz its of Nullable Bool type
            // above line also includes null check, so safe call is not required inside this block anymore
            initResult.getErrorDialog(this, DIALOG_REQ_CODE).show()
        } else {
            val errorMessage = "Error initializing YouTube Player: $initResult"
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    
    private val playbackEventListener = object : YouTubePlayer.PlaybackEventListener {
        // youtube player api tracks the playback of the video, so we can process the events that happen
        override fun onPlaying() {
            Toast.makeText(this@YoutubeActivity, "Video playing", Toast.LENGTH_SHORT).show()
        }

        override fun onPaused() {
            Toast.makeText(this@YoutubeActivity, "Video paused", Toast.LENGTH_SHORT).show()
        }

        override fun onStopped() {
            Toast.makeText(this@YoutubeActivity, "Video stopped", Toast.LENGTH_SHORT).show()
        }

        override fun onBuffering(p0: Boolean) {}

        override fun onSeekTo(p0: Int) {}
    }


    private val playerStateChangeListener = object : YouTubePlayer.PlayerStateChangeListener {
        override fun onLoading() {}

        override fun onLoaded(p0: String?) {}

        override fun onAdStarted() {
            Toast.makeText(this@YoutubeActivity, "Ad started, support the creator by watching the ad!", Toast.LENGTH_SHORT).show()
        }

        override fun onVideoStarted() {            
            Toast.makeText(this@YoutubeActivity, "Video has started", Toast.LENGTH_SHORT).show()
        }

        override fun onVideoEnded() {
            Toast.makeText(this@YoutubeActivity, "Video has ended", Toast.LENGTH_SHORT).show()
        }

        override fun onError(p0: YouTubePlayer.ErrorReason?) {}
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // RESULT_OK is -1, CANCELLED is 0
        Log.d(TAG, "onActivityResult called with request code: $requestCode, result code: $resultCode")
        if (requestCode == DIALOG_REQ_CODE) { // REQ CODE is specified by us
            // use when calling getErrorDialog()
            Log.d(TAG, "onActivityResult: " + intent?.toString())
            Log.d(TAG, "onActivityResult: " + intent?.extras.toString())
            // usually if result is ok, we init the player and play it
            // but since getErrorDialog() returns 0 code each time, regardless of success/failure, lets init player anyway
            playerView.initialize(getString(R.string.GOOGLE_API_KEY), this) // if error is not resolved, player doesn't init....
                // uncertain error^.. if resolved successfully, it inits player but returns 0 result code
        }
    }

}