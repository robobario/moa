import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.test._
import play.api.test.Helpers._
import model.JsonModel
import play.api.libs.json._
import model.Blog
import model.Heading
import model.Paragraph
import model.CodeSnippet
import model.SnippetLang._
import model.Paragraph
import model.CodeSnippet
import model.SyntaxHighlighter

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class JsonModelSpec extends Specification {

  "from json" should {

    "decode heading" in {
      val json = JsObject(Seq(
        "elements" -> JsArray(Seq(JsObject(Seq(
          "type" -> JsString("heading"),
          "content" -> JsString("sheeit")))))));
      val blog = JsonModel.fromJson(json)
      blog must be equalTo Blog(Seq(Heading("sheeit")))
    }

    "decode paragraph" in {
      val json = JsObject(Seq(
        "elements" -> JsArray(Seq(JsObject(Seq(
          "type" -> JsString("paragraph"),
          "content" -> JsString("sheeit")))))));
      val blog = JsonModel.fromJson(json)
      blog must be equalTo Blog(Seq(Paragraph("sheeit")))
    }

    "decode code snippet" in {
      val json = JsObject(Seq(
        "elements" -> JsArray(Seq(JsObject(Seq(
          "type" -> JsString("codeSnippet"),
          "content" -> JsString("sheeit"),
          "lang" -> JsString("Java")))))));
      val blog = JsonModel.fromJson(json)
      blog must be equalTo Blog(Seq(CodeSnippet("sheeit", Java)))
    }
  }

  "to json" should {

    "encode heading" in {
      val blog = Blog(Seq(Heading("sheeit")))
      val json = JsonModel.toJson(blog)
      val expected = JsObject(Seq(
        "elements" -> JsArray(Seq(JsObject(Seq(
          "type" -> JsString("heading"),
          "content" -> JsString("sheeit")))))));
      json must be equalTo expected
    }

    "encode paragraph" in {
      val blog = Blog(Seq(Paragraph("sheeit")))
      val json = JsonModel.toJson(blog)
      val expected = JsObject(Seq(
        "elements" -> JsArray(Seq(JsObject(Seq(
          "type" -> JsString("paragraph"),
          "content" -> JsString("sheeit")))))));
      json must be equalTo expected
    }

    "encode code snippet" in {
      val blog = Blog(Seq(CodeSnippet("sheeit", Java)))
      val json = JsonModel.toJson(blog)
      val expected = JsObject(Seq(
        "elements" -> JsArray(Seq(JsObject(Seq(
          "type" -> JsString("codeSnippet"),
          "content" -> JsString("sheeit"),
          "highlighted" -> JsString(SyntaxHighlighter.highlight("sheeit")),
          "lang" -> JsString("Java")))))));
      json must be equalTo expected
    }

  }
}
