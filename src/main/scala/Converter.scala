package ch.bornand.scalatags

import org.jsoup._
import org.jsoup.nodes._
import scala.collection.JavaConversions._

object Converter {

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
      case c:Comment => quote(c.toString.trim())
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
          case "style" => convertStyle(value)
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
        "".matches("\\d*(.\\d+)?")
        s"""$upCamelKey := "$value""""
      }
    }
    styles.mkString(", ")
  }

  def main(args:Array[String]): Unit = {

    val source = scala.io.Source.fromFile("source.html")
    val merged = source.mkString
    source.close()

    val result = Jsoup.parse(merged)

    val builder = new StringBuilder()
    //rationale for child(0) is that everyhing is wrapped ing a #root node
    val scalaTags = convertTree(result.child(0))
    //println(scalaTags)
    println(TestRes.converted.render)

  }
}
