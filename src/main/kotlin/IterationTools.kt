/* Taken from https://gist.github.com/kiwiandroiddev/fef957a69f91fa64a46790977d98862b */

/**
 * E.g.
 * cartesianProduct(listOf(1, 2, 3), listOf(true, false)) returns
 *  [(1, true), (1, false), (2, true), (2, false), (3, true), (3, false)]
 */
fun <T, U> cartesianProduct(c1: Collection<T>, c2: Collection<U>): List<Pair<T, U>> {
    return c1.flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
}

/*
 * Take from Kotlin Itertools: https://github.com/Lipen/kotlin-itertools
 */

/**
 * Returns all [r]-length subsequences of elements from the iterable.
 * Combinations are emitted lazily in lexicographic sort order.
 */
fun <T> Iterable<T>.combinations(r: Int): Sequence<List<T>> = when {
    r < 0 -> error("r must be non-negative")
    r == 0 -> sequenceOf(emptyList())
    else -> sequence {
        val pool = this@combinations.toList()
        val n = pool.size
        if (r > n) return@sequence
        yield(pool.take(r))
        val indices = IntArray(r) { it }

        while (indices[0] != n - r) {
            var i = r - 1
            while (indices[i] == i + n - r) i--

            indices[i]++
            for (j in (i + 1) until r) {
                indices[j] = indices[j - 1] + 1
            }

            yield(indices.map { pool[it] })
        }
    }
}
