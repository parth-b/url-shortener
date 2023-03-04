package com.shorten.backend.model

import java.net.URL

class  GenerateHashRequest (
    var url :String
) {
    fun checkUrl() : Boolean {
        var u : URL =  URL(url)
        u.toURI();
        if(u.host == null) return false
        return true
    }
}