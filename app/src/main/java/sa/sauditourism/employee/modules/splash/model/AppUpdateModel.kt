package sa.sauditourism.employee.modules.splash.model

data class AppUpdateModel(
    val forceUpdateEnabled: Boolean,
    val version: String
) {
    constructor() : this(false, "")

    fun shouldUpdateApp(appVersion: String): Boolean {
        if (version.isEmpty()) {
            return false
        }
        try {
            val currentVersionSplit = appVersion.split(".").map { it.toIntOrNull() ?: 0 }
            val dbVersionSplit = version.split(".").map { it.toIntOrNull() ?: 0 }

            // Compare major version
            if (currentVersionSplit[0] < dbVersionSplit[0]) return true
            if (currentVersionSplit[0] > dbVersionSplit[0]) return false

            // Compare minor version
            if (currentVersionSplit[1] < dbVersionSplit[1]) return true
            if (currentVersionSplit[1] > dbVersionSplit[1]) return false

            // Compare patch version
            return currentVersionSplit[2] < dbVersionSplit[2]
        } catch (ignored: Exception) {
            return false
        }
    }
}
