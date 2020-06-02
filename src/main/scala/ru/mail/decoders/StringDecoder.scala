package ru.mail.decoders

import cats.syntax.either._
import io.circe.Json
import io.circe.parser.parse
import ru.mail.decoders.UrlFormDecoderError.DecodingFailed

trait StringDecoder[R] { self =>
  def apply(s: String): Either[UrlFormDecoderError, R]
  def map[B](f: R => B): StringDecoder[B] =
    (s: String) => self(s).flatMap(v => Either.catchNonFatal(f(v)).leftMap(err => DecodingFailed(err.getMessage)))
}

object StringDecoder {
  implicit final val stringDecoder: StringDecoder[String] = (s: String) => s.asRight[UrlFormDecoderError]

  implicit final val intDecoder: StringDecoder[Int] = stringDecoder.map(_.toInt)

  implicit final val longDecoder: StringDecoder[Long] = stringDecoder.map(_.toLong)

  implicit final val jsonDecoder: StringDecoder[Json] = (s: String) =>
    parse(s).leftMap(err => DecodingFailed(err.message))
}
