package io.bibuti.picker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import io.bibuti.pickerlibrary.Picker

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv)?.apply {
            setOnClickListener {
                Picker(this@MainActivity).show()
            }
        }
    }
}