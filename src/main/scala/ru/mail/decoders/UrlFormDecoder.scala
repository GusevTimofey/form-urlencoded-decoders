package ru.mail.decoders

import cats.data.Chain
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.{ Applicative, Functor, Monad }
import shapeless._
import shapeless.labelled.{ field, FieldType }
import tofu.syntax.handle._
import tofu.syntax.raise._
import tofu.{ HandleTo, Raise }

trait UrlFormDecoder[F[_, _], T] {
  def decode(fields: Map[String, Chain[String]]): F[String, T]
}

object UrlFormDecoder {
  def apply[F[_, _], T](implicit env: UrlFormDecoder[F, T]): UrlFormDecoder[F, T] =
    env

  implicit def hnilDecoder[F[_, _]](
    implicit F: Applicative[F[String, *]]
  ): UrlFormDecoder[F, HNil] =
    (_: Map[String, Chain[String]]) => F.pure(HNil)

  implicit def hlistDecoder[F[_, _], Key <: Symbol, Head, Tail <: HList](
    implicit F: Monad[F[String, *]],
    FE: HandleTo[F[String, *], F[String, *], String],
    R: Raise[F[String, *], String],
    witness: Witness.Aux[Key],
    headDecoder: Lazy[FormDecoder[F, Head]],
    tailDecoder: UrlFormDecoder[F, Tail]
  ): UrlFormDecoder[F, FieldType[Key, Head] :: Tail] =
    (fields: Map[String, Chain[String]]) => {
      val fieldKey: String         = witness.value.name
      val fieldValue: List[String] = fields.getOrElse(fieldKey, Chain.empty).toList
      for {
        head <- headDecoder.value.from(fieldValue).handleWith { err: String =>
                 s"Error has occurred for key '$fieldKey': $err.".raise
               }
        tail <- tailDecoder.decode(fields)
      } yield field[Key](head) :: tail
    }

  implicit def genericDecoder[F[_, _], T, H](
    implicit generic: LabelledGeneric.Aux[T, H],
    F: Functor[F[String, *]],
    R: Raise[F[String, *], String],
    headDecoder: Lazy[UrlFormDecoder[F, H]]
  ): UrlFormDecoder[F, T] =
    (fields: Map[String, Chain[String]]) => headDecoder.value.decode(fields).map(generic.from)

}
