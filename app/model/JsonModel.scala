package model

import model.SnippetLang._
import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

sealed case class Blog(content: Seq[BlogElement])
abstract class BlogElement(content: String)
sealed case class Heading(content: String) extends BlogElement(content)
sealed case class Paragraph(content: String) extends BlogElement(content)
sealed case class CodeSnippet(content: String, lang: SnippetLang) extends BlogElement(content)

object JsonModel {
  val headingReads: Reads[Heading] = (JsPath \ "content").read[String].map(Heading(_))
  val paragraphReads: Reads[Paragraph] = (JsPath \ "content").read[String].map(Paragraph(_))
  implicit val langReads: Reads[SnippetLang] = JsPath.read[String].map(SnippetLang.withName(_))

  val codeSnippet: Reads[CodeSnippet] = (
    (JsPath \ "content").read[String] and
    (JsPath \ "lang").read[SnippetLang])(CodeSnippet.apply _)

  def toBlogElements(json: Seq[JsValue]) = {
    val objects = json.flatMap {
      case a: JsObject => Seq(a)
      case _ => Nil
    }
    objects.flatMap(obj => (obj \ "type") match {
      case JsString("heading") => obj.validate[Heading](headingReads).asOpt
      case JsString("paragraph") => obj.validate[Paragraph](paragraphReads).asOpt
      case JsString("codeSnippet") => obj.validate[CodeSnippet](codeSnippet).asOpt
      case _ => None
    })
  }

  def fromJson(json: JsValue): Blog = {
    val els = (json \ "elements") match {
      case JsArray(elements) => toBlogElements(elements)
      case _ => Nil
    }
    Blog(els)
  }

  implicit val headingWrites = new Writes[Heading] {
    def writes(heading: Heading) = Json.obj(
      "type" -> JsString("heading"),
      "content" -> heading.content)
  }

  implicit val paragraphWrites = new Writes[Paragraph] {
    def writes(paragraph: Paragraph) = Json.obj(
      "type" -> JsString("paragraph"),
      "content" -> paragraph.content)
  }

  implicit val codeSnippetWrites = new Writes[CodeSnippet] {
    def writes(codeSnippet: CodeSnippet) = {
      Json.obj(
        "type" -> JsString("codeSnippet"),
        "content" -> codeSnippet.content,
        "highlighted" -> SyntaxHighlighter.highlight(codeSnippet.content),
        "lang" -> JsString(codeSnippet.lang.toString))
    }
  }

  implicit val blogElementWrites = new Writes[BlogElement] {
    def writes(el: BlogElement) = el match {
      case h: Heading => Json.toJson(h)
      case p: Paragraph => Json.toJson(p)
      case c: CodeSnippet => Json.toJson(c)
    }
  }

  implicit val blogWrites = new Writes[Blog] {
    def writes(blog: Blog) = Json.obj(
      "elements" -> Json.toJson(blog.content))
  }

  def toJson(blog: Blog): JsValue = {
    Json.toJson(blog)
  }
}
