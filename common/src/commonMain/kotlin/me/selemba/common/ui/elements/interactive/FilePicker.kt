package me.selemba.common.ui.elements.interactive

import java.io.File


expect fun FilePicker(initialDirectory: String? = null, onSelected: (List<File>) -> Unit, onAbort: () -> Unit)