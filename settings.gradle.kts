pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    // Definición de las versiones de los PLUGINS
    plugins {
        id("com.android.application") version "8.2.0"
        id("org.jetbrains.kotlin.android") version "1.9.22"
        id("com.google.devtools.ksp") version "1.9.22-1.0.17"
        id("com.google.dagger.hilt.android") version "2.50"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "com.mod6.evalfinal_inventarioapi"
include(":app")