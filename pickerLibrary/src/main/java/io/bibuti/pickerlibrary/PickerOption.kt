package io.bibuti.pickerlibrary

sealed class PickerOption {
    object CameraImage : PickerOption()
    object CameraVideo : PickerOption()
    object GalleryImage : PickerOption()
    object GalleryVideo : PickerOption()
    object Documents : PickerOption()
    object OtherFiles : PickerOption()
}