import java.io.File
import java.nio.file._

object Main {

    def main(arguments : Array[String]) : Unit = {

        val postsFile = new File(arguments(0))

        val articleProcessor = new ArticleProcessor("<div>XXXXXXXXX</div>")
        assert(postsFile.exists())
        for(postFile <- postsFile.listFiles().toList if postFile.isDirectory) {
            val articleFile = new File(postFile, "article.md")
            if(articleFile.exists()) {
                val articleInfo = articleProcessor.process(articleFile.toPath)
                println(articleInfo)
            }
        }

    }
}
