package com.acclex.verificationdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initEvents()
    }

    private fun initEvents() {
        input_1.setOnCompleteListener {
            log(javaClass.toString(), it)
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        input_2.setOnCompleteListener {
            log(javaClass.toString(), it)
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        input_3.setOnCompleteListener {
            log(javaClass.toString(), it)
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        input_4.setOnCompleteListener {
            log(javaClass.toString(), it)
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        input_1.setPasteListener { data, box ->
            var c = data.getItemAt(0).coerceToText(this)
            if (c.length > box) {
                c = c.subSequence(0, box)
            }
            Toast.makeText(this, c, Toast.LENGTH_SHORT).show()
            log(javaClass.toString(), c.toString())
        }
    }
    private fun log(TAG: String, msg: String) = Log.d(TAG, msg)
}
