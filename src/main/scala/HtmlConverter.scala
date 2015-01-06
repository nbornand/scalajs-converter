package ch.bornand.scalatags

import org.jsoup._
import org.jsoup.nodes._
import scala.collection.JavaConversions._
import java.io._

object HtmlConverter {

  /**
   * Reserved scala keyword are surrounded with backquotes
   */
  private def wrapReserved(tag:String):String = {
    val scalaReserved = Set[String]("class","type","object","def","var");
    if(scalaReserved(tag)) s"`$tag`" else tag
  }

  private def quote(s:String) = {
    if(s.contains("\"") || s.contains("\n")) "\"\"\"" + s + "\"\"\""
    else "\"" + s + "\""
  }

  def convertTree(node:Node, depth:Int=0): String = {

    val padding = "\t" * depth
    val res = node match {
      case text: TextNode =>
      {
        if(text.getWholeText.trim() != ""){
          s""""${text.getWholeText.trim()}""""
        } else ""
      }
      case e: Element =>
      {
        val children = node.childNodes()
        val asTags = children.map(convertTree(_, depth + 1)).filter(_.trim() != "")

        //in case the keywork can be both a tag or style/attribute
        val packagePrefix = (if(Supported.ambiguous(e.tagName)) "t." else "")
        val safeTag = wrapReserved(e.tagName())
        s"""\n$padding$packagePrefix$safeTag${convertAttrs(e.attributes())}""" +
          (if(children.length > 0) "(" + asTags.mkString(",") + s"\n$padding)" else "")
      }
      //html comments <!-- -->
      case c:Comment => s"raw(${quote(c.toString.trim())})";
      //the inside of <script> for instance
      case d:DataNode => quote(d.getWholeData.trim())
      case o => "unknown : "+ o.getClass
    }
    res
  }

  def convertAttrs(attrs:Attributes):String = {
    val formatted = attrs.asList().map{  attr =>
      val (key, value) = (attr.getKey, attr.getValue)
      if(Supported.attrs.contains(key)){
        key match {
          //case "class" => s"""cls := "$value""""
          case "style" => CssConverter.convertStyle(value)
          case _ => {
            val safeKey = wrapReserved(key)
            s"$safeKey:=${quote(value.trim())}"
          }
        }
      } else s""""$key".attr:=${quote(value.trim)}"""
    }
    if(formatted.length > 0){
      s"(${formatted.mkString(", ")})"
    }
    else ""
  }

  def convertFile(sourcePath:String, destPath:String): Unit ={
    val source = scala.io.Source.fromFile(sourcePath)
    val merged = source.mkString
    source.close()

    val result = Jsoup.parse(merged)

    //rationale for child(0) is that everyhing is wrapped ing a #root node
    val scalaTags = convertTree(result.child(0))

    val pw = new PrintWriter(new File(destPath))
    pw.write(
      """
        |import scalatags._
        |import scalatags.Text.all._
        |import scalatags.Text.SvgAttrs
        |import CustomBundle._
        |
        |object CustomBundle {
        |  object st extends Text.Cap with Text.Styles with Text.Styles2
        |  object at extends Text.Cap with Text.Attrs
        |  object t extends Text.Cap with text.Tags with text.Tags2 with Text.Aggregate
        |}
        |
        |object TestRes {
        |
        |  val converted =
      """.stripMargin)
    pw.write(scalaTags)
    pw.write("}")
    pw.close
    println("done")
    /*val css =
      """
        |        .widerContainer{
        |        padding:0 30px 0 30px;
        |        max-width:1600px;
        |        margin:auto;
        |        }
        |        .waitingLoad:hover{
        |        opacity:0;
        |        -webkit-transition: opacity 0.8s ease-in;
        |        -moz-transition: opacity 0.8s ease-in;
        |        -ms-transition: opacity 0.8s ease-in;
        |        -o-transition: opacity 0.8s ease-in;
        |        transition:
        |
      """.stripMargin

    val tokens = CssParser.tokenize(css.toStream)
    println(tokens.toList)
    println(CssParser.parse(tokens).toList)*/
  }

  def main(args:Array[String]): Unit = {

    convertFile("source.html", "tags.scala")
    //JSConverter.run
  }

  object CssConverter{

