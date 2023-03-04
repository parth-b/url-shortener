package com.shorten.backend.controller
// curl -X POST "http://localhost:8081/api/url" -H "Content-Type: application/json" -d "{ \"url\": \"facebook.com\" }"

import com.shorten.backend.exception.urlNotFoundException
import com.shorten.backend.model.GenerateHashRequest
import com.shorten.backend.model.Url
import com.shorten.backend.repo.URLRepo
import com.shorten.backend.service.UrlService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException
import javax.validation.Valid

@RestController
@RequestMapping("/api")
class Controller (private val urlService: UrlService, private val urlRepo: URLRepo) {

    private fun getRandomString(length: Int) : String {
        val charset = "abcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    @PostMapping
    @RequestMapping("/url")
    fun addUrl(@Valid @RequestBody newUrl: GenerateHashRequest) :ResponseEntity<String>{
        val longUrl:String = newUrl.url

        var url:URL
        try {
             url = URL("http://${longUrl}")
        }catch (e: UnknownHostException){
            return ResponseEntity<String>("invalid url", HttpStatus.NOT_FOUND)
        }

        var hash:String = ""
        if(urlService.findByOriginalUrl(longUrl)==null) {
            hash = getRandomString(4)
            var url: Url = Url().apply {
                originalURL = longUrl
                hashURL = hash
                hits = 0
            }
            urlService.save(url)
        }
        return ResponseEntity<String>("localhost:8081/api/${hash}", HttpStatus.CREATED)
    }
// curl -X POST "http://localhost:8081/api/url" -H "Content-Type: application/json" -d "{ \"url\": \"facebook.com\" }"
    @GetMapping("/url")
    fun finAllUrl(): MutableIterable<Url> {
        return urlService.findAll() as MutableIterable<Url>
    }

    @RequestMapping("/{hash}")
    fun redirect(@PathVariable("hash") hash: String,
                       attributes: RedirectAttributes): RedirectView? {
        attributes.addFlashAttribute("flashAttribute",
            "redirect")
        attributes.addAttribute("attribute","redirect")
        val u = urlService.findByHashUrl(hash)

        var hits : Int
        if(u!=null) {
            hits = u.hits
            hits++;
            u.apply { this.hits = hits }
            urlService.save(u)
        }

        val redirectUrl = u?.originalURL
        if (redirectUrl != null) {
            return RedirectView("http://${redirectUrl}")
        }
        throw urlNotFoundException()

    }
}

@RestController
@RequestMapping("/hits")
class AnalyticsController(private val urlService: UrlService) {
    @GetMapping("/{url}")
    fun getHits(@PathVariable("url") url:String): String {

        val u = urlService.findByOriginalUrl(url)
        if (u != null) {
            return u.hits.toString()
        }
        return "Url not found!!!"
    }
}
