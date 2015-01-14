
import scalatags._
import scalatags.Text.all._
import scalatags.Text.SvgAttrs
import CustomBundle._

object CustomBundle {

  object st extends Text.Cap with Text.Styles with Text.Styles2

  object at extends Text.Cap with Text.Attrs with SvgAttrs

  object t extends Text.Cap with text.Tags with text.Tags2 with Text.Aggregate

}

case class CssClass(tag:String, modifiers:Modifier*) extends generic.TypedTag[text.Builder, String, String] with generic.Frag[text.Builder, String]{
  def applyTo(t: text.Builder) = modifiers.foreach(_.applyTo(t))

  def writeTo(strb: StringBuilder): Unit = {
    val builder = new text.Builder()
    build(builder)

    // tag
    strb += '.' ++= tag ++= "{"

    // attributes
    var i = 0
    while (i < builder.attrIndex){
      val pair = builder.attrs(i)
      strb += ' ' ++= pair._1 ++= ":\""
      Escaping.escape(pair._2, strb)
      strb += ';'
      i += 1
    }

    if (builder.childIndex == 0) {
      // No children - close tag
      strb ++= "}"
    } else {
      strb += '}'
      // Childrens
      var i = 0
      while(i < builder.childIndex){
        builder.children(i).writeTo(strb)
        i += 1
      }

      // Closing tag
      strb ++= "}"
    }
  }
  override def toString = {
    val strb = new StringBuilder
    writeTo(strb)
    strb.toString()
  }
  def render: String = this.toString.asInstanceOf[String]

}



object TestRes {

  implicit def cssClassToString(c:CssClass):String = c.tag

  val test = CssClass("test", backgroundColor:="red")

  def main(args:Array[String]) : Unit = {
    println(test.render)
  }

  val converted =