    /**
     * Convert a list of CSS properties
     * @param styleValue "first-property:value1;second-property:value2"
     * @return "firstProperty := "value", secondProperty := "value2"
     */
    def convertStyle(styleValue:String): String ={
      val styles = styleValue.replace(";+",";").split(";").collect{
        case s if s.trim != "" => {
          val split = s.split(":")
          //TODO case when malformed
          val (key, value) = (split(0).trim(), split(1).trim())
          val upCamelKey = "-(\\w)".r.replaceAllIn(key, m => m.group(1).toUpperCase)
          val valueFormated = convertCssValue(value)
          Supported.styles.get(upCamelKey) match {
            case Some(styleAttr) =>  s"""$upCamelKey := $valueFormated"""
            case None =>  s""""$upCamelKey".attr := $valueFormated"""
          }
        }
      }
      styles.mkString(", ")
    }
    /*def convertStyle(styleValue:String): String ={
      def toUpCamel(s:String) = "(\\w)".r.replaceAllIn(s, m => m.group(1).toUpperCase)
      val defs = CssParser.parseDefinitions(CssParser.tokenize(styleValue.toStream))._1
      defs.map( pair => {
        pair
        val key = toUpCamel(pair.property)
        val valueFormated = convertCssValue(pair.value)
        Supported.styles.get(key) match {
          case Some(styleAttr) =>  s"""$key := $valueFormated"""
          case None =>  s""""$key".attr := $valueFormated"""
        }
      }).mkString(", ")
    }*/

    def convertCssValue(value:String) : String = {
      val numberReg = "(-?\\d+(:?\\.\\d*)?)";
      //TODO use reflection on DataTypes instead of hardcode
      //TODO percent case not working
      val extensions = Set("px", "pt", "mm", "cm", "in", "pc", "em", "ch", "ex", "rem", "deg","grad", "rad", "turn", "%")
      value match {
        case number if number.matches(numberReg+".*") => {
          (numberReg+"([^\\d]+)").r.findFirstMatchIn(number) match{
            case Some(m) => {
              if(extensions(m.group(3))){
                //negative numbers must be surrounded by parentheses
                (if(m.group(1).startsWith("-")) s"(${m.group(1)})" else m.group(1)) +
                  "." + m.group(3)
              }
              else "\"" + value + "\""
            }
            case None => value
          }
        }
        case _ => "\"" + value + "\""
      }
    }

  }

  object CssParser{

    /**
     * Lexer
     */

    //TODO add position to give feedback to the user
    object Tokens {
      sealed abstract class Token(val tpe:String){
        override def toString:String = tpe
      }
      case object LBracket extends Token("{")
      case object RBracket extends Token("}")
      case object Colon extends Token(":")
      case object SemiColon extends Token(";")
      case class TextBlock(content:String) extends Token("text"){
        override def toString:String = content
      }
    }
    import Tokens._

    private val delimiter = Map('{' -> LBracket, '}' -> RBracket, ':' -> Colon, ';' -> SemiColon)

    def tokenize(cssContent:Stream[Char]):Stream[Token] = {
      if(cssContent.isEmpty) Stream[Token]()
      else cssContent.head match {
        case c if delimiter.contains(c) => delimiter(c) #:: tokenize(cssContent.drop(1))
        case space if space.isWhitespace => tokenize(cssContent.drop(1))
        case _ => {
          val textBlock = cssContent.takeWhile(c => !delimiter.contains(c)).mkString
          TextBlock(textBlock.trim()) #:: tokenize(cssContent.drop(textBlock.length))
        }
      }
    }

    def reportError(remainingTokens : Stream[Token], message:String): Unit = {
      println(message)
      println("Occured around : \"" + remainingTokens.take(20).mkString + "\"")
      System.exit(1);
    }

    /**
     * Parser
     */

    object CssAST{
      case class CssPair(property:String, value:String)
      case class Selector(name:String, pairs: List[CssPair])
    }
    import CssAST._

    private def assert(tokens:Stream[Token], expected:String*) = {
      if(tokens.length < expected.length) reportError(tokens, s"expected (${expected.mkString(")(")}) but end of stream reached")
      tokens.zip(expected).filter{ case (token,tpe) => token.tpe != tpe}.foreach{
        case (token,tpe) => reportError(tokens, s"$tpe was expected, got type $token")
      }
    }
    def parse(tokens:Stream[Token]): Stream[Selector] = tokens match{
      case Stream() =>  Stream[Selector]()
      case o => {
        assert(tokens, "text")
        val idParts = tokens.takeWhile(t => t.tpe != "{")
        assert(tokens.drop(idParts.length), "{")
        val (pairs, newStream) = parseDefinitions(tokens.drop(idParts.length+1))
        //TODO handle invalid id
        val idOrClass = Selector(idParts.map(_.toString).mkString, pairs)
        assert(newStream,"}")
        idOrClass #:: parse(newStream.drop(1))
      }
    }
    def parseDefinitions(tokens:Stream[Token]):(List[CssPair], Stream[Token]) = tokens match {
      case Stream() => (Nil, tokens)
      case SemiColon #:: rest => parseDefinitions(tokens.drop(1))
      case RBracket #:: rest => (Nil, tokens)
      case _ => {
        assert(tokens,"text", ":", "text")
        val pair = buildCssPair(tokens.get(0).asInstanceOf[TextBlock].content, tokens.get(2).asInstanceOf[TextBlock].content)
        val (pairs, newStream) = parseDefinitions(tokens.drop(3))
        (pair :: pairs, newStream)
      }
    }
    private def buildCssPair(key:String, value:String) = CssPair(key, value)
  }
}
