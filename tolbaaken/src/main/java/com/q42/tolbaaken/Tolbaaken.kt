package com.q42.tolbaaken

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

enum class TolbaakenLogLevel { Debug, Verbose, Info, Warn, Error }

/**
 * Custom logging for Kotlin
 *
 * Name is inspired by a lighthouse on the island Kotlin
 */
object Tolbaaken {
    var logger: TolbaakenLogger? = null

    inline fun verbose(throwable: Throwable? = null, tag: String? = null, message: () -> Any?) = logger?.let {
        log(it, tag, message(), throwable, TolbaakenLogLevel.Verbose)
    }

    inline fun debug(throwable: Throwable? = null, tag: String? = null, message: () -> Any?) = logger?.let {
        log(it, tag, message(), throwable, TolbaakenLogLevel.Debug)
    }

    inline fun info(throwable: Throwable? = null, tag: String? = null, message: () -> Any?) = logger?.let {
        log(it, tag, message(), throwable, TolbaakenLogLevel.Info)
    }

    inline fun warn(throwable: Throwable? = null, tag: String? = null, message: () -> Any?) = logger?.let {
        log(it, tag, message(), throwable, TolbaakenLogLevel.Warn)
    }

    inline fun error(throwable: Throwable? = null, tag: String? = null, message: () -> Any?) = logger?.let {
        log(it, tag, message(), throwable, TolbaakenLogLevel.Error)
    }

    private val ANONYMOUS_CLASS = "(\\$\\d+)+$".toRegex()
    private val MAX_TAG_LENGTH = 23
    private val CALL_STACK_INDEX = 2

    private fun StackTraceElement.createStackElementTag() =
            className.replace(ANONYMOUS_CLASS, "").substringAfterLast(".").trimTag()

    private fun String.trimTag() = when {
        length > MAX_TAG_LENGTH -> substring(0, MAX_TAG_LENGTH)
        else -> this
    }

    private fun getTagFromStack() = Throwable().stackTrace.getOrNull(CALL_STACK_INDEX)?.createStackElementTag() ?: "Unknown"

    fun log(logger: TolbaakenLogger,
            customTag: String?,
            message: Any?,
            throwable: Throwable?,
            level: TolbaakenLogLevel) {
        val tag = customTag?.trimTag() ?: getTagFromStack()
        logger.log(level, tag, message.toString(), throwable)
    }
}

interface TolbaakenLogger {
    fun log(level: TolbaakenLogLevel, tag: String, message: String, throwable: Throwable?)
}

/**
 * Default logger to use in Android applications
 */
object AndroidTolbaakenLogger : TolbaakenLogger {
    override fun log(level: TolbaakenLogLevel, tag: String, message: String, throwable: Throwable?) {
        val androidLogPriority = level.androidLogPriority
        Log.println(androidLogPriority, tag, formatMessage(message, throwable))
    }

    private val TolbaakenLogLevel.androidLogPriority get() = when (this) {
        TolbaakenLogLevel.Verbose -> Log.VERBOSE
        TolbaakenLogLevel.Debug -> Log.DEBUG
        TolbaakenLogLevel.Info -> Log.INFO
        TolbaakenLogLevel.Warn -> Log.WARN
        TolbaakenLogLevel.Error -> Log.ERROR
    }

    private fun formatMessage(message: String, throwable: Throwable?) = if (throwable != null) {
        "$message\n${throwable.getStackTraceString()}"
    } else {
        message
    }

    private fun Throwable.getStackTraceString(): String {
        // Don't replace this with Log.getStackTraceString() - it hides
        // UnknownHostException, which is not what we want.
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}
