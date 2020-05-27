package test

import cats.Applicative
import io.circe.Json
import tofu.Raise
import tofu.syntax.raise._
import io.circe.parser.parse
import cats.syntax.either._

trait StringDecoder[F[_, _], R] {
  def from(s: String): F[String, R]
}

object StringDecoder {
  implicit def intDecoder[F[_, _]](
    implicit F: Applicative[F[String, *]],
    R: Raise[F[String, *], String]
  ): StringDecoder[F, Int] = (s: String) => s.toIntOption.orRaise(s"Cannot decoder '$s' as 'Int'")

  implicit def stringDecoder[F[_, _]](
    implicit F: Applicative[F[String, *]]
  ): StringDecoder[F, String] = (s: String) => F.pure(s)

  implicit def longDecoder[F[_, _]](
    implicit F: Applicative[F[String, *]],
    R: Raise[F[String, *], String]
  ): StringDecoder[F, Long] = (s: String) => s.toLongOption.orRaise(s"Cannot decode '$s' as 'Long'")

  implicit def jsonDecoder[F[_, _]](
    implicit F: Applicative[F[String, *]],
    R: Raise[F[String, *], String]
  ): StringDecoder[F, Json] = (s: String) => parse(s).leftMap(_.message).toRaise

}
