package com.example.calculator_26

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var current = ""
    private var operator = ""
    private var firstNumber = 0.0
    private var secondNumber = 0.0
    private var result = 0.0
    private var isNewOperation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        display = findViewById(R.id.txtDisplay)

        // ปุ่มตัวเลข 0-9
        val numberButtons = listOf(
            R.id.Btn0, R.id.Btn1, R.id.Btn2, R.id.Btn3, R.id.Btn4,
            R.id.Btn5, R.id.Btn6, R.id.Btn7, R.id.Btn8, R.id.Btn9
        )

        // Event ปุ่มตัวเลข
        for (id in numberButtons) {
            findViewById<Button>(id)?.setOnClickListener {
                numberClicked((it as Button).text.toString())
            }
        }

        // Event ปุ่มตัวดำเนินการ + - * / %
        findViewById<Button>(R.id.btnPlus)?.setOnClickListener { operatorClicked("+") }
        findViewById<Button>(R.id.btnMinus)?.setOnClickListener { operatorClicked("-") }
        findViewById<Button>(R.id.btnMul)?.setOnClickListener { operatorClicked("*") }
        findViewById<Button>(R.id.btnDivide)?.setOnClickListener { operatorClicked("/") }
        findViewById<Button>(R.id.BtnMod)?.setOnClickListener { operatorClicked("%") }

        // Event ปุ่มพิเศษ = AC DEL . ±
        findViewById<Button>(R.id.BtnDiv4)?.setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.BtnAAC)?.setOnClickListener { clearAll() }
        findViewById<Button>(R.id.BtnDel)?.setOnClickListener { deleteLast() }
        findViewById<Button>(R.id.BtnMod4)?.setOnClickListener { addDot() }
        findViewById<Button>(R.id.BtnAAC4)?.setOnClickListener { toggleSign() }
    }

    private fun numberClicked(value: String) {
        if (isNewOperation) {
            current = ""
            isNewOperation = false
        }
        current += value
        display.text = current
    }

    private fun operatorClicked(op: String) {
        if (current.isNotEmpty()) {
            firstNumber = current.toDoubleOrNull() ?: 0.0
        }
        operator = op
        current = ""
    }

    private fun calculateResult() {
        if (operator.isEmpty()) return
        if (current.isNotEmpty()) {
            secondNumber = current.toDoubleOrNull() ?: 0.0
        }
        result = when (operator) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> if (secondNumber != 0.0) firstNumber / secondNumber else Double.NaN
            "%" -> firstNumber % secondNumber
            else -> 0.0
        }

        val formattedResult = if (result.isNaN()) {
            "Cannot divide by zero"
        } else if (result == result.toLong().toDouble()) {
            result.toLong().toString()
        } else {
            result.toString()
        }

        display.text = formattedResult
        current = if (result.isNaN()) "" else formattedResult
        isNewOperation = true
    }

    private fun clearAll() {
        current = ""
        operator = ""
        firstNumber = 0.0
        secondNumber = 0.0
        result = 0.0
        isNewOperation = true
        display.text = "0"
    }

    private fun deleteLast() {
        if (current.isNotEmpty()) {
            current = current.dropLast(1)
            display.text = if (current.isEmpty()) "0" else current
        }
    }

    private fun addDot() {
        if (isNewOperation) {
            current = "0"
            isNewOperation = false
        }
        if (!current.contains(".")) {
            current += "."
            display.text = current
        }
    }

    private fun toggleSign() {
        if (current.isNotEmpty() && current != "0") {
            current = if (current.startsWith("-")) {
                current.substring(1)
            } else {
                "-$current"
            }
            display.text = current
        }
    }


}