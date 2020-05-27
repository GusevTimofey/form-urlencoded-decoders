package ru.mail.decoders

import cats.syntax.either._
import io.circe.Json
import io.circe.parser.parse
import ru.mail.decoders.UrlFormDecoderError.{ JsonParsingFailed, NumberCastFailed }

trait StringDecoder[R] {
  def apply(s: String): Either[UrlFormDecoderError, R]
}

object StringDecoder {
  implicit final val intDecoder: StringDecoder[Int] =
    (s: String) => Either.fromOption(s.toIntOption, NumberCastFailed(s"Failed to cast $s to Int"))

  implicit final val stringDecoder: StringDecoder[String] = (s: String) => s.asRight[UrlFormDecoderError]

  implicit final val longDecoder: StringDecoder[Long] = (s: String) =>
    Either.fromOption(s.toLongOption, NumberCastFailed(s"Failed to cast $s to Long"))

  implicit final val jsonDecoder: StringDecoder[Json] = (s: String) =>
    parse(s).leftMap(err => JsonParsingFailed(err.message))

}
