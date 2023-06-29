package me.selemba.common.persistence

actual object StorageLocation {

    private fun getOS():OS{
        val tmp = System.getProperty("os.name").lowercase()
        for (os in OS.values()){
            if(os.matcher(tmp))return os
        }
        return OS.UNSPECIFIED
    }

    actual val userDataLocation: String = when(getOS()){
        OS.WINDOWS -> ""
        OS.LINUX -> ""
        OS.MAC -> ""
        else -> ""
    }
    enum class OS(val matcher: (String)->Boolean) {
        WINDOWS({it.contains("win")}),
        MAC({it.contains("mac")}),
        LINUX({it.contains(Regex("(nix)|(aix)|(nux)"))}),
        UNSPECIFIED({false})
    }
}
