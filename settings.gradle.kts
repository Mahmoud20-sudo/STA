/*pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
//        maven  ("https://maven.fullstory.com" )
        maven (  "https://developer.huawei.com/repo/" )
//        maven (  "https://jitpack.io" )
    }
}
*/



pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        maven ("https://developer.huawei.com/repo/")
        maven  ("https://oss.sonatype.org/content/repositories/snapshots/" )
        maven ("https://jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven ("https://developer.huawei.com/repo/")
        maven  ("https://oss.sonatype.org/content/repositories/snapshots/" )
        maven ("https://jitpack.io")
    }
}

rootProject.name = "Employee"
include(":app")