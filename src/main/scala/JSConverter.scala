package ch.bornand.scalatags

import scala.collection.JavaConversions._
import org.mozilla.javascript._
import org.mozilla.javascript.{ast => ast}

object JSConverter {
   def run: Unit ={
     val cx= Context.enter


     try {
       cx.setLanguageVersion(Context.VERSION_1_2)
       val scope = cx.initStandardObjects
       val idsBeforeEval = scope.getAllIds.map(_.toString).toSet
       //val result = cx.evaluateString(scope, "obj = {a:1, b:['x','y']}", "MySource", 1, null)
       val result = cx.evaluateString(scope,
         """var test = function(){
           |  return 2;
           |}
         """.stripMargin, "MySource", 1, null)

       val freshIds = scope.getAllIds.map(_.toString).toSet.diff(idsBeforeEval)
       freshIds.map(id => scope.get(id) match {
         case func:Function => {
           println(id)
         }
       })


       val env = new CompilerEnvirons();
       env.setRecoverFromErrors(true);
       env.setGenerateDebugInfo(true);
       env.setRecordingComments(true);


       val factory = new IRFactory(env);
       val res = factory.parse("""
          var main = angular.controller("Name", function($scope){
            $scope.name = 2;
          });
          var o = {};
          o.test = 2;
          angular.controller("Name", function($scope){
               $scope.name = 2;
               $scope.text = "test"
          });
                            """, null, 0)

       val ngAliases = scala.collection.mutable.Set[String]("angular");

       def children(n:Node) : List[Node] = {
       def childrenRec(n:Node) : List[Node] = n match {
           case first:Node if first != null => first :: childrenRec(first.getNext)
           case _ => Nil
         }
         if(n == null) Nil else childrenRec(n.getFirstChild)
       }

       def harvestScript(root:Node): Unit = {
         root match{
           case empty if empty == null =>
           case node => {
             println("Match : " +  root.getClass)
             node match {
               case varDecl:ast.VariableDeclaration => {
                 varDecl.getVariables.map(_.getInitializer).collect{ case call:ast.FunctionCall => call}.foreach(harvestCall(_))
                 println(varDecl.getVariables.head.getTarget.asInstanceOf[ast.Name].getIdentifier)
               }
               case expr:ast.ExpressionStatement => {
                 val actualExpr = expr.getExpression
                 actualExpr match {
                   case call:ast.FunctionCall => harvestCall(call)
                   case _ => println("Expr : " + actualExpr.getClass)
                 }
               }
               case other => println(other.getClass)
             }
             harvestScript(node.getFirstChild);
             harvestScript(node.getNext);
           }
         }
       }

       def harvestCall(call:ast.FunctionCall) = {
         call.getTarget match {
           case propGet:ast.PropertyGet => {
             val func = propGet.getProperty.asInstanceOf[ast.Name].getIdentifier
             val target = propGet.getTarget.asInstanceOf[ast.Name].getIdentifier
             if(ngAliases.contains(target)){
               println("angular component decl : " + func)
             }

           }
           case _ => println("unknown target "+ call.getTarget.getClass)
         }
         println(call.getArguments.collect{ case arg:ast.StringLiteral => arg.getValue })
         val bodies = call.getArguments.collect{ case arg:ast.FunctionNode => arg }
           .map(func => children(func.getBody.asInstanceOf[ast.Block])).flatten
         val leftAssign = bodies.collect{ case exprStat:ast.ExpressionStatement => exprStat.getExpression}
           .collect{ case assign:ast.Assignment => assign.getLeft}.map{case propGet:ast.PropertyGet => propGet}
         leftAssign.foreach{ left =>
           val func = left.getProperty.asInstanceOf[ast.Name].getIdentifier
           val target = left.getTarget.asInstanceOf[ast.Name].getIdentifier
           println(target, func)
         }
       }

       harvestScript(res.getFirstChild)
     }
     finally {
       Context.exit
     }
  }
}
