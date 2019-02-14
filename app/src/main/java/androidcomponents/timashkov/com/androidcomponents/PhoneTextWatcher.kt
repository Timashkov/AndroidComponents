package androidcomponents.timashkov.com.androidcomponents

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText

class PhoneTextWatcher(private val editText: EditText, private val onTextChanged: () -> Unit) : TextWatcher {
    private var tempFilteringString = "+"

    private var position = 0

    override fun afterTextChanged(s: Editable?) {
        onTextChanged()
        editText.removeTextChangedListener(this)
        s?.let {
            var userInput = s.toString()


            userInput = userInput.replace("[^\\d|+]".toRegex(), "")
            var c = 0
            if (userInput.isNotEmpty() && userInput[0] == '+')
                c = 1
            if (s.toString() != tempFilteringString) {
                if (userInput.length > 11 + c)
                    userInput = userInput.substring(0, 11 + c)

                val sb = StringBuilder()
                for (i in 0 until userInput.length) {
                    if (i != 0 && userInput[i] == '+')
                        continue

                    val spacesPositions = arrayOf(1 + c, 4 + c, 7 + c, 9 + c)
                    if (i in spacesPositions) {
                        if (position == i + 1 + spacesPositions.indexOf(i))
                            position++
                        sb.append(" ")
                    }
                    sb.append(userInput[i])
                }
                tempFilteringString = sb.toString()
                if (!tempFilteringString.startsWith("+")) {
                    tempFilteringString = "+$tempFilteringString"
                }

                s.filters = arrayOfNulls<InputFilter>(0)

                if (editText.text.toString() != tempFilteringString)
                    editText.setText(tempFilteringString)
                editText.setSelection(if (tempFilteringString.length > position) position else tempFilteringString.length)
            }
        }
        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        position = start + count
        if (position == 0)
            position = 1
    }
}
