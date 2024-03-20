plugins {
    kotlin("plugin.serialization") version "1.9.22"
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false
    id("app.cash.sqldelight") version "2.0.0" apply false
}