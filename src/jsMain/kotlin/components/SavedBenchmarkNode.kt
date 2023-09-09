package components

import kotlinx.serialization.Serializable

@Serializable
data class SavedBenchmarkNode(
    val key : String,
    val value : String
)

@Serializable
data class SavedBenchmarks(
    var items : List<SavedBenchmarkNode>
)