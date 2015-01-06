package ch.bornand.scalatags

import org.jsoup.Jsoup

import collection.mutable.Stack
import org.scalatest._
import scala.tools.reflect.ToolBox

import scalatags._
import scalatags.Text.all._
import scalatags.Text.SvgAttrs
import CustomBundle._
import scala.tools.nsc.interpreter.IMain
import scala.tools.nsc.Settings


object CustomBundle {
  object st extends Text.Cap with Text.Styles with Text.Styles2
  object at extends Text.Cap with Text.Attrs
  object t extends Text.Cap with text.Tags with text.Tags2 with Text.Aggregate
}

class FullConversionTest extends FlatSpec with Matchers {

  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    stack.pop() should be (2)
    stack.pop() should be (1)
    //val res = in.compileString("case class Test(s:String)");
    //println(res)

/*    val source = scala.io.Source.fromFile("source.html")
    val merged = source.mkString
    source.close()

    val result = Jsoup.parse(merged)
    val scalaTags = HtmlConverter.convertTree(result.child(0))

    val parsed = toolbox.parse(scalaTags+".render")
    println(toolbox.eval(parsed))*/
    val in = new IMain(new Settings)
    in.compiled("val x:Int = 2")
}

it should "throw NoSuchElementException if an empty stack is popped" in {
val emptyStack = new Stack[Int]
a [NoSuchElementException] should be thrownBy {
emptyStack.pop()
}
}
}