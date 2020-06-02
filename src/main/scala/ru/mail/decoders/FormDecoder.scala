package ru.mail.decoders

import cats.instances.either._
import cats.instances.list._
import cats.syntax.either._
import cats.syntax.option._
import cats.syntax.traverse._
import ru.mail.decoders.UrlFormDecoderError.DecodingFailed

trait FormDecoder[A] {
  def apply(s: List[String]): Either[UrlFormDecoderError, A]
}

object FormDecoder {
  def apply[A](implicit env: FormDecoder[A]): FormDecoder[A] =
    env

  implicit def valueFormDecoder[R](implicit d: StringDecoder[R]): FormDecoder[R] = {
    case head :: _ => d(head)
    case Nil       => DecodingFailed("No such value found").asLeft[R]
  }

  implicit def optionFormDecoder[R](implicit d: StringDecoder[R]): FormDecoder[Option[R]] = {
    case head :: _ => d(head).map(_.some)
    case Nil       => none[R].asRight[UrlFormDecoderError]
  }

  implicit def listFormDecoder[R](implicit d: StringDecoder[R]): FormDecoder[List[R]] = _.traverse(d(_))

}
