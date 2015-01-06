/**
 * Created by Nicolas on 22.12.2014.
 */
class Css {

}

case class Selector(l:Seq[String])

object Selector{
  def sharp(id:String):Selector = Selector(id :: Nil)
}
