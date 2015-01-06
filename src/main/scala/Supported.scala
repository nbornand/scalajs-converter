package ch.bornand.scalatags

import reflect.runtime.universe._
import scalatags.text._
import scala.collection.{mutable => mut}

/**
 * Gather sets of tags, names and style that are "natively" supported by scalatags
 * It uses reflection such as to be able to cope with small changes
 */
object Supported {

  val tags = supportedTags
  val attrs = supportedAttrs
  val styles = supportedStyles

  /**
   * Some keywords such as "style" can be both tag or attributes
   */
  val ambiguous = (tags & attrs) ++ (tags & styles.keySet) ++ (attrs & styles.keySet)

  private def supportedTags: Set[String] = {

    val typeToInspect = List(typeOf[scalatags.generic.Tags[_, _, _]], typeOf[scalatags.generic.Tags2[_, _, _]])

    val supported = typeToInspect.flatMap( t => t.members.collect{
      case m if m.typeSignature.baseClasses.headOption.isDefined
        //TODO fix dirty workaround
        && (m.typeSignature.baseClasses.head.name.toString equals "TypedTag") => m.name.toString.trim()
    })
    supported.toSet[String]

  }

    private def supportedAttrs: Set[String] = {

      val typeToInspect = List(typeOf[scalatags.generic.Attrs[_,_,_]], typeOf[scalatags.generic.SvgAttrs[_,_,_]])
      val supported = typeToInspect.flatMap( t => t.members.collect{
        case m if(m.typeSignature =:= typeOf[scalatags.generic.Attr]) => {
          m.name.toString.trim()
        }
      })
      supported.toSet[String]

    }

    private def supportedStyles: Map[String, Set[String]] = {

      val typeToInspect = List(typeOf[scalatags.generic.Styles[_,_,_]], typeOf[scalatags.generic.Styles2[_,_,_]])
      val supported = typeToInspect.flatMap( t => t.members.collect{
            case m if( m.typeSignature <:< typeOf[ scalatags.generic.Style]) => {
              val enum = m.typeSignature.members.collect{
                case pair if pair.typeSignature.baseClasses.headOption.isDefined
                  && (pair.typeSignature.resultType.baseClasses.head.name.toString equals "StylePair") => pair.name.toString.trim()
              }
              (m.name.toString.trim(), enum.toSet)
        }
      })
      supported.toMap
    }

}
