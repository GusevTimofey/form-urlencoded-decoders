package tests

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import ru.mail.decoders.UrlFormDecoder
import tests.TestInstances.{ RequestForm, _ }

class DecoderTests extends AnyWordSpecLike with Matchers {

  "UrlFormDecoder" should {
    "decode input with all correct fields" in {
      UrlFormDecoder[RequestForm]
        .decode(correctAllFieldsInput)
        .exists(_ == correctAllFieldsOutput) shouldBe true
    }
    "decode input without some optional fields" in {
      UrlFormDecoder[RequestForm]
        .decode(correctWithoutSomeOptionalFieldsInput)
        .exists(_ == correctWithoutSomeOptionalFieldsOutput) shouldBe true
    }
    "decode input only with mandatory fields" in {
      UrlFormDecoder[RequestForm]
        .decode(correctWithoutOptionalFieldsInput)
        .exists(_ == correctWithoutOptionalFieldsOutput) shouldBe true
    }
    "decode input with empty list" in {
      UrlFormDecoder[RequestForm]
        .decode(correctWithEmptyListsFieldsInput)
        .exists(_ == correctWithEmptyListsFieldsOutput) shouldBe true
    }
    "not decode input with incorrect optional value 'key'" in {
      UrlFormDecoder[RequestForm].decode(incorrectWithIncorrectKeyFieldsInput).isLeft shouldBe true
    }
    "not decode input without mandatory field 'password'" in {
      UrlFormDecoder[RequestForm].decode(incorrectWithoutMandatoryPasswordFieldsInput).isLeft shouldBe true
    }
    "not decode input with inconsistent data in field 'amount'" in {
      UrlFormDecoder[RequestForm].decode(incorrectWithInconsistentAmountFieldsInput).isLeft shouldBe true
    }
    "not decode input with inconsistent data in field 'jsonField'" in {
      UrlFormDecoder[RequestForm].decode(incorrectWithInconsistentJsonFieldsInput).isLeft shouldBe true
    }
  }
}
