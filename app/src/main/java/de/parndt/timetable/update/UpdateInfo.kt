package de.parndt.timetable.update

data class UpdateInfo(
    private val versionName: String,
    val downloadUrl: String,
    val experimental: Boolean
) {

    fun getVersionNumber(): Int {
        return convertVersionNameToNumber()
    }

    private fun convertVersionNameToNumber(): Int {
        return versionName.replace(".", "").toInt()
    }

}

