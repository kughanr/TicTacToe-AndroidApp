package kughanr.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onePlayerButton.setOnClickListener {
            finish()
            startActivity(Intent(this, OnePlayerActivity::class.java))
        }

        twoPlayerButton.setOnClickListener {
            Log.d(tag, "starting two player mode")
            finish()
            startActivity(Intent(this, TwoPlayerActivity::class.java))
        }
    }
}
