package org.piramalswasthya.sakhi.model

import android.text.InputType.TYPE_CLASS_TEXT
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File


data class FormInput(
    val inputType: InputType,
    var title: String,
    var entries: Array<String>? = null,
    var required: Boolean,
    var value: MutableStateFlow<String?> = MutableStateFlow(null),
    val regex: String? = null,
    val allCaps: Boolean = false,
    val etInputType: Int = TYPE_CLASS_TEXT,
    val isMobileNumber: Boolean = false,
    val etMaxLength: Int = 50,
    var errorText: String? = null,
    var max: Long? = null,
    var min: Long? = null,
    var minDecimal : Double? = null,
    var maxDecimal : Double? = null,
    val orientation: Int? = null,
    var imageFile: File? = null
){

//-----------------Do NOT mess with order of enum values ----------------------//
    enum class InputType{
        EDIT_TEXT,
        DROPDOWN,
        RADIO,
        DATE_PICKER,
        TEXT_VIEW,
        IMAGE_VIEW,
        CHECKBOXES,
        TIME_PICKER,
        HEADLINE
    }
}
