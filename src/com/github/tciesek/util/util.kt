package com.github.tciesek.util

import java.io.File

fun String.toRelativeFile() = File(
    listOf("src", currentPackage().replace(".", File.separator), this).joinToString(File.separator)
)

private fun currentPackage(): String {
    val stacktrace = Throwable().stackTrace
    return stacktrace[stacktrace.size - 1].className.substringBeforeLast(".")
}
