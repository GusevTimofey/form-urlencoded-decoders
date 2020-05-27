package tests

import cats.data.Chain
import io.circe.Json
import io.circe.parser.parse

object TestInstances {
  final case class RequestForm(
    user: String,
    password: Int,
    amount: Long,
    jsonField: Json,
    lang: Option[String],
    txId: Option[Int],
    key: Option[Long],
    jsonParam: Option[Json],
    argsString: List[String],
    argsLong: List[Long]
  )

  val correctAllFieldsInput: Map[String, Chain[String]] =
    Map(
      "user"       -> Chain("User_Field"),
      "password"   -> Chain("1234567890"),
      "amount"     -> Chain("987654321"),
      "jsonField"  -> Chain("""{"json" : "123"}"""),
      "lang"       -> Chain("language"),
      "txId"       -> Chain("987123465"),
      "key"        -> Chain("99998888777"),
      "jsonParam"  -> Chain("""{"123" : "321"}"""),
      "argsString" -> Chain("idk1", "idk2", "idk3", "idk4"),
      "argsLong"   -> Chain("123", "456", "34875")
    )

  val correctAllFieldsOutput: RequestForm = RequestForm(
    "User_Field",
    1234567890,
    987654321,
    parse("""{"json" : "123"}""").getOrElse(Json.Null),
    Some("language"),
    Some(987123465),
    Some(99998888777L),
    Some(parse("""{"123" : "321"}""").getOrElse(Json.Null)),
    List("idk1", "idk2", "idk3", "idk4"),
    List(123, 456, 34875)
  )

  val correctWithoutSomeOptionalFieldsInput: Map[String, Chain[String]] =
    Map(
      "user"       -> Chain("User_Field"),
      "password"   -> Chain("1234567890"),
      "amount"     -> Chain("987654321"),
      "jsonField"  -> Chain("""{"json" : "123"}"""),
      "lang"       -> Chain("language"),
      "txId"       -> Chain("987123465"),
      "argsString" -> Chain("idk1", "idk2", "idk3", "idk4"),
      "argsLong"   -> Chain("123", "456", "34875")
    )

  val correctWithoutSomeOptionalFieldsOutput: RequestForm = RequestForm(
    "User_Field",
    1234567890,
    987654321,
    parse("""{"json" : "123"}""").getOrElse(Json.Null),
    Some("language"),
    Some(987123465),
    None,
    None,
    List("idk1", "idk2", "idk3", "idk4"),
    List(123, 456, 34875)
  )

  val correctWithoutOptionalFieldsInput: Map[String, Chain[String]] =
    Map(
      "user"       -> Chain("User_Field"),
      "password"   -> Chain("1234567890"),
      "amount"     -> Chain("987654321"),
      "jsonField"  -> Chain("""{"json" : "123"}"""),
      "argsString" -> Chain("idk1", "idk2", "idk3", "idk4"),
      "argsLong"   -> Chain("123", "456", "34875")
    )

  val correctWithoutOptionalFieldsOutput: RequestForm = RequestForm(
    "User_Field",
    1234567890,
    987654321,
    parse("""{"json" : "123"}""").getOrElse(Json.Null),
    None,
    None,
    None,
    None,
    List("idk1", "idk2", "idk3", "idk4"),
    List(123, 456, 34875)
  )

  val correctWithEmptyListsFieldsInput: Map[String, Chain[String]] =
    Map(
      "user"       -> Chain("User_Field"),
      "password"   -> Chain("1234567890"),
      "amount"     -> Chain("987654321"),
      "jsonField"  -> Chain("""{"json" : "123"}"""),
      "argsLong"   -> Chain("123", "456", "34875")
    )

  val correctWithEmptyListsFieldsOutput: RequestForm = RequestForm(
    "User_Field",
    1234567890,
    987654321,
    parse("""{"json" : "123"}""").getOrElse(Json.Null),
    None,
    None,
    None,
    None,
    List(),
    List(123, 456, 34875)
  )

  val incorrectWithIncorrectKeyFieldsInput: Map[String, Chain[String]] =
    Map(
      "user"       -> Chain("User_Field"),
      "password"   -> Chain("1234567890"),
      "amount"     -> Chain("987654321"),
      "key"        -> Chain("qwe"),
      "jsonField"  -> Chain("""{"json" : "123"}"""),
      "argsLong"   -> Chain("123", "456", "34875")
    )

  val incorrectWithoutMandatoryPasswordFieldsInput: Map[String, Chain[String]] =
    Map(
      "user"       -> Chain("User_Field"),
      "amount"     -> Chain("987654321"),
      "jsonField"  -> Chain("""{"json" : "123"}"""),
      "argsLong"   -> Chain("123", "456", "34875")
    )

  val incorrectWithInconsistentAmountFieldsInput: Map[String, Chain[String]] =
    Map(
      "user"       -> Chain("User_Field"),
      "password"   -> Chain("1234567890"),
      "amount"     -> Chain("acvbdkfks"),
      "jsonField"  -> Chain("""{"json" : "123"}"""),
      "argsLong"   -> Chain("123", "456", "34875")
    )

  val incorrectWithInconsistentJsonFieldsInput: Map[String, Chain[String]] =
    Map(
      "user"       -> Chain("User_Field"),
      "password"   -> Chain("1234567890"),
      "amount"     -> Chain("987654321"),
      "jsonField"  -> Chain(""""{"json" : "123"}""""),
      "argsLong"   -> Chain("123", "456", "34875")
    )

}
