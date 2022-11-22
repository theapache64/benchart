inline fun <T : Any> jso(): T = js("({})")

inline fun <T : Any> jso(builder: T.() -> Unit): T = jso<T>().apply(builder)