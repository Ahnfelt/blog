import java.util

import com.vladsch.flexmark.ast.{Heading, HtmlBlock}
import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.typographic.TypographicExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.parser.block.{NodePostProcessor, NodePostProcessorFactory}
import com.vladsch.flexmark.util.ast.{Document, Node, NodeTracker}
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

        val document = parser.parse("# foo\n\nThis is *Sparta*\n\n## Yahoo --- there\n\nHello")
        val title = document.getFirstChild match { case heading : Heading => heading.getText.toString; case _ => "" }
        val documentHtml = renderer.render(document)
        val authorHtml = "<div>XXXXXXXXX</div>"
        val html = documentHtml.replaceFirst("</h1>", "</h1>\n" + Regex.quoteReplacement(authorHtml))
        println(html)

    }

}
