package androidcomponents.timashkov.com.androidcomponents

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText

class PhoneTextWatcher(private val editText: EditText) : TextWatcher {
    private var tempFilteringString = "+"

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)
        s?.let {
            val userInput = s.toString().replace("[^\\d|+]".toRegex(), "")
            var c = 0
            if (userInput.isNotEmpty() && userInput[0] == '+')
                c = 1
            if (s.toString() != tempFilteringString) {
                if (userInput.length <= 11 + c) {
                    val sb = StringBuilder()
                    for (i in 0 until userInput.length) {
                        if (i != 0 && userInput[i] == '+')
                            continue

                        if (i in arrayOf(1 + c, 4 + c, 7 + c, 9 + c)) {
                            sb.append(" ")
                        }
                        sb.append(userInput[i])
                    }
                    tempFilteringString = sb.toString()
                    if (!tempFilteringString.startsWith("+")) {
                        tempFilteringString = "+$tempFilteringString"
                    }

                    s.filters = arrayOfNulls<InputFilter>(0)
                }
                editText.setText(tempFilteringString)
                editText.setSelection(tempFilteringString.length)
            }
        }
        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
