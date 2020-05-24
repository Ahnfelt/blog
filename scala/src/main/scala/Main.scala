import java.io.File
import java.nio.file._

object Main {

    def main(arguments : Array[String]) : Unit = {

        val templatesFile = new File(arguments(1))
        val authorTemplateFile = new File(templatesFile, "post-author.html")
        val authorTemplate = new String(Files.readAllBytes(authorTemplateFile.toPath), "UTF-8")

        val postsFile = new File(arguments(0))

        val articleProcessor = new ArticleProcessor(authorTemplate)
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
