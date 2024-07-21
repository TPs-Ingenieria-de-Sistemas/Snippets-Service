package org.example.snippetservice.redis.route
import com.example.printscriptservice.redisEvents.LintRequestEvent
import org.example.snippetservice.redis.producer.LintEventsProducer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

private val logger: Logger = LoggerFactory.getLogger(Service::class.java)

@RestController
class StreamTestRoute
    @Autowired
    constructor(private val producer: LintEventsProducer) {
        @PostMapping("/lint-stream/{userID}/{snippetID}/{rules}/{language}/{version}")
        suspend fun post(
            @PathVariable userID: String,
            @PathVariable snippetID: String,
            @PathVariable rules: String,
            @PathVariable language: String,
            @PathVariable version: String,
        ) {
            logger.info("TESTING LINT STREAM")
            producer.publishEvent(userID, snippetID, rules, language, version)
        }
    }
