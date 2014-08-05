package controllers

import org.python.util.PythonInterpreter
import play.api.mvc._

object Application extends Controller {

  val interp: PythonInterpreter = interpreter

  def interpreter: PythonInterpreter = {
    val interpreter1: PythonInterpreter = new PythonInterpreter()
    interpreter1.exec("from pygments import highlight\n")
    interpreter1.exec("from pygments.lexers.agile import PythonLexer\n")
    interpreter1.exec("from pygments.formatters import HtmlFormatter\n")
    interpreter1.exec("from pygments.styles.monokai import MonokaiStyle\n")
    interpreter1.exec("formatter = HtmlFormatter(style=MonokaiStyle)")
    interpreter1
  }


  def index = Action {
    def interpret(in: String): String = {
      interp.set("code", in)
      interp.exec("result = highlight(code, PythonLexer(), formatter)")
      interp.get("result").toString
    }

    val interpret1: String = interpret("x = Thing(1,2)")
    Ok(views.html.index(interpret1))
  }

  def css = Action {
    interp.exec("result = formatter.get_style_defs()")
    val css = interp.get("result").toString
    Ok(css).as("text/css")
  }

}