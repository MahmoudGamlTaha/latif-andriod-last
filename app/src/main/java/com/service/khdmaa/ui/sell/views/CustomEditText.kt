package com.service.khdmaa.ui.sell.views

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.service.khdmaa.databinding.CustomEdittextLayoutBinding
import com.service.khdmaa.utiles.Utiles


class CustomEditText(
    context_: Context,
    label: String = "",
    val isText: Boolean,
    val isMultiline: Boolean,
    action: ViewAction<String>
) : CustomParentView<String>(
    context_,
    label,
    action
),
    TextWatcher {
    override fun createView() {
        val editText = CustomEdittextLayoutBinding.inflate(LayoutInflater.from(context))
        view = editText.apply {
            labeltxt.text = "$label "
            editext.hint = label
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 40, 0, 5)
            root.layoutParams = params
            editext.addTextChangedListener(this@CustomEditText)
            var inputType = InputType.TYPE_CLASS_TEXT
            if (!isText)
                inputType = InputType.TYPE_CLASS_NUMBER
            else if (isMultiline) {
                inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

             }
            Utiles.log_D("snnsnsnsnsn", "$isMultiline")
            editext.inputType = inputType
        }.root

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        action?.getActionId(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {
    }
}