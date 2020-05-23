import java.util

import com.vladsch.flexmark.ast.{Heading, HtmlBlock, Image, Paragraph}
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.typographic.TypographicExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.parser.block.{NodePostProcessor, NodePostProcessorFactory}
import com.vladsch.flexmark.util.ast.{Document, Node, NodeTracker, TextCollectingVisitor}
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.util.misc.Extension
import com.vladsch.flexmark.util.sequence.BasedSequence

import scala.util.matching.Regex

object Main {

    def main(arguments : Array[String]) : Unit = {

        val options = new MutableDataSet()

        options.set(Parser.EXTENSIONS, util.Arrays.asList[Extension](
            TablesExtension.create(),
            StrikethroughExtension.create(),
            TypographicExtension.create(),
        ))

        options.set(HtmlRenderer.GENERATE_HEADER_ID, java.lang.Boolean.TRUE)
        options.set(HtmlRenderer.RENDER_HEADER_ID, java.lang.Boolean.TRUE)
        options.set(HtmlRenderer.HEADER_ID_GENERATOR_NO_DUPED_DASHES, java.lang.Boolean.TRUE)

        val parser = Parser.builder(options).build()
        val renderer = HtmlRenderer.builder(options).build()


        val document = parser.parse("# foo *bar*\n\nThis is *Sparta*\n\n## Yahoo --- there\n\nHello")
        val (title, image, teaser) = extractMeta(document)
        println((title, image, teaser))
        val documentHtml = renderer.render(document)
        val authorHtml = "<div>XXXXXXXXX</div>"
        val html = documentHtml.replaceFirst("</h1>", "</h1>\n" + Regex.quoteReplacement(authorHtml))
        println(html)

    }

    def extractMeta(document : Document) : (String, String, String) = {
        val visitor = new TextCollectingVisitor()
        document.getFirstChild match {
            case heading : Heading =>
                val (imageText, imageParagraph) = heading.getNext match {
                    case paragraph : Paragraph =>
                        paragraph.getFirstChild match {
                            case image : Image =>
                                image.getUrlContent.toString -> Some(paragraph)
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
