package repo

import org.w3c.xhr.XMLHttpRequest

interface GoogleSheetRepo {
    fun getChunkSize(
        shareKey: String,
        onChunkSize: (chunkSize: Int) -> Unit,
        onFailed: (reason: String) -> Unit
    )

    fun getSharedInput(
        shareKey: String,
        onSharedInput: (input: String) -> Unit,
        onFailed: (reason: String) -> Unit
    )
}

class GoogleSheetRepoImpl : GoogleSheetRepo {
    companion object {
        private const val BASE_URL =
            "https://docs.google.com/spreadsheets/d/1U1bKMHN0hlpZ1CVke3TB3-Xc20ZJwZxlMWYXpMcII-k/gviz/tq?tqx=out:csv&sheet=Sheet1"
    }

    override fun getChunkSize(
        shareKey: String,
        onChunkSize: (chunkSize: Int) -> Unit,
        onFailed: (reason: String) -> Unit
    ) {
        try {
            val chunkCountUrl = "$BASE_URL&tq=SELECT COUNT(C) WHERE B = '$shareKey'"
            val xhr = XMLHttpRequest()
            xhr.open("GET", chunkCountUrl)
            xhr.onreadystatechange = { _ ->
                println("QuickTag: GoogleSheetRepoImpl:getChunkCount: readyState: ${xhr.readyState}, status = ${xhr.status}")
                if (xhr.readyState == 4.toShort()) {
                    if (xhr.status == 200.toShort()) {
                        val responseLines = xhr.responseText.split("\n")
                        if (responseLines.size == 2) {
                            // chunk exist
                            val chunkSize = responseLines[1].replace("\"", "").toInt()
                            onChunkSize(chunkSize)
                        } else {
                            // share doesn't exist
                            onFailed("No chunk exist for shareKey '$shareKey'")
                        }
                    } else {
                        onFailed("Share request failed")
                    }
                }
            }
            xhr.send()
        } catch (e: Throwable) {
            onFailed(e.message ?: "Something wrong")
        }
    }

    override fun getSharedInput(
        shareKey: String,
        onSharedInput: (input: String) -> Unit,
        onFailed: (reason: String) -> Unit
    ) {
        try {
            val chunkCountUrl = "$BASE_URL&tq=SELECT C,D WHERE B = '$shareKey' ORDER BY C"
            val xhr = XMLHttpRequest()
            xhr.open("GET", chunkCountUrl)
            xhr.onreadystatechange = { _ ->
                println("QuickTag: GoogleSheetRepoImpl:getChunkCount: readyState: ${xhr.readyState}, status = ${xhr.status}")
                if (xhr.readyState == 4.toShort()) {
                    if (xhr.status == 200.toShort()) {
                        val responseLines = xhr.responseText
                        val firstLineBreakIndex = responseLines.indexOf('\n')
                        if (firstLineBreakIndex != -1) {
                            val sharedInput = responseLines
                                .substring(firstLineBreakIndex+1, responseLines.length - 1)
                                .replace("\"\\n\"(?:\\d+)\",\"".toRegex(),"")
                                .substring(5)
                            onSharedInput(sharedInput)
                        } else {
                            onFailed("Invalid shareKey '$shareKey'")
                        }
                        console.log("index is $firstLineBreakIndex")
                        console.log("lines: '$responseLines'")
                        /*if (responseLines.size == 2) {
                            // chunk exist
                            val chunkSize = responseLines[1].replace("\"", "").toInt()
                            onChunkSize(chunkSize)
                        } else {
                            // share doesn't exist
                            onFailed("No chunk exist for shareKey '$shareKey'")
                        }*/
                    } else {
                        onFailed("Share request failed")
                    }
                }
            }
            xhr.send()
        } catch (e: Throwable) {
            onFailed(e.message ?: "Something wrong")
        }
    }
}