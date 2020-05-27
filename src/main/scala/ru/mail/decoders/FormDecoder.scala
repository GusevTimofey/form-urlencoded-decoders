package ru.mail.decoders

import cats.Applicative
import cats.instances.list._
import cats.syntax.functor._
import cats.syntax.option._
import cats.syntax.traverse._
import tofu.Raise
import tofu.syntax.raise._

trait FormDecoder[F[_, _], A] {
  def from(s: List[String]): F[String, A]
}

object FormDecoder {
  def apply[F[_, _], A](implicit env: FormDecoder[F, A]): FormDecoder[F, A] =
    env

  implicit def valueFormDecoder[F[_, _], R](
    implicit F: Applicative[F[String, *]],
    R: Raise[F[String, *], String],
    d: StringDecoder[F, R]
  ): FormDecoder[F, R] = {
    case head :: _ => d.from(head)
    case Nil       => s"Empty input".raise
  }

  implicit def optionFormDecoder[F[_, _], R](
    implicit F: Applicative[F[String, *]],
    R: Raise[F[String, *], String],
    d: StringDecoder[F, R]
  ): FormDecoder[F, Option[R]] = {
    case head :: _ => d.from(head).map(_.some)
    case Nil       => F.pure(none[R])
  }

  implicit def listFormDecoder[F[_, _], R](
    implicit F: Applicative[F[String, *]],
    R: Raise[F[String, *], String],
    d: StringDecoder[F, R]
  ): FormDecoder[F, List[R]] = _.traverse(d.from)

}
