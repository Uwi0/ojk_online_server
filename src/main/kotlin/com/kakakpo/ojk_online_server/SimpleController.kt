package com.kakakpo.ojk_online_server

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api_v1")
class SimpleController {

    @GetMapping("/ping")
    fun getPing() = "ok"
}