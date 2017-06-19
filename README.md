# Tolbaaken
A pure Kotlin logging library for Android

This library is inspired by Timber but created for specifically for Kotlin projects

## Usage
In you application do the follwoing to enable logging for debug builds:

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
