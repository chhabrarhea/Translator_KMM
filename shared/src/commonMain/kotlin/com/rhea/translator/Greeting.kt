package com.rhea.translator

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}