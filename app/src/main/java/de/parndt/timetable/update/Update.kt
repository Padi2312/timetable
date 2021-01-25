package de.parndt.timetable.update

data class Update(val version: String, val download: String) {
    fun getVersion(): Int {
        return version.replace(".", "").toInt()
    }
}
