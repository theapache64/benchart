package repo

import components.SavedBenchmarkNode
import components.SavedBenchmarks
import kotlinx.browser.window
import page.home.HomeViewModel

interface BenchmarkRepo {
    fun getSavedBenchmarks(): List<SavedBenchmarkNode>
}

class BenchmarkRepoImpl : BenchmarkRepo {

    companion object {
        val KEY_SAVED_BENCHMARKS = "savedBenchmarks"
    }

    override fun getSavedBenchmarks(): List<SavedBenchmarkNode> {
        val savedBenchmarksString = window.localStorage.getItem(KEY_SAVED_BENCHMARKS)
        println(savedBenchmarksString)
        val savedBenchmark = if (savedBenchmarksString == null) {
            // Creating first saved benchmark
            SavedBenchmarks(items = arrayOf())
        } else {
            JSON.parse(savedBenchmarksString)
        }

        return savedBenchmark.items.toList()
    }

}