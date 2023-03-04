package com.shorten.backend.model

import javax.persistence.*
import javax.persistence.Id

@Entity(name = "urls")
class Url() {
    @Id
    @Column(name = "originalURL")
    var originalURL:String? = null

    @Column(name = "hashURL")
    var hashURL:String? = null

    @Column(name = "hits")
    var hits:Int = 0

    public fun Url(originalUrl:String, hashUrl:String, hits: Int) {
        this.originalURL=originalUrl
        this.hashURL = hashUrl
        this.hits = hits
    }
}
