package com.speedtest.api

import com.google.gson.Gson
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/speedtest")
class SpeedTestResource(val pubSub: PubSubTemplate) {

    val gson: Gson = Gson()

    data class Speed(val download: Float, val upload: Float)
    data class Client(val ip: String, val lat: Float, val lon: Float, val isp: String, val country: String)
    data class Server(val host: String, val lat: Float, val lon: Float, val country: String, val distance: Int, val ping: Int, val id: String)
    data class Data(val speeds: Speed, val client: Client, val server: Server)
    data class TestResult(val user: String, val device: Int, val timestamp: Long, val data: Data)

    @PostMapping
    fun createUser(@RequestBody testResult: TestResult) {
        this.pubSub.publish("speedtest", gson.toJson(testResult))
    }
}
