package org.example.snippetservice.redis.producer

import com.example.printscriptservice.redisEvents.LintRequestEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import redisStreams.RedisStreamProducer

@Component
class LintEventsProducer
    @Autowired
    constructor(
        @Value("\${redis.stream.linter-request-key}") streamKey: String,
        redis: RedisTemplate<String, String>,
    ) : RedisStreamProducer(streamKey, redis) {
        fun publishEvent(
            userID: String,
            snippetID: String,
            snippetName: String,
            rules: String,
            language: String,
            version: String,
        ) {
            println("Publishing on stream: $streamKey")
            val res = LintRequestEvent(userID, snippetID, snippetName, rules, language, version)
            emit(res)
        }
    }
