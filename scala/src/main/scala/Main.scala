import java.io.File
import java.nio.file._

import scala.util.matching.Regex

object Main {

    def main(arguments : Array[String]) : Unit = {

        val templatesFile = new File(arguments(1))

        val authorTemplateFile = new File(templatesFile, "post-author.html")
        val authorTemplate = new String(Files.readAllBytes(authorTemplateFile.toPath), "UTF-8")

        val postTemplateFile = new File(templatesFile, "post.html")
        val postTemplate = new String(Files.readAllBytes(postTemplateFile.toPath), "UTF-8")

        val defaultTemplateFile = new File(templatesFile, "default.html")
        val defaultTemplate = new String(Files.readAllBytes(defaultTemplateFile.toPath), "UTF-8")

        val articleProcessor = new ArticleProcessor(authorTemplate)

        val postsFile = new File(arguments(0))
        assert(postsFile.exists())
        for(postFile <- postsFile.listFiles().toList if postFile.isDirectory) {
            val articleFile = new File(postFile, "article.md")
            if(articleFile.exists()) {
                val articleInfo = articleProcessor.process(articleFile.toPath)
                val articleHtml = postTemplate.replace("$body$", articleInfo.articleHtml)
                val articleTitle = articleInfo.title.getOrElse("").
                    replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;")
                val pageHtml = defaultTemplate.replace("$body$", articleHtml).replace("$title$", articleTitle)
                println(pageHtml)
            }
        }

    }
}
