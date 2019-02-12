package androidcomponents.timashkov.com.androidcomponents

/** The local-part of the email address may use any of these ASCII characters:

uppercase and lowercase Latin letters A to Z and a to z;
digits 0 to 9;
special characters !#$%&'*+-/=?^_`{|}~;
dot ., provided that it is not the first or last character unless quoted, and provided also that it does not appear consecutively unless quoted (e.g. John..Doe@example.com is not allowed but "John..Doe"@example.com is allowed);
space and "(),:;<>@[\] characters are allowed with restrictions (they are only allowed inside a quoted string, as described in the paragraph below, and in addition, a backslash or double-quote must be preceded by a backslash);
comments are allowed with parentheses at either end of the local-part; e.g. john.smith(comment)@example.com and (comment)john.smith@example.com are both equivalent to john.smith@example.com.*/

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class EmailTextWatcher(private val editText: EditText, private val onTextChanged: () -> Unit) : TextWatcher {
    private var namePart = ""
    private var position = 0
    private var lastKnownValue = ""
    private var lastKnownPosition = 0

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)
        onTextChanged()
        s?.let {

            var userInput = s.toString().toLowerCase()

            if (userInput.isEmpty()) {
                finalizeValidation(userInput)
                return
            }
            if (userInput.startsWith("\"")) {
                //quoted string, allowed +/- all symbols

                runQuotedValidation(userInput)
            } else {
                userInput = userInput.replace(Regex("[^0-9a-z+.@_!#\$%&'*/=?^`{|}~-]"), "")
                parseUnQuoted(userInput)
            }
        }
    }

    private fun runQuotedValidation(userInput: String) {

        if (userInput.count { symbol -> symbol == '"' } > 2) {
            var rest = userInput.substring(1)
            rest = rest.substring(0, rest.indexOfFirst { symbol -> symbol == '"' })
            finalizeValidation('"' + rest)
            return
        } else if (userInput.count { symbol -> symbol == '"' } == 2) {
            if (userInput.endsWith('"')) {
                finalizeValidation(userInput)
                return
            }
            var rest = userInput.substring(1)
            val lastQuote = rest.indexOf('"')
            if (rest[lastQuote + 1] == '@') {
                namePart = userInput.substring(0, lastQuote + 2)
                val forValidation = userInput.substring(namePart.length + 1).replace(Regex("[^a-z0-9.]"), "")
                runDomainValidation(forValidation)
            } else {
                rest = rest.substring(0, rest.indexOfFirst { symbol -> symbol == '"' } + 1)
                finalizeValidation('"' + rest)
                return
            }
        } else {
            finalizeValidation(userInput)
            return
        }
    }

    private fun parseUnQuoted(input: String) {

        var userInput = input

        if (userInput.startsWith("@"))
            userInput = userInput.substring(1)
        if (userInput.startsWith("."))
            userInput = userInput.substring(1)
        while (userInput.contains(".."))
            userInput = userInput.replace("..", ".")

        val firstAtSymbol = userInput.indexOfFirst { symbol -> symbol == '@' }
        if (userInput.isEmpty() || firstAtSymbol == -1) {
            finalizeValidation(userInput)
            return
        }

        namePart = userInput.substring(0, firstAtSymbol)
        if (namePart.endsWith("."))
            namePart = namePart.substring(0, namePart.length - 1)
        val forValidation = userInput.substring(namePart.length + 1).replace(Regex("[^a-z0-9.]"), "")
        runDomainValidation(forValidation)
    }

    private fun runDomainValidation(input: String) {
        
        val firstDotSymbol = input.indexOfFirst { symbol -> symbol == '.' }
        if (input.isEmpty() || firstDotSymbol == -1) {
            val result = "$namePart@$input"
            finalizeValidation(result)
            return
        }

        val domain = input.substring(0, firstDotSymbol)
        val domainZone = input.substring(firstDotSymbol + 1).replace(".", "")
        val result = "$namePart@$domain.$domainZone"
        finalizeValidation(result)
    }

    private fun finalizeValidation(value: String) {
        if (lastKnownValue.length == value.length && position > 0 && lastKnownPosition != position)
            position--
        lastKnownValue = value
        lastKnownPosition = position
        if (editText.text.toString() != value)
            editText.setText(value)
        editText.setSelection(if (value.length > position) position else value.length)
        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        position = start + count
    }
}
