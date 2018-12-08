package androidcomponents.timashkov.com.androidcomponents

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat

class MoneyTextWatcher(private val editText: EditText) : TextWatcher {

    private val decimalFormat = DecimalFormat("###.##")
    private var separatorEntered = false
    private val separator: Char

    init {
        decimalFormat.isDecimalSeparatorAlwaysShown = false
        separator = decimalFormat.decimalFormatSymbols.decimalSeparator
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        editText.removeTextChangedListener(this)
        s?.let {
            var source = s.toString()

            val separatorEnteredNow = source.contains(separator)
            if (separatorEnteredNow != separatorEntered && separatorEntered) {
                source = source.substring(0, source.length - 2)
            }
            separatorEntered = separatorEnteredNow

            var amountString = source.replace(Regex("[^0-9$separator]"), "")

            if (amountString.isEmpty() || amountString.startsWith(separator)) {
                editText.setText("")
                separatorEntered = false
                editText.addTextChangedListener(this)
                return
            }
            val resultText = if (separatorEntered) {

                val cutIndexBySeparator = amountString.indexOf(separator) + 3
                val cutIndex = if (cutIndexBySeparator > amountString.length) amountString.length else cutIndexBySeparator
                amountString = amountString.substring(0, cutIndex)

                val amount = decimalFormat.parse(amountString).toFloat()
                String.format("%.2f", amount)
            } else {
                val amount = decimalFormat.parse(amountString).toInt()
                String.format("%d", amount)

            }
            editText.setText(resultText)
            editText.setSelection(if (start + count > resultText.length) resultText.length else start + count)
        }
        editText.addTextChangedListener(this)
    }
}