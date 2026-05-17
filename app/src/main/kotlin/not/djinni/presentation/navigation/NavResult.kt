package not.djinni.presentation.navigation

interface NavResultContract<T> {
    fun encode(value: T): String
    fun decode(value: String): T
}

data class NavResultKey<T>(
    val id: String,
    val contract: NavResultContract<T>,
)

class NavResultStore internal constructor(
    pendingResults: Map<String, String> = emptyMap(),
) {
    private val results = pendingResults.toMutableMap()

    fun put(id: String, value: String) {
        results[id] = value
    }

    fun consume(id: String): String? = results.remove(id)

    fun snapshot(): Map<String, String> = results.toMap()
}
