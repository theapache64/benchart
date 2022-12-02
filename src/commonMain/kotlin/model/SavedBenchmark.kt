package model


data class BenchmarkNode(
    val key : String,
    val value : String
)

data class SavedBenchmarks(
    var items : Array<BenchmarkNode>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SavedBenchmarks

        if (!items.contentEquals(other.items)) return false

        return true
    }

    override fun hashCode(): Int {
        return items.contentHashCode()
    }
}