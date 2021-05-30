package io.bibuti.picker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.bibuti.pickerlibrary.ConstantsHolder.TAG
import io.bibuti.pickerlibrary.Picker
import io.bibuti.pickerlibrary.PickerOption
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private val picker by lazy {
        Picker(
            activity = this@MainActivity, pickerOptions = listOf(
                PickerOption.CameraImage,
                PickerOption.GalleryImage,
                PickerOption.Documents,
                PickerOption.OtherFiles
            )
        )
    }

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
        picker.onActivityResult(
            requestCode,
            resultCode,
            data
        ) { generatedFile, mimeType, isProcessing ->
            findViewById<TextView>(R.id.tv)?.apply {
                generatedFile?.let { file ->
                    text = file.name
                }
            }
        }
    }
}