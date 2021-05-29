package io.bibuti.pickerlibrary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.bibuti.pickerlibrary.databinding.FragmentPickerSheetBinding

class Picker(
    private val context: Context,
    pickerOptions: List<PickerOption> = listOf(
        PickerOption.CameraImage,
        PickerOption.GalleryImage,
        PickerOption.Documents,
        PickerOption.OtherFiles
    )
) {

    private var bottomSheetDialog: BottomSheetDialog = BottomSheetDialog(
        context,
        R.style.AppBottomSheetDialogTheme
    )

    private var binding: FragmentPickerSheetBinding = FragmentPickerSheetBinding.bind(
        LayoutInflater
            .from(context)
            .inflate(R.layout.fragment_picker_sheet, null, false)
    )

    init {
        bottomSheetDialog.setContentView(binding.root)
        binding.cameraTV.apply {
            visibility =
                if (pickerOptions.contains(PickerOption.CameraImage)) View.VISIBLE else View.GONE
            setOnClickListener {
                dispatchPicker(mimeType = MimeType.IMAGE, openCamera = true)
            }
        }
        binding.recordVideoTV.apply {
            visibility =
                if (pickerOptions.contains(PickerOption.CameraVideo)) View.VISIBLE else View.GONE
            setOnClickListener {
                dispatchPicker(mimeType = MimeType.VIDEO, openCamera = true)
            }
        }
        binding.galleryTV.apply {
            val isVideoPicker = pickerOptions.contains(PickerOption.GalleryVideo)
            setCompoundDrawablesWithIntrinsicBounds(
                if (isVideoPicker) ContextCompat.getDrawable(
                    this.context,
                    R.drawable.ic_gallery_video
                ) else ContextCompat.getDrawable(this.context, R.drawable.ic_gallery_image),
                null,
                null,
                null
            )
            visibility =
                if (pickerOptions.contains(PickerOption.GalleryImage) or pickerOptions.contains(
                        PickerOption.GalleryVideo
                    )
                ) View.VISIBLE else View.GONE
            setOnClickListener {
                dispatchPicker(
                    mimeType = if (isVideoPicker)
                        MimeType.VIDEO
                    else
                        MimeType.IMAGE,
                    openCamera = false
                )
            }
        }
        binding.documentTV.apply {
            visibility =
                if (pickerOptions.contains(PickerOption.Documents)) View.VISIBLE else View.GONE
            setOnClickListener {
                dispatchPicker(mimeType = MimeType.DOCUMENTS, openCamera = false)
            }
        }
        binding.otherFilesTV.apply {
            visibility =
                if (pickerOptions.contains(PickerOption.OtherFiles)) View.VISIBLE else View.GONE
            setOnClickListener {
                dispatchPicker(mimeType = MimeType.OTHERS, openCamera = false)
            }
        }
    }

    private fun dispatchPicker(mimeType: MimeType, openCamera: Boolean = false) {
        if ((mimeType == MimeType.IMAGE) or (mimeType == MimeType.VIDEO)) {
            if (openCamera) {
                //launch camera app intent
            } else {
                //launch intent
            }
        } else {
            //launch intent
        }
        dismiss()
    }

    fun show() {
        if (bottomSheetDialog.isShowing)
            return
        bottomSheetDialog.show()
    }

    fun dismiss() {
        if (bottomSheetDialog.isShowing)
            bottomSheetDialog.dismiss()
    }

}