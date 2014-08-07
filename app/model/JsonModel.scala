package model

import model.SnippetLang._
import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

sealed case class Blog(content:Seq[BlogElement])
abstract class BlogElement(content:String)
sealed case class Heading(content:String) extends BlogElement(content)
sealed case class Paragraph(content:String) extends BlogElement(content)
sealed case class CodeSnippet(content:String, lang:SnippetLang) extends BlogElement(content)

object JsonModel {
	val headingReads: Reads[Heading] = (JsPath \ "content").read[String].map(Heading(_))
    val paragraphReads: Reads[Paragraph] = (JsPath \ "content").read[String].map(Paragraph(_))
    implicit val langReads: Reads[SnippetLang] = JsPath.read[String].map(SnippetLang.withName(_))
    
    val codeSnippet: Reads[CodeSnippet] = (
	  (JsPath \ "content").read[String] and
	  (JsPath \ "lang").read[SnippetLang]
	)(CodeSnippet.apply _)
  
	def fromJson(json:Seq[JsValue]):Seq[BlogElement]={
	  val objects = json.flatMap {
	    case a:JsObject => Seq(a)
	    case _ => Nil
	  }
	  objects.flatMap (obj => (obj \ "type") match {
	  	case JsString("heading") => obj.validate[Heading](headingReads).asOpt
	  	case JsString("paragraph") => obj.validate[Paragraph](paragraphReads).asOpt
	    case JsString("codeSnippet") => obj.validate[CodeSnippet](codeSnippet).asOpt
	    case _ => None
	  })
	}
  
	def fromJson(json:JsValue):Blog ={
		val els = (json \ "elements") match {
		  case JsArray(elements) => fromJson(elements)
		  case _ => Nil
		}
		Blog(els)
	}
}
