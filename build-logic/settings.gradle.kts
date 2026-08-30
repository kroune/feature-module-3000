// Optional AGP override for the measure-agp-commits workflow: build-logic is where AGP
// actually enters the build (implementation(libs.android.gradle.plugin)), so both the
// repository and the catalog version must be overridable here.
val agpOverrideRepoUrl: String? = providers.gradleProperty("agpOverrideRepoUrl").orNull
val agpOverrideVersion: String? = providers.gradleProperty("agpOverrideVersion").orNull

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        if (agpOverrideRepoUrl != null) {
            maven { url = uri(agpOverrideRepoUrl) }
        }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
            if (agpOverrideVersion != null) {
                version("agp", agpOverrideVersion)
            }
        }
    }
}
rootProject.name = "build-logic"
include(":convention")
