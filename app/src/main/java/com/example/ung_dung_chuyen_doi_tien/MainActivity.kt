package com.example.ung_dung_chuyen_doi_tien

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var editTextSource: EditText
    private lateinit var editTextTarget: EditText
    private lateinit var spinnerSourceCurrency: Spinner
    private lateinit var spinnerTargetCurrency: Spinner
    private var isSourceEditing = false
    private var isTargetEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextSource = findViewById(R.id.editTextSource)
        editTextTarget = findViewById(R.id.editTextTarget)
        spinnerSourceCurrency = findViewById(R.id.spinnerSourceCurrency)
        spinnerTargetCurrency = findViewById(R.id.spinnerTargetCurrency)

        val buttonIds = arrayOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6, R.id.button7,
            R.id.button8, R.id.button9, R.id.buttonC, R.id.buttonCE
        )

        for (id in buttonIds) {
            findViewById<Button>(id).setOnClickListener(this)
        }

        val currencies = listOf("USD", "VND")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerSourceCurrency.adapter = adapter
        spinnerTargetCurrency.adapter = adapter

        // TextWatcher cho editTextSource
        editTextSource.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isTargetEditing) {
                    isSourceEditing = true
                    convertCurrencyFromSource()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                isSourceEditing = false
            }
        })

        // TextWatcher cho editTextTarget
        editTextTarget.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isSourceEditing) {
                    isTargetEditing = true
                    convertCurrencyFromTarget()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                isTargetEditing = false
            }
        })


        spinnerSourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (spinnerSourceCurrency.selectedItem.toString() == "USD") {
                    spinnerTargetCurrency.setSelection(1)
                } else {
                    spinnerTargetCurrency.setSelection(0)
                }
                convertCurrencyFromSource()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Sự kiện chọn cho spinnerTargetCurrency
        spinnerTargetCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (spinnerTargetCurrency.selectedItem.toString() == "USD") {
                    spinnerSourceCurrency.setSelection(1)
                } else {
                    spinnerSourceCurrency.setSelection(0)
                }
                convertCurrencyFromSource()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    override fun onClick(v: View?) {
        val currentText = editTextSource.text.toString()
        when (v?.id) {
            R.id.button0 -> editTextSource.setText(currentText + "0")
            R.id.button1 -> editTextSource.setText(currentText + "1")
            R.id.button2 -> editTextSource.setText(currentText + "2")
            R.id.button3 -> editTextSource.setText(currentText + "3")
            R.id.button4 -> editTextSource.setText(currentText + "4")
            R.id.button5 -> editTextSource.setText(currentText + "5")
            R.id.button6 -> editTextSource.setText(currentText + "6")
            R.id.button7 -> editTextSource.setText(currentText + "7")
            R.id.button8 -> editTextSource.setText(currentText + "8")
            R.id.button9 -> editTextSource.setText(currentText + "9")
            R.id.buttonC -> editTextSource.setText("")
            R.id.buttonCE -> {
                if (currentText.isNotEmpty()) {
                    editTextSource.setText(currentText.dropLast(1))
                }
            }
        }
        editTextSource.setSelection(editTextSource.text.length)
        convertCurrencyFromSource()
    }

    private fun convertCurrencyFromSource() {
        val sourceCurrency = spinnerSourceCurrency.selectedItem.toString()
        val targetCurrency = spinnerTargetCurrency.selectedItem.toString()

        val sourceAmount = editTextSource.text.toString().toDoubleOrNull()
        if (sourceAmount != null) {
            val exchangeRates = mapOf(
                "USD" to 1.0,
                "VND" to 23000.0
            )

            val sourceRate = exchangeRates[sourceCurrency] ?: 1.0
            val targetRate = exchangeRates[targetCurrency] ?: 1.0

            val result = sourceAmount * (targetRate / sourceRate)
            editTextTarget.setText(result.toString())
        } else {
            editTextTarget.setText("")
        }
    }

    private fun convertCurrencyFromTarget() {
        val targetCurrency = spinnerTargetCurrency.selectedItem.toString()
        val sourceCurrency = spinnerSourceCurrency.selectedItem.toString()

        val targetAmount = editTextTarget.text.toString().toDoubleOrNull()
        if (targetAmount != null) {
            val exchangeRates = mapOf(
                "USD" to 1.0,
                "VND" to 23000.0
            )

            val targetRate = exchangeRates[targetCurrency] ?: 1.0
            val sourceRate = exchangeRates[sourceCurrency] ?: 1.0

            val result = targetAmount * (sourceRate / targetRate)
            editTextSource.setText(result.toString())
        } else {
            editTextSource.setText("")
        }
    }
}
