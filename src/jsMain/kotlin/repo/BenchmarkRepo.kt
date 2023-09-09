package repo

import components.SavedBenchmarkNode
import components.SavedBenchmarks
import kotlinx.browser.window
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import utils.JsonUtils

interface BenchmarkRepo {
    fun getSavedBenchmarks(): List<SavedBenchmarkNode>
    fun saveBenchmarks(newList: List<SavedBenchmarkNode>)
    fun delete(deletedBenchmarkNode: SavedBenchmarkNode)
}

class BenchmarkRepoImpl : BenchmarkRepo {

    companion object {
        private const val KEY_SAVED_BENCHMARKS = "savedBenchmarks"
    }


    override fun getSavedBenchmarks(): List<SavedBenchmarkNode> {
        val savedBenchmarksString = window.localStorage.getItem(KEY_SAVED_BENCHMARKS)
        val savedBenchmark = if (savedBenchmarksString == null) {
            // Creating first saved benchmark
            SavedBenchmarks(items = listOf())
        } else {
            println("JSON is '$savedBenchmarksString'")
            JsonUtils.json.decodeFromString(savedBenchmarksString)
        }

     /*   if ("${savedBenchmark.items}" == "undefined") {
            saveBenchmarks(listOf())
            return emptyList()
        }*/

        return savedBenchmark.items.toList()
    }

    override fun saveBenchmarks(newList: List<SavedBenchmarkNode>) {
        val savedBenchmarks = JsonUtils.json.encodeToString(SavedBenchmarks(newList))
        window.localStorage.setItem(KEY_SAVED_BENCHMARKS, savedBenchmarks)
    }

    override fun delete(deletedBenchmarkNode: SavedBenchmarkNode) {
        // Appending new benchmark
        val newList = getSavedBenchmarks().toMutableList().apply {
            removeAll { it.key == deletedBenchmarkNode.key }
        }
        saveBenchmarks(newList)
    }

}