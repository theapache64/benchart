import org.w3c.xhr.XMLHttpRequest
import repo.GoogleFormRepoImpl
import kotlin.test.Test

class GoogleSheetRepoTest {

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
