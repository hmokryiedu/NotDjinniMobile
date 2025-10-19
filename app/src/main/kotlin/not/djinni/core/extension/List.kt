package not.djinni.core.extension

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

inline fun <reified T, R> Collection<T>?.mapToImmutable(
    action: (T) -> R,
): ImmutableList<R> {
    if (this == null) return persistentListOf()
    val persistentListBuilder = persistentListOf<R>().builder()
    for (item in this) {
        persistentListBuilder.add(action(item))
    }
    return persistentListBuilder.build()
}

inline fun <reified T, R> List<T>?.mapToImmutableNotNull(
    action: (T) -> R?,
): ImmutableList<R> {
    if (this == null) return persistentListOf()
    val persistentListBuilder = persistentListOf<R>().builder()
    for (item in this) {
        persistentListBuilder.add(action(item) ?: continue)
    }
    return persistentListBuilder.build()
}

inline fun <reified T, R> List<T>?.mapToImmutableIndexed(
    action: (Int, T) -> R,
): ImmutableList<R> {
    if (this == null) return persistentListOf()
    val persistentListBuilder = persistentListOf<R>().builder()
    forEachIndexed { index, item ->
        val mappedItem = action(index, item)
        persistentListBuilder.add(mappedItem)
    }
    return persistentListBuilder.build()
}

inline fun <reified T> List<T>?.filterImmutable(
    predicate: (T) -> Boolean,
): ImmutableList<T> {
    if (this == null) return persistentListOf()
    val persistentListBuilder = persistentListOf<T>().builder()
    forEach { item -> if (predicate(item)) persistentListBuilder.add(item) }
    return persistentListBuilder.build()
}

inline fun <reified T> buildImmutableList(
    action: PersistentList.Builder<T>.() -> Unit,
): ImmutableList<T> {
    val builder = persistentListOf<T>().builder()
    builder.action()
    return builder.build()
}

inline fun <reified T> ImmutableList(
    size: Int,
    provider: (Int) -> T
): ImmutableList<T> {
    val builder = persistentListOf<T>().builder()
    repeat(size) { builder.add(provider(it)) }
    return builder.build()
}