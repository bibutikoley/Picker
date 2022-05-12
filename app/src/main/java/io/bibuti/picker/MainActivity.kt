package io.bibuti.picker

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import io.bibuti.pickerlibrary.Picker
import io.bibuti.pickerlibrary.PickerOption

class MainActivity : AppCompatActivity() {

    private val picker by lazy {
        Picker(
            context = this@MainActivity,
            pickerOptions = listOf(
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
                picker.show(result)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result?.data?.let {
                picker.onResult(it) { generatedFile, mimeType, isProcessing ->
                    findViewById<TextView>(R.id.tv)?.apply {
                        generatedFile?.let { file ->
                            text = file.name +
                                    "\nType - $mimeType\n" +
                                    "absolutePath - ${file.absolutePath}"
                        }
                    }

                }
            }
        }
}