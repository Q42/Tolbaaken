package com.q42.tolbaaken

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TolbaakenTest {
    lateinit var testLogger: TestTolbaakenLogger

    @Before
    fun setup() {
        testLogger = TestTolbaakenLogger()
        Tolbaaken.logger = testLogger
    }

    @Test
    fun testTag() {
        Tolbaaken.debug { "message" }

        Assert.assertEquals("TolbaakenTest", testLogger.logMessages.first().tag)
        Assert.assertEquals("message", testLogger.logMessages.first().message)
    }

    @Test
    fun testTagLength() {
        Tolbaaken.debug(tag = "VeryVeryVeryVeryVeryVeryLongTagName") { "message" }

        Assert.assertEquals("VeryVeryVeryVeryVeryVer", testLogger.logMessages.first().tag)
        Assert.assertEquals("message", testLogger.logMessages.first().message)
    }

    @Test
    fun testLevels() {
        Tolbaaken.debug { "message" }
        Tolbaaken.verbose { "message" }
        Tolbaaken.info { "message" }
        Tolbaaken.warn { "message" }
        Tolbaaken.error { "message" }

        Assert.assertEquals(listOf(TolbaakenLogLevel.Debug, TolbaakenLogLevel.Verbose, TolbaakenLogLevel.Info, TolbaakenLogLevel.Warn, TolbaakenLogLevel.Error), testLogger.logMessages.map { it.level })
    }

    @Test
    fun testStack() {
        val e = Exception("test")
        Tolbaaken.debug(e) { "message" }

        Assert.assertEquals("message", testLogger.logMessages.first().message)
        Assert.assertEquals(e, testLogger.logMessages.first().throwable)
    }
}

data class LogMessage(val tag: String, val message: String, val level: TolbaakenLogLevel, val throwable: Throwable? = null)

class TestTolbaakenLogger : TolbaakenLogger {
    val logMessages = mutableListOf<LogMessage>()

    override fun log(level: TolbaakenLogLevel, tag: String, message: String, throwable: Throwable?) {
        logMessages += LogMessage(tag, message, level, throwable)
    }
}