    html(lang:="en")(
      head(
        meta("http-equiv".attr:="content-type", content:="text/html; charset=UTF-8"),
        meta(charset:="utf-8"),
        meta("http-equiv".attr:="X-UA-Compatible", content:="IE=edge"),
        meta(name:="viewport", content:="width=device-width, initial-scale=1"),
        meta(name:="description", content:=""),
        meta(name:=test.tag, content:=""),
        link(rel:="icon", href:="http://getbootstrap.com/favicon.ico"),
        t.title("ITBeauty"
        ),raw("<!-- Bootstrap core CSS -->"),
        link(href:="css/bootstrap.css", rel:="stylesheet"),
        t.style(""".widerContainer{
        padding:0 30px 0 30px;
        max-width:1600px;
        margin:auto;
        }
        .waitingLoad{
        opacity:0;
        -webkit-transition: opacity 0.8s ease-in;
        -moz-transition: opacity 0.8s ease-in;
        -ms-transition: opacity 0.8s ease-in;
        -o-transition: opacity 0.8s ease-in;
        transition: opacity 0.8s ease-in;
        }"""
        ),
        link(href:="lib/datepicker/css/datepicker.css", rel:="stylesheet"),raw("<!-- Bootstrap theme -->"),raw("""<!--<link href="index_files/bootstrap-theme.css" rel="stylesheet">-->"""),raw("<!-- Custom styles for this template -->"),raw("""<!--<link href="index_files/theme.css" rel="stylesheet">-->"""),raw("<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->"),raw("""<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->"""),
        script(src:="index_files/ie-emulation-modes-warning.js"),raw("<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->"),raw("""<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->"""),
        t.style(`type`:="text/css", id:="holderjs-style")
      ),
      body(role:="document", "ng-app".attr:="itbeauty", "ng-controller".attr:="root")(
        div(`class`:="navbar navbar-inverse navbar-fixed-top", role:="navigation")(
          div(`class`:="container")(
            div(`class`:="navbar-header")(
              button(`type`:="button", `class`:="navbar-toggle collapsed", "data-toggle".attr:="collapse", "data-target".attr:=".navbar-collapse")(
                span(`class`:="sr-only")("Toggle navigation"
                ),
                span(`class`:="icon-bar"),
                span(`class`:="icon-bar"),"Ajouter à la liste de commande",
                span(`class`:="icon-bar")
              ),
              a(`class`:="navbar-brand", href:="#")("ITBeauty"
              )
            ),
            div(`class`:="navbar-collapse collapse")(
              ul(`class`:="nav navbar-nav waitingLoad")(
                li(`class`:="dropdown")(
                  a(href:="#", `class`:="dropdown-toggle", "data-toggle".attr:="dropdown")("{{state.salonSelected.name}}",
                    span(`class`:="caret")
                  ),
                  ul(`class`:="dropdown-menu", role:="menu")(
                    li("ng-repeat".attr:="salon in salons")(
                      a(href:="#", "ng-click".attr:="setSalonSelected(salon)")("{{salon.name}}"
                      )
                    )
                  )
                ),
                li("ng-repeat".attr:="(index,view) in ::state.views", "ng-click".attr:="state.setCurrentViewId(index)", "ng-class".attr:="{active:state.currentViewId==index}")(
                  a("ng-bind".attr:="view.label")
                )
              )
            )
          )
        ),raw("""<!--<div role="main" id="preloader">
    </br></br>
    <img src="img/preloader.gif"></div>
</div>-->"""),
        div(role:="main", `class`:="waitingLoad")(
          br,
          div(`class`:="page-header container theme-showcase")(
            h1("ng-bind".attr:="state.currentView.label")
          ),
          div(id:="userFeedback", "ng-if".attr:="state.feedback.display", `class`:="container theme-showcase")(
            div(`class`:="alert alert-{{state.feedback.cssClass}}", role:="alert")("{{state.feedback.message}}"
            )
          ),raw("<!-- /container -->"),raw("""<!-- Bootstrap core JavaScript
================================================== -->"""),
          script(src:="index_files/jquery.js"),
          script(src:="index_files/bootstrap.js"),
          script(src:="js/bootstrap/collapse.js"),
          script(src:="js/bootstrap/transition.js"),
          script(src:="index_files/docs.js"),raw("<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->"),
          script(src:="index_files/ie10-viewport-bug-workaround.js"),
          script(src:="lib/jquery1.11.min.js"),
          script(src:="lib/angular1.3.js"),
          script(src:="lib/datepicker/js/bootstrap-datepicker.js"),
          script(src:="lib/random-color.js"),
          script(src:="js/controllers/root-controller.js"),
          script(src:="js/controllers/client-controller.js"),
          script(src:="js/controllers/employee-controller.js"),
          script(src:="js/controllers/salon-controller.js"),
          script(src:="js/controllers/money-controller.js"),
          script(src:="js/controllers/planning-controller.js"),
          script(src:="js/controllers/product-controller.js"),
          script(src:="js/controllers/service-controller.js"),
          script(src:="js/directives/ng-entity-table.js"),
          script(src:="js/directives/ng-entity-detail.js"),
          script(src:="js/directives/search-module.js"),
          script(src:="js/directives/ng-date-picker.js"),
          script(src:="js/directives/ng-entity-modify.js"),
          script(src:="js/services/remote-data-service.js"),
          script(src:="js/services/time-service.js"),
          div("data-original-title".attr:="Copy to clipboard", title:="", position := "absolute", left := 0.px, top := (-9999).px, width := 15.px, height := 15.px, zIndex := 999999999, `class`:="global-zeroclipboard-container", id:="global-zeroclipboard-html-bridge")(
            `object`("classid".attr:="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000", id:="global-zeroclipboard-flash-bridge", height:="100%", width:="100%")(
              param(name:="movie", value:="/assets/flash/ZeroClipboard.swf?noCache=1412403846320"),
              param(name:="allowScriptAccess", value:="sameDomain"),
              param(name:="scale", value:="exactfit"),
              param(name:="loop", value:="false"),
              param(name:="menu", value:="false"),
              param(name:="quality", value:="best"),
              param(name:="bgcolor", value:="#ffffff"),
              param(name:="wmode", value:="transparent"),
              param(name:="flashvars", value:="trustedOrigins=getbootstrap.com%2C%2F%2Fgetbootstrap.com%2Chttp%3A%2F%2Fgetbootstrap.com"),
              embed(src:="index_files/ZeroClipboard.swf", "loop".attr:="false", "menu".attr:="false", "quality".attr:="best", "bgcolor".attr:="#ffffff", name:="global-zeroclipboard-flash-bridge", "allowscriptaccess".attr:="sameDomain", "allowfullscreen".attr:="false", `type`:="application/x-shockwave-flash", "wmode".attr:="transparent", "pluginspage".attr:="http://www.macromedia.com/go/getflashplayer", "flashvars".attr:="trustedOrigins=getbootstrap.com%2C%2F%2Fgetbootstrap.com%2Chttp%3A%2F%2Fgetbootstrap.com", at.scale:="exactfit", height:="100%", width:="100%")
            )
          )
        )
      ))

}