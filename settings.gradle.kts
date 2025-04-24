pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven (url = "https://maven.google.com/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "MotoT"
include(":app")
include(":ratingbrand")
include(":userprofile")
include(":core")
