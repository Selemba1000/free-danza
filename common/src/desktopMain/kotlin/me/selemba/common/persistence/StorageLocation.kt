package me.selemba.common.persistence

actual object StorageLocationImpl {

    private fun getOS():OS{
        val tmp = System.getProperty("os.name").lowercase()
        for (os in OS.values()){
            if(os.matcher(tmp))return os
        }
        return OS.UNSPECIFIED
    }

    actual val userDataLocation: String = when(getOS()){
        OS.WINDOWS -> System.getenv("LOCALAPPDATA");
        OS.LINUX -> ""
        OS.MAC -> ""
        //TODO
        else -> ""
    }
    enum class OS(val matcher: (String)->Boolean) {
        WINDOWS({it.contains("win")}),
        MAC({it.contains("mac")}),
        LINUX({it.contains(Regex("(nix)|(aix)|(nux)"))}),
        UNSPECIFIED({false})
    }
}

