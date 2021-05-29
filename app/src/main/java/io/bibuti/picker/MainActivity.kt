package io.bibuti.picker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import io.bibuti.pickerlibrary.Picker

class MainActivity : AppCompatActivity() {

    private val picker by lazy { Picker(this@MainActivity) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv)?.apply {
            setOnClickListener {
                picker.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        picker.onActivityResult(requestCode, resultCode, data) { generatedFile, mimeType, isProcessing ->
            findViewById<TextView>(R.id.tv)?.apply {
                text = generatedFile?.name
            }
        }
    }
}