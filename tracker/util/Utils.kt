package tracker.util

fun prompt(message: String, default: String? = null): String {
    print("$message ${default?.let { "[$it]" } ?: ""} ")
    val line = readLine()
    return line?.takeIf { it.isNotBlank() } ?: default ?: ""
}
