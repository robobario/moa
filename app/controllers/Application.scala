package controllers

import org.python.util.PythonInterpreter
import play.api.mvc._
import model.SyntaxHighlighter

object Application extends Controller {

  def index = Action {
    Ok(views.html.index(SyntaxHighlighter.highlight("x = Thing(1,2)")))
  }

  def css = Action {
    Ok(SyntaxHighlighter.css).as("text/css")
  }

}