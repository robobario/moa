package model

import org.python.util.PythonInterpreter

object SyntaxHighlighter {
  val interp: PythonInterpreter = interpreter

  def interpreter: PythonInterpreter = {
    val interpreter1 = new PythonInterpreter()
    interpreter1.exec("from pygments import highlight\n")
    interpreter1.exec("from pygments.lexers.agile import PythonLexer\n")
    interpreter1.exec("from pygments.formatters import HtmlFormatter\n")
    interpreter1.exec("from pygments.styles.monokai import MonokaiStyle\n")
    interpreter1.exec("formatter = HtmlFormatter(style=MonokaiStyle)")
    interpreter1
  }

  def highlight(in: String): String = {
    interp.set("code", in)
    interp.exec("result = highlight(code, PythonLexer(), formatter)")
    interp.get("result").toString
  }

  def css = {
    interp.exec("result = formatter.get_style_defs()")
    val css = interp.get("result").toString
    css
  }
}