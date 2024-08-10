import org.w3c.xhr.XMLHttpRequest
import repo.GoogleFormRepoImpl
import kotlin.test.Test

class GoogleFormRepoTest {


    private val googleFormRepo = GoogleFormRepoImpl()

    @Test
    fun writeDataToSheet() {
        googleFormRepo.insert(
            shareKey = "myShareKey",
            chunkIndex = 0,
            inputChunk = "iamTheInput"
        )
    }

}
