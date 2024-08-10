package repo

import org.w3c.xhr.XMLHttpRequest

interface GoogleFormRepo {
    fun insert(
        shareKey : String,
        chunkIndex : Int,
        inputChunk : String
    )
}

class GoogleFormRepoImpl : GoogleFormRepo {

    companion object {
        private const val FORM_SUBMISSION_URL =
            "https://docs.google.com/forms/d/e/1FAIpQLSfYy0ZnzlSot_3SpJ7GVK9umEpf3Dqzz1pQ7jyLUVd7jO2qCQ/formResponse"
    }

    override fun insert(shareKey: String, chunkIndex: Int, inputChunk: String) {
        val data = "entry.1218983684=$shareKey&entry.1886726465=$chunkIndex&entry.1340578003=$inputChunk";
        val xhr = XMLHttpRequest()
        xhr.open("POST", FORM_SUBMISSION_URL)
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        xhr.send(data)
    }

}