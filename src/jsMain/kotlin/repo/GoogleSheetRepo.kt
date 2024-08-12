package repo

import org.w3c.xhr.XMLHttpRequest

interface GoogleSheetRepo {
    fun getChunkSize(
        shareKey: String,
        onChunkSize: (chunkSize: Int) -> Unit,
        onFailed: (reason: String) -> Unit
    )
}

class GoogleSheetRepoImpl : GoogleSheetRepo {
    companion object {
        private const val BASE_URL =
            "https://docss.google.com/spreadsheets/d/1U1bKMHN0hlpZ1CVke3TB3-Xc20ZJwZxlMWYXpMcII-k/gviz/tq?tqx=out:csv&sheet=Sheet1"
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
                if (xhr.readyState == 4.toShort() ) {
                    if(xhr.status == 200.toShort()){
                        val responseLines = xhr.responseText.split("\n")
                        if (responseLines.size == 2) {
                            // chunk exist
                            val chunkSize = responseLines[1].replace("\"", "").toInt()
                            onChunkSize(chunkSize)
                        } else {
                            // share doesn't exist
                            onFailed("No chunk exist for shareKey '$shareKey'")
                        }
                    }else{
                        onFailed("Share request failed")
                    }
                }
            }
            xhr.send()
        }catch (e: Throwable){
            onFailed(e.message ?: "Something wrong")
        }
    }
}