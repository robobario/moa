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

/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class JsonModelSpec extends Specification {

  "json model" should {

    "decode heading" in {
      val json: JsValue = JsObject(Seq(
        "elements" -> JsArray(Seq(JsObject(Seq(
          "type" -> JsString("heading"),
          "content" -> JsString("sheeit")))))));
      val blog = JsonModel.fromJson(json)
      blog must be equalTo Blog(Seq(Heading("sheeit")))
    }

    "decode paragraph" in {
      val json: JsValue = JsObject(Seq(
        "elements" -> JsArray(Seq(JsObject(Seq(
          "type" -> JsString("paragraph"),
          "content" -> JsString("sheeit")))))));
      val blog = JsonModel.fromJson(json)
      blog must be equalTo Blog(Seq(Paragraph("sheeit")))
    }

    "decode code style" in {
      val json: JsValue = JsObject(Seq(
        "elements" -> JsArray(Seq(JsObject(Seq(
          "type" -> JsString("codeSnippet"),
          "content" -> JsString("sheeit"),
          "lang" -> JsString("Java")))))));
      val blog = JsonModel.fromJson(json)
      blog must be equalTo Blog(Seq(CodeSnippet("sheeit", Java)))
    }
  }
}
