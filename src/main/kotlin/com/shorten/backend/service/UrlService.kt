package com.shorten.backend.service

import com.shorten.backend.model.Url
import com.shorten.backend.repo.URLRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UrlService (@Autowired var urlRepo: URLRepo) {
    fun save(u: Url) {
        urlRepo.save(u)
    }
    fun findAll(): MutableIterable<Url> {
        return urlRepo.findAll()
    }
    fun findByHashUrl(url: String): Url? {
        return urlRepo.findByHashUrl(url)
    }
    fun findByOriginalUrl(url: String): Url? {
        return urlRepo.findByOriginalUrl(url)
    }
}
