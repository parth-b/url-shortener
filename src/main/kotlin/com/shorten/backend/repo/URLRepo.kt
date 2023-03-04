package com.shorten.backend.repo

import com.shorten.backend.auth.models.User
import com.shorten.backend.model.Url

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*


@Repository
interface URLRepo: CrudRepository<Url, Long> {
    @Query ("select u from urls u where u.hashURL = ?1")
    abstract fun findByHashUrl(hashUrl: String) : Url?

    @Query ("select u from urls u where u.originalURL = ?1")
    abstract fun findByOriginalUrl(originalUrl: String): Url?
}





