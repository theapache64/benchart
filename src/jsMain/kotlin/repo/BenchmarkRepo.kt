package repo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import components.SavedBenchmarkNode
import components.SavedBenchmarks
import kotlinx.browser.window

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
            SavedBenchmarks(items = arrayOf())
        } else {
            println("JSON is '$savedBenchmarksString'")
            JSON.parse<SavedBenchmarks>(savedBenchmarksString)
        }

        println("savedBenchmark is `$savedBenchmark`")
        println("savedBenchmarkList is ${savedBenchmark.items}")
        println("savedBenchmarkList toList is ${savedBenchmark.items.toList()}")

        return savedBenchmark.items.toList()
    }

    override fun saveBenchmarks(newList: List<SavedBenchmarkNode>) {
        val savedBenchmarks = JSON.stringify(SavedBenchmarks(newList.toTypedArray()))
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