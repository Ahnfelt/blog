import $ivy.`com.vladsch.flexmark:flexmark-all:0.61.30`

import java.nio.file.{Files, Path, Paths}
import java.util

import com.vladsch.flexmark.ast.{Heading, Image, Paragraph}
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.typographic.TypographicExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.{Document, TextCollectingVisitor}
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.util.misc.Extension

import scala.util.matching.Regex

class ArticleProcessor(authorHtmlTemplate : String) {

    private val options = new MutableDataSet()

    options.set(Parser.EXTENSIONS, util.Arrays.asList[Extension](
        TablesExtension.create(),
        StrikethroughExtension.create(),
        TypographicExtension.create(),
    ))

    options.set(HtmlRenderer.GENERATE_HEADER_ID, java.lang.Boolean.TRUE)
    options.set(HtmlRenderer.RENDER_HEADER_ID, java.lang.Boolean.TRUE)
    options.set(HtmlRenderer.HEADER_ID_GENERATOR_NO_DUPED_DASHES, java.lang.Boolean.TRUE)

    private val parser = Parser.builder(options).build()
    private val renderer = HtmlRenderer.builder(options).build()

    def process(path : Path) : ArticleInfo = {
        val markdown = new String(Files.readAllBytes(path), "UTF-8")
        val document = parser.parse(markdown)
        val (title, image, teaser) = extractMeta(document)
        val documentHtml = renderer.render(document)
        val publicationDate = None
        val authorHtml = authorHtmlTemplate.replace("$date$", "Draft")
        val html = documentHtml.replaceFirst("</h1>", "</h1>\n" + Regex.quoteReplacement(authorHtml))
        ArticleInfo(
            articleHtml = html,
            title = Some(title.trim).filter(_.nonEmpty),
            imageUrl = Some(image.trim).filter(_.nonEmpty),
            teaser = Some(teaser.trim).filter(_.nonEmpty),
            publicationDate = publicationDate
        )
    }

    private def extractMeta(document : Document) : (String, String, String) = {
        val visitor = new TextCollectingVisitor()
        document.getFirstChild match {
            case heading : Heading =>
                val (imageText, imageParagraph) = heading.getNext match {
                    case paragraph : Paragraph =>
                        paragraph.getFirstChild match {
                            case image : Image =>
                                image.getUrl.toString -> Some(paragraph)
                            case _ =>
                                "" -> None
                        }
                    case _ =>
                        "" -> None
                }
                val teaserText = imageParagraph.getOrElse(heading).getNext match {
                    case paragraph : Paragraph =>
                        visitor.collectAndGetText(paragraph)
                    case _ =>
                        ""
                }
                (visitor.collectAndGetText(heading), imageText, teaserText)
            case _ =>
                ("", "", "")
        }
    }

}

case class ArticleInfo(
    articleHtml : String,
    title : Option[String],
    imageUrl : Option[String],
    teaser : Option[String],
    publicationDate : Option[String]
)
