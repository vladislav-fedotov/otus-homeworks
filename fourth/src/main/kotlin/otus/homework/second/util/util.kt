package otus.homework.second.util

import ru.tinkoff.eclair.logger.SimpleLogger
import kotlin.system.measureTimeMillis


inline fun <T> measure(block: () -> T): T {
    val logger = SimpleLogger()
    var result:T? = null
    val measuredTimeMillis = measureTimeMillis {
        result = block()
    }
    logger.info("this method took $measuredTimeMillis millis")
    return result!!
}