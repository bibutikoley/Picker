@file:Suppress("DEPRECATION")

package io.bibuti.pickerlibrary

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.bibuti.pickerlibrary.databinding.FragmentPickerSheetBinding
import java.io.File

class Picker(
    private val activity: FragmentActivity,
    private val fragment: Fragment? = null,
    pickerOptions: List<PickerOption> = listOf()
) {

    private var bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(
        activity,
        R.style.AppBottomSheetDialogTheme
    )

    @SuppressLint("InflateParams")
    private var binding: FragmentPickerSheetBinding = FragmentPickerSheetBinding.bind(
        LayoutInflater
            .from(activity)
            .inflate(R.layout.fragment_picker_sheet, null, false)
    )

    private var imageCapturedFromCamera = false

    init {
        activity.clearCache()
        imageCapturedFromCamera = false
        bottomSheetDialog.setContentView(binding.root)
        binding.cameraTV.apply {
            visibility = if (pickerOptions.contains(PickerOption.CameraImage))
                View.VISIBLE
            else
                View.GONE
            setOnClickListener {
                dispatchPicker(mimeType = MimeType.IMAGE, openCamera = true)
            }
        }
        binding.recordVideoTV.apply {
            visibility = if (pickerOptions.contains(PickerOption.CameraVideo))
                View.VISIBLE
            else
                View.GONE
            setOnClickListener {
                dispatchPicker(mimeType = MimeType.VIDEO, openCamera = true)
            }
        }
        binding.galleryTV.apply {
            val isVideoPicker = pickerOptions.contains(PickerOption.GalleryVideo)
            setCompoundDrawablesWithIntrinsicBounds(
                if (isVideoPicker) ContextCompat.getDrawable(
                    this.context,
                    R.drawable.ic_picker_gallery_video
                ) else ContextCompat.getDrawable(this.context, R.drawable.ic_picker_gallery_image),
                null,
                null,
                null
            )
            visibility =
                if (
                    pickerOptions.contains(PickerOption.GalleryImage)
                    or
                    pickerOptions.contains(PickerOption.GalleryVideo)
                )
                    View.VISIBLE
                else
                    View.GONE
            setOnClickListener {
                dispatchPicker(
                    mimeType = if (isVideoPicker)
                        MimeType.VIDEO
                    else
                        MimeType.IMAGE
                )
            }
        }
        binding.documentTV.apply {
            visibility = if (pickerOptions.contains(PickerOption.Documents))
                View.VISIBLE
            else
                View.GONE
            setOnClickListener {
                dispatchPicker(mimeType = MimeType.DOCUMENTS)
            }
        }
        binding.otherFilesTV.apply {
            visibility = if (pickerOptions.contains(PickerOption.OtherFiles))
                View.VISIBLE
            else
                View.GONE
            setOnClickListener {
                dispatchPicker(mimeType = MimeType.OTHERS)
            }
        }
        binding.noOptionsSpecifiedTV.apply {
            visibility = if (pickerOptions.isEmpty()) View.VISIBLE else View.GONE
            setOnClickListener {
                dismiss()
            }
        }
    }

    private fun dispatchPicker(mimeType: MimeType, openCamera: Boolean = false) {
        imageCapturedFromCamera = false
        val intent = Intent()
        when (mimeType) {
            MimeType.IMAGE,
            MimeType.VIDEO -> {
                if (openCamera) {
                    //launch camera app intent
                    intent.action = if (mimeType == MimeType.IMAGE) {
                        imageCapturedFromCamera = true
                        MediaStore.ACTION_IMAGE_CAPTURE
                    } else
                        MediaStore.ACTION_VIDEO_CAPTURE
                } else {
                    //launch intent for gallery
                    intent.type = mimeType.key
                    intent.action = Intent.ACTION_GET_CONTENT
                }
            }
            else -> {
                //launch intent for others (documents and files)
                intent.type = mimeType.key
                //multiple mime types can be selected
                intent.action = Intent.ACTION_GET_CONTENT
            }
        }
        if (fragment == null) {
            activity.startActivityForResult(
                Intent.createChooser(intent, null),
                ConstantsHolder.ACTIVITY_RESULT_REQUEST_CODE
            )
        } else {
            fragment.startActivityForResult(
                Intent.createChooser(intent, null),
                ConstantsHolder.ACTIVITY_RESULT_REQUEST_CODE
            )
        }
    }

    fun show() {
        if (bottomSheetDialog.isShowing)
            return
        bottomSheetDialog.show()
    }

    private fun dismiss() {
        if (bottomSheetDialog.isShowing)
            bottomSheetDialog.dismiss()
    }

    /**
     * @param requestCode - Request Code from onActivityResult
     */
    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?,
        onFileReady: (File?, String?, Boolean) -> Unit
    ) {
        when (requestCode) {
            ConstantsHolder.ACTIVITY_RESULT_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && intent != null) {
                    intent.data?.apply {
                        onFileReady.invoke(null, null, true)
                        binding.optionsTV.visibility = View.GONE
                        binding.processingTV.visibility = View.VISIBLE
                        activity.createFileFromContentUri(this) { file, mimeType ->
                            binding.optionsTV.visibility = View.VISIBLE
                            binding.processingTV.visibility = View.GONE
                            dismiss()
                            onFileReady.invoke(file, mimeType, false)
                        }
                    }
                    if (imageCapturedFromCamera) {
                        intent.extras?.get("data")?.apply {
                            (this as? Bitmap)?.let { bitmap ->
                                onFileReady.invoke(null, null, true)
                                binding.optionsTV.visibility = View.GONE
                                binding.processingTV.visibility = View.VISIBLE
                                activity.createFileFromBitmap(bitmap) { file, mimeType ->
                                    binding.optionsTV.visibility = View.VISIBLE
                                    binding.processingTV.visibility = View.GONE
                                    dismiss()
                                    onFileReady.invoke(file, mimeType, false)
                                }
                            }
                        }
                    }
                } else {
                    dismiss()
                }
            }
        }
    }

}