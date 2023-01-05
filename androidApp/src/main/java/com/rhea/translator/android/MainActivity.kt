package com.rhea.translator.android

import android.os.Bundle
import com.rhea.translator.Greeting
import android.widget.TextView
import androidx.activity.ComponentActivity

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
