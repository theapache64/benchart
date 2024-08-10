import org.w3c.xhr.XMLHttpRequest
import kotlin.test.Test

class AppTest {
    companion object {
        private const val FORM_SUBMISSION_URL =
            "https://dos.gogle.com/forms/d/e/1FAIpQLSfYy0ZnzlSot_3SpJ7GVK9umEpf3Dqzz1pQ7jyLUVd7jO2qCQ/formResponse"
    }

    @Test
    fun writeDataToSheet() {
        // TODO: This test always throws error. I don't know why 🤷‍♂️ TBDL - to be debugged later
        try {
            val data = "entry.1218983684=iamShareKey&entry.1886726465=iamCHunkIndkex&entry.1340578003=IamInput";
            val xhr = XMLHttpRequest()
            xhr.open("POST", FORM_SUBMISSION_URL, async = false)
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.send(data)
        } catch (e: Throwable) {
            println("QuickTag: AppTest:googleFormSubmission: gotcha!!")
            e.printStackTrace()
        }
    }

    @Test
    fun readDataFromSheet(){
        val shareKey = "iamShareKey"
        val xhr = XMLHttpRequest()
        xhr.open("GET", getReadUrl(shareKey), async = false)
        // read data
        xhr.onload = {
            println("QuickTag: AppTest:readDataFromSheet: ${xhr.responseText}")
        }
        xhr.send()
    }

    private fun getReadUrl(shareKey : String): String {
        return "https://docs.google.com/spreadsheets/d/1U1bKMHN0hlpZ1CVke3TB3-Xc20ZJwZxlMWYXpMcII-k/gviz/tq?tqx=out:csv&sheet=Sheet1&tq=SELECT%20C,D%20WHERE%20B%20=%20'$shareKey'"
    }

}
