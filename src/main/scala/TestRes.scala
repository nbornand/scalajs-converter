package ch.bornand.scalatags

import scalatags._
import scalatags.Text.all._
import scalatags.Text.SvgAttrs
import CustomBundle._

object CustomBundle {
  object st extends Text.Cap with Text.Styles with Text.Styles2
  object at extends Text.Cap with Text.Attrs
  object t extends Text.Cap with text.Tags with text.Tags2 with Text.Aggregate
}

object TestRes {

  val converted = html(lang:="en")(
    head(
      meta("http-equiv".attr:="content-type", content:="text/html; charset=UTF-8"),
      meta(charset:="utf-8"),
      meta("http-equiv".attr:="X-UA-Compatible", content:="IE=edge"),
      meta(name:="viewport", content:="width=device-width, initial-scale=1"),
      meta(name:="description", content:=""),
      meta(name:="author", content:=""),
      link(rel:="icon", href:="http://getbootstrap.com/favicon.ico"),
      t.title("ITBeauty"
      ),"<!-- Bootstrap core CSS -->",
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
      link(href:="lib/datepicker/css/datepicker.css", rel:="stylesheet"),"<!-- Bootstrap theme -->","""<!--<link href="index_files/bootstrap-theme.css" rel="stylesheet">-->""","<!-- Custom styles for this template -->","""<!--<link href="index_files/theme.css" rel="stylesheet">-->""","<!-- Just for debugging purposes. Don't actually copy these 2 lines! -->","""<!--[if lt IE 9]><script src="../../assets/js/ie8-responsive-file-warning.js"></script><![endif]-->""",
      script(src:="index_files/ie-emulation-modes-warning.js"),"<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->","""<!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->""",
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
      ),"""<!--<div role="main" id="preloader">
    </br></br>
    <img src="img/preloader.gif"></div>
</div>-->""",
      div(role:="main", `class`:="waitingLoad")(
        br,
        div(`class`:="page-header container theme-showcase")(
          h1("ng-bind".attr:="state.currentView.label")
        ),
        div(id:="userFeedback", "ng-if".attr:="state.feedback.display", `class`:="container theme-showcase")(
          div(`class`:="alert alert-{{state.feedback.cssClass}}", role:="alert")("{{state.feedback.message}}"
          )
        ),
        div("ng-switch".attr:="state.currentView.name")(
          div("ng-switch-when".attr:="clients", `class`:="container theme-showcase")(
            div("ng-controller".attr:="client", `class`:="row")(
              div("ng-switch".attr:="clientTagActive")(
                ul(`class`:="nav nav-tabs", role:="tablist")(
                  li("ng-repeat".attr:="tab in clientTags", "ng-class".attr:="{active:clientTagActive===tab.id}")(
                    a("ng-click".attr:="setClientTab(tab)")("{{tab.label}}"
                    )
                  )
                ),
                br,
                div("ng-switch-when".attr:="search")(
                  div("search-module".attr:="", "settings".attr:="searchSettings", "ng-if".attr:="state.current==='waitForSelection'")(
                    div("ng-if".attr:="clients.length > 0", "ng-entity-table".attr:="client", "data".attr:="clients", "on-selection".attr:="onClientSelected", "with-limit".attr:="")
                  ),
                  ul(`class`:="list-group", "ng-if".attr:="state.current==='clientSelected'")(
                    li(`class`:="list-group-item active")(
                      h4("Client Séléctionné"
                      )
                    ),
                    li(`class`:="list-group-item")(
                      div("ng-if".attr:="state.current==='clientSelected'", "ng-entity-detail".attr:="client", "data".attr:="state.clientSelected", "label".attr:="Client séléctionné", "on-discard".attr:="onClientUnselected"),
                      div(`class`:="panel panel-default")(
                        div(`class`:="panel-heading", "ng-click".attr:="expandClientDetails(state.clientSelected)")("Capital beauté"
                        ),
                        div(`class`:="panel-body", "ng-if".attr:="accountExpanded", "ng-repeat".attr:="account in accounts")(
                          p(
                            strong("Montant actuel du compte : {{account.balance.toFixed(2)}}"
                            )
                          ),
                          div("ng-if".attr:="account.accounttransation_list.length > 0", "data".attr:="account.accounttransation_list", "on-selection".attr:="onTransactionSelected", "with-limit".attr:=""),
                          p("ng-if".attr:="account.accounttransation_list.length === 0")("Pas de transactions pour le moment"
                          )
                        ),
                        div(`class`:="panel-heading")("Abonnements"
                        ),
                        div(`class`:="panel-body", "ng-if".attr:="subscriptionExpanded", "ng-repeat".attr:="subscription in subscriptions")(
                          div(
                          ),
                          div(
                            p(
                              strong("Abbonement pour : {{account.balance.toFixed(2)}}"
                              )
                            )
                          )
                        ),"<!-- List group -->"
                      )
                    ),
                    li(`class`:="list-group-item active", "ng-click".attr:="list.push(list.length)")(
                      button(`type`:="button", `class`:="btn btn-default", "ng-click".attr:="unselectClient()")(
                        span(`class`:="glyphicon glyphicon-arrow-left"),"Retour à la liste"
                      ),
                      button(`type`:="button", `class`:="btn btn-default", "ng-click".attr:="createRDV()")(
                        span(`class`:="glyphicon glyphicon-plus"),"Créer un RDV"
                      ),
                      button(`type`:="button", `class`:="btn btn-default", "ng-click".attr:="sellProduct(state.clientSelected)")(
                        span(`class`:="glyphicon glyphicon-usd"),"Vente produit"
                      ),
                      button(`type`:="button", `class`:="btn btn-warning", "ng-click".attr:="modifyClient()")(
                        span(`class`:="glyphicon glyphicon-edit"),"Modifier les information client"
                      )
                    )
                  ),
                  div("ng-if".attr:="state.current==='modifyClient'", "ng-entity-modify".attr:="client", "data".attr:="state.clientSelected", "label".attr:="Client séléctionné", "on-discard".attr:="onClientUnselected")
                ),
                div("ng-switch-when".attr:="add")(
                  ul(`class`:="list-group")(
                    li(`class`:="list-group-item")(
                      div("ng-entity-modify".attr:="client", "data".attr:="{}", "label".attr:="Client", "on-discard".attr:="onClientUnselected")
                    )
                  )
                )
              )
            )
          ),
          div("ng-switch-when".attr:="employees", `class`:="container theme-showcase")(
            div("ng-controller".attr:="employee")(
              div("ng-if".attr:="state.current === 'displayAll'")(
                ul(`class`:="nav nav-pills")(
                  li("ng-repeat".attr:="salon in salons", "ng-class".attr:="{active:state.salonSelected.id === salon.id}")(
                    a("ng-click".attr:="setSalonSelected(salon)", "ng-bind".attr:="salon.name")(
                      span(`class`:="badge")("{{salon.employee_list.length}}"
                      )
                    )
                  )
                ),
                br,
                div("ng-entity-table".attr:="employee", "data".attr:="employees", "on-selection".attr:="employeeSelected"),
                button(`type`:="button", `class`:="btn btn-default", "ng-click".attr:="addEmployee()")(
                  span(`class`:="glyphicon glyphicon-plus"),"Ajouter un employé"
                )
              ),
              ul(`class`:="list-group", "ng-if".attr:="state.current==='employeeDetail'")(
                li(`class`:="list-group-item active")(
                  h4("Employé Séléctionné"
                  )
                ),
                li(`class`:="list-group-item")(
                  div("ng-entity-detail".attr:="employee", "data".attr:="state.employeeSelected", "label".attr:="Client séléctionné", "on-discard".attr:="onClientUnselected")
                ),
                li(`class`:="list-group-item active", "ng-click".attr:="list.push(list.length)")(
                  button(`type`:="button", `class`:="btn btn-default", "ng-click".attr:="unselect()")(
                    span(`class`:="glyphicon glyphicon-arrow-left"),"Retour à la liste"
                  ),
                  button(`type`:="button", `class`:="btn btn-warning", "ng-click".attr:="modifyEmployee()")(
                    span(`class`:="glyphicon glyphicon-edit"),"Modifier les information"
                  ),
                  button(`type`:="button", `class`:="btn btn-default", "ng-click".attr:="createRDV()")(
                    span(`class`:="glyphicon glyphicon-calendar"),"Horaire par défaut"
                  ),
                  button(`type`:="button", `class`:="btn btn-default", "ng-click".attr:="createRDV()")(
                    span(`class`:="glyphicon glyphicon-plane"),"Vacances"
                  )
                )
              ),
              div("ng-if".attr:="state.current==='modifyEmployee'", "ng-entity-modify".attr:="employee", "data".attr:="state.employeeSelected", "label".attr:="Client séléctionné", "on-discard".attr:="onClientUnselected")
            )
          ),
          div("ng-switch-when".attr:="planning")(
            div("ng-controller".attr:="planning")(
              div(`class`:="container theme-showcase")(
                ul(`class`:="list-group", "ng-if".attr:="state.current==='rdvCreation' || state.current==='moveRdv'")(
                  li(`class`:="list-group-item active")(
                    h4("Rendez-vous"
                    )
                  ),
                  li(`class`:="list-group-item")(
                    div("ng-entity-detail".attr:="client", "data".attr:="currentAppointment.client", "label".attr:="Client séléctionné", "on-discard".attr:="onClientUnselected")
                  ),
                  li(`class`:="list-group-item")(
                    div(`class`:="form-group")(
                      label(`class`:="control-label")("Etape 1: séléctionnez une plage horaire"
                      )
                    )
                  ),
                  li(`class`:="list-group-item", "ng-if".attr:="slotSelected.length !== 0")(
                    label(`class`:="control-label")("Plages horaires sélectionnées :",
                      span("ng-repeat".attr:="slot in slotSelected")("{{slot.start}} avec {{getEmployeeWithId(slot.employee_id).first}},"
                      )
                    )
                  ),
                  li(`class`:="list-group-item active", "ng-click".attr:="list.push(list.length)")(
                    button(`type`:="button", `class`:="btn btn-danger col-sm-offset-7", "ng-click".attr:="cancelRDV()")(
                      span(`class`:="glyphicon glyphicon-trash"),"Annuler"
                    ),
                    button(`type`:="button", `class`:="btn btn-success", "ng-click".attr:="saveRDV(currentAppointment)")(
                      span(`class`:="glyphicon glyphicon-floppy-disk"),"Sauver le Rdv"
                    )
                  )
                ),
                ul(`class`:="list-group container theme-showcase", "ng-if".attr:="state.current==='rdvModify'")(
                  li(`class`:="list-group-item active")(
                    h4("Modification de rendez-vous"
                    )
                  ),
                  li(`class`:="list-group-item")(
                    div("ng-entity-detail".attr:="client", "data".attr:="rdvModified.client", "label".attr:="Client séléctionné", "on-discard".attr:="onClientUnselected")
                  ),
                  li(`class`:="list-group-item", "ng-repeat".attr:="prestation in rdvModified.appointmentservice_list")(
                    button(`type`:="button", `class`:="col-sm-1 close", "ng-click".attr:="removeService(prestation)")(
                      span(fontSize := "30px", color := "red", position := "relative", top := "-3px")("×"
                      ),
                      span(`class`:="sr-only")("Close"
                      )
                    ),
                    select(`class`:="form-control", "ng-model".attr:="prestation.service_id", "ng-options".attr:="service.id as service.name for service in serviceList", "ng-change".attr:="modifyService(prestation)")(
                    )
                  ),
                  li(`class`:="list-group-item active", "ng-click".attr:="list.push(list.length)")(
                    button("ng-if".attr:="rdvModified.state!=='payed'", `type`:="button", `class`:="btn btn-default", "ng-click".attr:="addServiceToExisting()")(
                      span(`class`:="glyphicon glyphicon-plus"),"Rajouter une prestation"
                    ),
                    button("ng-if".attr:="rdvModified.state!=='payed'", `type`:="button", `class`:="btn btn-danger col-sm-offset-6", "ng-click".attr:="removeRDV(rdvModified)")(
                      span(`class`:="glyphicon glyphicon-trash"),"Supprimer"
                    ),
                    button("ng-if".attr:="rdvModified.state==='taken'", `type`:="button", `class`:="btn btn-default", "ng-click".attr:="moveTimeSlots(rdvModified)")(
                      span(`class`:="glyphicon glyphicon-fullscreen"),"Déplacer"
                    ),
                    button("ng-if".attr:="rdvModified.state==='taken'", `type`:="button", `class`:="btn btn-success", "ng-click".attr:="startRDV()")(
                      span(`class`:="glyphicon glyphicon-time"),"Début"
                    ),
                    button("ng-if".attr:="rdvModified.state==='started'", `type`:="button", `class`:="btn btn-default", "ng-click".attr:="endRDV()")(
                      span(`class`:="glyphicon glyphicon-stop"),"Marque comme fini"
                    ),
                    button("ng-if".attr:="rdvModified.state==='done'", `type`:="button", `class`:="btn btn-default", "ng-click".attr:="payRDV()")(
                      span(`class`:="glyphicon glyphicon-usd"),"Paiement"
                    )
                  )
                )
              ),
              div("ng-date-picker".attr:="", `class`:="container theme-showcase"),
              br,
              div(`class`:="panel panel-default", "ng-if".attr:="detail.display", position := "fixed", left := "{{detail.posX}}px", top := "{{detail.posY}}px")(
                ul(`class`:="list-group")(
                  li(`class`:="list-group-item")(
                    strong("{{detail.rdv.client.second}} {{detail.rdv.client.first}}"
                    ),"({{detail.rdv.client.gender}})",
                    br,"{{detail.rdv.client.mobile}}"
                  ),
                  li(`class`:="list-group-item")(
                    span("ng-repeat".attr:="service in detail.rdv.appointmentservice_list")("{{serviceWithId(service.service_id).name}}",
                      br
                    )
                  )
                )
              ),
              div(`class`:="widerContainer")(
                table(`class`:="table table-bordered table-striped planning", tableLayout := "fixed", marginTop := "15px", "ng-if".attr:="matrix !== undefined")(
                  thead(
                    tr(`class`:="info")(
                      th(width := "70px"),
                      th("ng-repeat".attr:="employee in salonSelected.employee_list")("{{employee.first}}"
                      )
                    )
                  ),
                  tbody(
                    tr("ng-repeat".attr:="t in planning.timeScale")(
                      th(`class`:="text-nowrap", width := "50px")("{{t.text}}"
                      ),
                      td("ng-repeat".attr:="employee in salonSelected.employee_list", "ng-click".attr:="slotClicked(t.i,employee.id)", "ng-class".attr:="""{info:matrix[t.i][employee.id].link.state === 'taken',
                                    success:matrix[t.i][employee.id].link.state === 'payed', danger:matrix[t.i][employee.id].link.state === 'done',
                                    selected: matrix[t.i][employee.id].focus === true, started:matrix[t.i][employee.id].link.state === 'started',
                                    warning:rdvModified.id && matrix[t.i][employee.id].link.id===rdvModified.id}""")(
                          a("ng-click".attr:="displayRDV(matrix[t.i][employee.id].link)", "ng-mouseover".attr:="displayAppointmentDetails($event, matrix[t.i][employee.id].link)", "ng-mouseleave".attr:="hideAppointmentDetails()")("{{matrix[t.i][employee.id].label}}"
                          )
                        )
                    )
                  ),
                  tbody(
                    tr(`class`:="info")(
                      th,
                      th("ng-repeat".attr:="employee in salonSelected.employee_list")("{{employee.first}}"
                      )
                    )
                  )
                )
              )
            )
          ),
          div("ng-switch-when".attr:="money", `class`:="container theme-showcase")(
            div("ng-controller".attr:="money")(
              div("ng-switch".attr:="checkoutTagActive", "ng-if".attr:="state.current!=='payRDV'")(
                ul(`class`:="nav nav-tabs", role:="tablist")(
                  li("ng-repeat".attr:="tab in checkoutTabs", "ng-class".attr:="{active:checkoutTagActive===tab.id}")(
                    a("ng-click".attr:="setCheckoutTab(tab)")("{{tab.label}}"
                    )
                  )
                ),
                br,
                div("ng-switch-when".attr:="summary")(
                  div("ng-entity-table".attr:="dailysummary", "data".attr:="summary", "on-selection".attr:="identity")
                ),
                div("ng-switch-when".attr:="add")(
                  div("ng-entity-modify".attr:="spending", "data".attr:="{}", "label".attr:="Dépense", "default".attr:="{day:'today'}")
                ),
                div("ng-switch-when".attr:="list")(
                  div("ng-entity-table".attr:="ticket", "data".attr:="dailyTickets", "on-selection".attr:="identity")
                ),
                div("ng-switch-when".attr:="terminate")("terminate"
                )
              ),
              ul(`class`:="list-group", "ng-if".attr:="state.current==='payRDV'")(
                li(`class`:="list-group-item active")(
                  h4("Ticket pour rendez-vous"
                  )
                ),
                li(`class`:="list-group-item", "ng-repeat".attr:="prestation in state.rdvTicket.rdv.appointmentservice_list")(
                  div(`class`:="form-group")(
                    label(`class`:="control-label col-sm-10")("{{getServiceWithId(prestation.service_id).name}}"
                    ),
                    label(`class`:="control-label col-sm-1")("{{getServiceWithId(prestation.service_id).price}}"
                    ),
                    br
                  )
                ),
                li(`class`:="list-group-item", "ng-repeat".attr:="wrapper in state.rdvTicket.products")(
                  div(`class`:="form-group")(
                    label(`class`:="control-label col-sm-8")("{{wrapper.product.name}}"
                    ),
                    div(`class`:="col-sm-1")(
                      input(`class`:="form-control", "ng-model".attr:="wrapper.quantity", "ng-change".attr:="reValidate()", placeholder:="{{property.label||property.name}}")
                    ),
                    label(`class`:="control-label col-sm-1")("x {{wrapper.product.sell_price}} ="
                    ),
                    label(`class`:="control-label col-sm-1")("{{(wrapper.product.sell_price*wrapper.quantity).toFixed(2)}}"
                    ),
                    button(`type`:="button", `class`:="col-sm-1 close", "ng-click".attr:="removeProduct(state.rdvTicket.products, wrapper)")(
                      span(fontSize := "30px", color := "red", position := "relative", top := "-3px")("×"
                      ),
                      span(`class`:="sr-only")("Close"
                      )
                    ),
                    br
                  )
                ),
                li(`class`:="list-group-item", "ng-if".attr:="state.rdvTicket.products === 0")(
                  div(`class`:="form-group")(
                    label(`class`:="control-label col-sm-12")("Aucun produit pour le moment"
                    ),
                    br
                  )
                ),
                li(`class`:="list-group-item")(
                  div(`class`:="form-group")(
                    label(`class`:="control-label col-sm-10")("Total :"
                    ),
                    label(`class`:="control-label col-sm-1")("{{computeSum(state.rdvTicket.rdv.appointmentservice_list, state.rdvTicket.products).amount}}"
                    ),
                    br
                  )
                ),
                li(`class`:="list-group-item")(
                  label(`class`:="control-label col-sm-3")("Rabais"
                  ),
                  div(`class`:="col-sm-3")(
                    select(`class`:="form-control", "ng-model".attr:="state.rdvTicket.discount", "ng-change".attr:="recomputeDiscount()", "ng-init".attr:="state.rdvTicket.discount=state.rdvTicket.discount||discounts[0].amount")(
                      option("ng-repeat".attr:="rate in discounts", value:="{{rate.amount}}")("{{rate.label}}"
                      )
                    )
                  ),
                  div("ng-if".attr:="state.rdvTicket.discount==='other'")(
                    div(`class`:="col-sm-1")(
                      input(`class`:="form-control", "ng-model".attr:="state.rdvTicket.discountPercent", "ng-change".attr:="recomputeDiscount()")
                    ),
                    label(`class`:="control-label col-sm-3")("%"
                    )
                  ),
                  div("ng-if".attr:="state.rdvTicket.discount!=='other'")(
                    label(`class`:="control-label col-sm-4")
                  ),
                  label(`class`:="control-label col-sm-2")("-{{effectiveDiscount}}"
                  ),
                  label
                ),
                li(`class`:="list-group-item")(
                  div(`class`:="form-group")(
                    label(`class`:="control-label col-sm-10")("Total avec deduction :"
                    ),
                    label(`class`:="control-label col-sm-2")("{{totalWithDiscount}}"
                    ),
                    br
                  )
                ),
                li(`class`:="list-group-item")(
                  label(`class`:="control-label col-sm-3")("Moyen de payement"
                  ),
                  div(`class`:="col-sm-3")(
                    select(`class`:="form-control", "ng-model".attr:="state.rdvTicket.pay_mean")(
                      option("ng-repeat".attr:="mean in paymeans", value:="{{mean.id}}")("{{mean.label}}"
                      )
                    )
                  ),
                  div("ng-if".attr:="state.rdvTicket.pay_mean === 'mixte'")(
                    div(`class`:="col-sm-2")(
                      select(`class`:="form-control", "ng-model".attr:="state.rdvTicket.mainMean")(
                        option("ng-repeat".attr:="mean in paymeans", value:="{{mean.id}}")("{{mean.label}}"
                        )
                      )
                    ),
                    div(`class`:="col-sm-1")(
                      input(`class`:="form-control", "ng-model".attr:="mainAmount", "ng-change".attr:="reValidate()")
                    ),
                    div(`class`:="col-sm-2")(
                      select(`class`:="form-control", "ng-model".attr:="state.rdvTicket.secondMean")(
                        option("ng-repeat".attr:="mean in paymeans", value:="{{mean.id}}")("{{mean.label}}"
                        )
                      )
                    ),
                    label(`class`:="col-sm-1")("{{(parseFloat(mainAmount)||0)-totalWithDiscount}}"
                    )
                  ),
                  label(`class`:="col-sm-6", "ng-if".attr:="state.rdvTicket.pay_mean !== 'mixte'"),
                  label
                ),
                li(`class`:="list-group-item active", "ng-click".attr:="list.push(list.length)")(
                  button(`type`:="button", `class`:="btn btn-default", "ng-click".attr:="addProduct()")(
                    span(`class`:="glyphicon glyphicon-plus"),"Ajouter un produit"
                  ),
                  button(`type`:="button", `class`:="btn btn-warning col-sm-offset-7", "ng-click".attr:="removeRDV()")(
                    span(`class`:="glyphicon glyphicon-trash"),"Retour"
                  ),
                  button(`type`:="button", `class`:="btn btn-success", "ng-click".attr:="addTicket(state.rdvTicket.rdv, state.rdvTicket.products, state.rdvTicket.pay_mean)")(
                    span(`class`:="glyphicon glyphicon-time"),"Encaisser"
                  )
                )
              )
            )
          ),
          div("ng-switch-when".attr:="salons", "ng-controller".attr:="salon", `class`:="container theme-showcase")(
            div("ng-switch".attr:="salonTagActive")(
              ul(`class`:="nav nav-tabs", role:="tablist")(
                li("ng-repeat".attr:="tab in salonTags", "ng-class".attr:="{active:salonTagActive===tab.id}")(
                  a("ng-click".attr:="setSalonTab(tab)")("{{tab.label}}"
                  )
                )
              ),
              br,
              div("ng-switch-when".attr:="display")(
                div("ng-entity-table".attr:="salon", "data".attr:="salons", "on-selection".attr:="onSalonSelected")
              ),
              div("ng-switch-when".attr:="add")(
                div("ng-entity-modify".attr:="salon", "data".attr:="{}", "label".attr:="Salon")
              ),
              div("ng-switch-when".attr:="modify")(
                button("ng-if".attr:="state.current !== 'modifySalon'", `type`:="button", "ng-repeat".attr:="salon in salons", `class`:="btn btn-default", "ng-click".attr:="modifyCurrentSalon(salon)")(
                  span(`class`:="glyphicon glyphicon-edit"),"Modifier : {{salon.name}}"
                ),
                div("ng-if".attr:="state.current === 'modifySalon'", "ng-entity-modify".attr:="salon", "data".attr:="state.salonSelected", "label".attr:="Salon")
              )
            )
          ),
          div("ng-switch-when".attr:="products")(
            div("ng-controller".attr:="product", `class`:="row")(
              div("ng-switch".attr:="productTagActive")(
                ul(`class`:="nav nav-tabs container theme-showcase", role:="tablist")(
                  li("ng-repeat".attr:="tab in productTags", "ng-class".attr:="{active:productTagActive===tab.id}")(
                    a("ng-click".attr:="setProductTab(tab)")("{{tab.label}}"
                    )
                  )
                ),
                br,
                div("ng-switch-when".attr:="search")(
                  div("search-module".attr:="", "settings".attr:="productSettings", "ng-if".attr:="state.current!=='productSelected'", `class`:="widerContainer")(
                    div("ng-if".attr:="products.length > 0", "ng-entity-table".attr:="product", "data".attr:="products", "on-selection".attr:="onProductSelected", "with-limit".attr:="")
                  ),
                  ul(`class`:="list-group widerContainer", "ng-if".attr:="state.current==='productSelected'")(
                    li(`class`:="list-group-item active")(
                      h4("Produit Séléctionné"
                      )
                    ),
                    li(`class`:="list-group-item")(
                      div("ng-if".attr:="state.current==='productSelected'", "ng-entity-detail".attr:="product", "data".attr:="state.productSelected", "label".attr:="Product séléctionné", "on-discard".attr:="onClientUnselected")
                    ),
                    li(`class`:="list-group-item active", "ng-click".attr:="list.push(list.length)")(
                      button(`type`:="button", `class`:="btn btn-default", "ng-click".attr:="unselectProduct()")(
                        span(`class`:="glyphicon glyphicon-arrow-left"),"Retour à la liste"
                      ),
                      button(`type`:="button", `class`:="btn btn-default", "ng-if".attr:="addToRdv", "ng-click".attr:="addProductToRdv(state.productSelected)")(
                        span(`class`:="glyphicon glyphicon-plus"),"Ajouter au ticket"
                      ),
                      button(`type`:="button", `class`:="btn btn-warning", "ng-if".attr:="!addToRdv", "ng-click".attr:="modifyProduct()")(
                        span(`class`:="glyphicon glyphicon-edit"),"Modifier les information produit"
                      ),
                      button(`type`:="button", `class`:="btn btn-warning", "ng-click".attr:="reorder()")(
                        span(`class`:="glyphicon glyphicon-edit"),"Ajouter à la liste de commande"
                      )
                    )
                  )
                ),
                div("ng-switch-when".attr:="add", `class`:="container theme-showcase")(
                  ul(`class`:="list-group")(
                    li(`class`:="list-group-item")(
                      div("ng-entity-modify".attr:="product", "data".attr:="{}", "label".attr:="Produit", "on-discard".attr:="onClientUnselected")
                    )
                  )
                )
              )
            )
          ),
          div("ng-switch-when".attr:="services", "ng-controller".attr:="service", `class`:="container theme-showcase")(
            div("ng-switch".attr:="serviceTagActive")(
              ul(`class`:="nav nav-tabs", role:="tablist")(
                li("ng-repeat".attr:="tab in serviceTags", "ng-class".attr:="{active:serviceTagActive===tab.id}")(
                  a("ng-click".attr:="setServiceTab(tab)")("{{tab.label}}"
                  )
                )
              ),
              br,
              div("ng-switch-when".attr:="display")(
                div("ng-entity-table".attr:="service", "data".attr:="serviceList", "on-selection".attr:="onServiceSelected")
              ),
              div("ng-switch-when".attr:="add")(
                div("ng-entity-modify".attr:="service", "data".attr:="{}", "label".attr:="Prestation")
              ),
              div("ng-switch-when".attr:="modify")(
                button("ng-if".attr:="state.current !== 'modifySalon'", `type`:="button", "ng-repeat".attr:="salon in salons", `class`:="btn btn-default", "ng-click".attr:="modifyCurrentSalon(salon)")(
                  span(`class`:="glyphicon glyphicon-edit"),"Modifier : {{salon.name}}"
                ),
                div("ng-if".attr:="state.current === 'modifySalon'", "ng-entity-modify".attr:="salon", "data".attr:="state.salonSelected", "label".attr:="Salon")
              )
            )
          )
        )
      ),"<!-- /container -->","""<!-- Bootstrap core JavaScript
================================================== -->""",
      script(src:="index_files/jquery.js"),
      script(src:="index_files/bootstrap.js"),
      script(src:="js/bootstrap/collapse.js"),
      script(src:="js/bootstrap/transition.js"),
      script(src:="index_files/docs.js"),"<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->",
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
      div("data-original-title".attr:="Copy to clipboard", title:="", position := "absolute", left := "0px", top := "-9999px", width := "15px", height := "15px", zIndex := "999999999", `class`:="global-zeroclipboard-container", id:="global-zeroclipboard-html-bridge")(
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
          embed(src:="index_files/ZeroClipboard.swf", "loop".attr:="false", "menu".attr:="false", "quality".attr:="best", "bgcolor".attr:="#ffffff", name:="global-zeroclipboard-flash-bridge", "allowscriptaccess".attr:="sameDomain", "allowfullscreen".attr:="false", `type`:="application/x-shockwave-flash", "wmode".attr:="transparent", "pluginspage".attr:="http://www.macromedia.com/go/getflashplayer", "flashvars".attr:="trustedOrigins=getbootstrap.com%2C%2F%2Fgetbootstrap.com%2Chttp%3A%2F%2Fgetbootstrap.com", "scale".attr:="exactfit", height:="100%", width:="100%")
        )
      )
    )
  )

}
