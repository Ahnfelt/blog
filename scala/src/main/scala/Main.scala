import java.util

import com.vladsch.flexmark.ast.{Heading, HtmlBlock}
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.parser.block.{NodePostProcessor, NodePostProcessorFactory}
import com.vladsch.flexmark.util.ast.{Document, Node, NodeTracker}
import com.vladsch.flexmark.util.data.MutableDataSet
import com.vladsch.flexmark.util.misc.Extension
import com.vladsch.flexmark.util.sequence.BasedSequence

object Main {
    def main(arguments : Array[String]) : Unit = {

        val options = new MutableDataSet()

        options.set(Parser.EXTENSIONS, util.Arrays.asList[Extension](
            TablesExtension.create(),
            StrikethroughExtension.create()
        ))

        val parser = Parser.builder(options).postProcessorFactory(new AuthorPostProcessorFactory()).build()
        val renderer = HtmlRenderer.builder(options).build()

        val document = parser.parse("# foo\n\nThis is *Sparta*\n\nHello")
        val html = renderer.render(document)
        println(html)

    }
}

class AuthorPostProcessor() extends NodePostProcessor {
    var title = ""
    override def process(state : NodeTracker, node : Node) : Unit = {
        node match {
            case heading : Heading if heading.getPrevious == null =>
                title = heading.getText.toString
                val authorNode = new HtmlBlock(BasedSequence.of("<div>XXXXXXXXXXX</div>"))
                authorNode.insertAfter(heading)
                state.nodeAddedWithChildren(authorNode)
        }
    }
}

class AuthorPostProcessorFactory() extends NodePostProcessorFactory(false) {
    addNodes(classOf[Heading])
    override def apply(document : Document) = new AuthorPostProcessor()
}
