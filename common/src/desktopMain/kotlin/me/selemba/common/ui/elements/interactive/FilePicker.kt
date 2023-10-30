package me.selemba.common.ui.elements.interactive

import me.selemba.fileDialog.FileDialog
import me.selemba.fileDialog.FilePattern
import java.io.File


actual fun FilePicker(initialDirectory: String?, onSelected: (List<File>) -> Unit, onAbort: () -> Unit) {
    FileDialog.fileOpenDialogMulti("Dateien auswählen.", filePattern = FilePattern.AudioFiles, callback = onSelected, callbackAbort = onAbort)
}