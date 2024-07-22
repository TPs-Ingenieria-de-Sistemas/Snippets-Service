package org.example.snippetservice.redis.consumer

import redisEvents.LintResultEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.connection.stream.ObjectRecord
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

import redisStreams.RedisStreamConsumer
import java.time.Duration

@Component
class LintResponseConsumer @Autowired constructor(
    redis: RedisTemplate<String, String>,
    @Value("\${redis.stream.linter-response-key}") streamKey: String,
    @Value("\${redis.groups.lint}") groupId: String,
) : RedisStreamConsumer<LintResultEvent>(streamKey, groupId, redis) {

    private val logger: Logger = LoggerFactory.getLogger(Service::class.java)

    override fun onMessage(record: ObjectRecord<String, LintResultEvent>) {
        logger.info("Received Message from Redis")
        val req = record.value
        logger.info("Linted Request: {user id: ${req.userID}}, {snippetId: ${req.snippetID}}, result: ${req.result}")
    }

    override fun options(): StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, LintResultEvent>> {
        logger.info("Received Message from Redis")
        return StreamReceiver.StreamReceiverOptions.builder()
            .pollTimeout(Duration.ofMillis(10000)) // Set poll rate
            .targetType(LintResultEvent::class.java) // Set type to de-serialize record
            .build()
    }
}
