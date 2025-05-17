package de.d2s.hamprefix2country

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import java.util.Calendar
import androidx.core.graphics.toColorInt

class MainActivity : AppCompatActivity() {

    val cal = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val callsignEdit = findViewById<EditText>(R.id.callsignEdit)
        val countryText = findViewById<TextView>(R.id.countryTextView)
        val infoButton = findViewById<Button>(R.id.aboutButton)
        infoButton.setBackgroundColor("#4CAF50".toColorInt())
        infoButton.setOnClickListener {
            showInfo()
        }
        countryText.movementMethod = ScrollingMovementMethod()
        val clearCallsignButton = findViewById<TextView>(R.id.clearCallsignButton)
        clearCallsignButton.setBackgroundColor("#4CAF50".toColorInt())
        clearCallsignButton.setOnClickListener {
            callsignEdit.text.clear()
        }
        val csvText: String = application.assets.open("dxcc-2020-02_stripped.csv").bufferedReader().use{
            it.readText()
        }
        val prefixCountryCSV = PrefixCountryCSV(csvText)
        prefixCountryCSV.readCsv()

        callsignEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val foundCountries = prefixCountryCSV.findCountriesForPrefix(s.toString())
                var countryListString = ""
                for (c in foundCountries) {
                    countryListString += (c + "\n")
                }
                countryText.text = countryListString
                if (s.toString().isEmpty()) {
                    countryText.text = ""
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Not used
            }
        })

        // Set input filter to convert letters to uppercase, keep numbers
        // This is ChatGPT code... it looks wrong to me but it works for now.
        // But I suspect this to cause the annoying random underline in the EditText...
        // No, in fact the Android keyboard does the underline thanks to spell-checking.
        val upperCaseFilter = InputFilter { source, start, end, dest, dstart, dend ->
            val filtered = StringBuilder()
            for (i in start until end) {
                val c = source[i]
                filtered.append(if (c.isLetter()) c.uppercaseChar() else c)
            }
            filtered.toString()
        }

        callsignEdit.filters = arrayOf(upperCaseFilter)
    }

    private fun showInfo() {
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        val year = cal.get(Calendar.YEAR)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("App Info")
        builder.setMessage("HAMPrefix2Country v"+versionName+"\n\n"+
            "This app translates the beginning of a callsign "+
            "prefix to the closest matching country names because I am too lazy "+
            "to remember all of them....\n\n"+
            "Author: Dennis Lübke, 2025-"+year+"\n"+
            "Contact: android-dev@dennis2society.de\n"+
            "github.com/dennis2society/HAMPrefix2Country")
        builder.show()
    }
}