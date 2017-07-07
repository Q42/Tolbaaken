# Tolbaaken
A pure Kotlin logging library for Android with zero overhead.

## Motivation
I created this library because I wanted a pure Kotlin logging library with zero overhead in production apps. This project is inspired by [Timber](https://github.com/JakeWharton/timber). 

## Zero overhead
Building a log-message could potentially be a expensive operation, especially if you log objects using the `toString` method:

    Tolbaaken.info { "Log some object: $object" }
    
With Tolbaaken the log-message is computed inside a lambda which is not executed when logging is disabled. Tolbaakens log method are inlined, so there is no overhead of creating a anonymous inner class.

## Usage
In your application do the follwoing to enable logging for debug builds:

    if (BuildConfig.DEBUG) {
      Tolbaaken.logger = AndroidTolbaakenLogger
    }
    
Some debug examples:

    Tolbaaken.debug { "This is a debug message" }
    Tolbaaken.info(tag = "CustomTag") { "This is a info message" }
    
    try {
      // crash and burn
    } catch(e: Exception) {
      Tolbaaken.warn(e) { "Yikes, something went wrong..." }
    }

Disable logging:

    Tolbaaken.logger = null
    
## Download

You can find Tolbaaken on jitPack: https://jitpack.io/#Q42/Tolbaaken/1.0

**Step 1.** Add the JitPack repository to your build file

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
    
**Step 2.** Add the dependency

    dependencies {
	        compile 'com.github.Q42:Tolbaaken:1.0'
	}
	
## Custom logging
Sometimes you want custom log handling, for example logging Exceptions to some crash reporting library.
 
**In your application initialize your custom logger**

    Tolbaaken.logger = CustomCrashReportingLogger

**Custom logger implementation**

    object CustomCrashReportingLogger : TolbaakenLogger {
        override fun log(level: TolbaakenLogLevel, tag: String, message: String, throwable: Throwable?) {
            // Forward logging to the Android logger
            AndroidTolbaakenLogger.log(level, tag, message, throwable)
    
            if(level == TolbaakenLogLevel.Error && throwable != null) {
                // TODO Log to crash reporting...
            }
        }
    }
    
## Library name
This library's name is inspired by the lighthouse on the island [Kotlin](https://www.wikiwand.com/en/Kotlin_Island) which is named "Tolbaaken". 