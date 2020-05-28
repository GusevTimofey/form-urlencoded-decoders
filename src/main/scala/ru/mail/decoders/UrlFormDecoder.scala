package ru.mail.decoders

import cats.data.Chain
import cats.syntax.either._
import ru.mail.decoders.UrlFormDecoderError.DecoderErrorWithKey
import shapeless._
import shapeless.labelled.{ field, FieldType }

trait UrlFormDecoder[T] {
  def decode(fields: Map[String, Chain[String]]): Either[UrlFormDecoderError, T]
}

object UrlFormDecoder {
  def apply[T](implicit env: UrlFormDecoder[T]): UrlFormDecoder[T] =
    env

  implicit def hnilDecoder: UrlFormDecoder[HNil] =
    (_: Map[String, Chain[String]]) => HNil.asRight[UrlFormDecoderError]

  implicit def hlistDecoder[Key <: Symbol, Head, Tail <: HList](
    implicit witness: Witness.Aux[Key],
    headDecoder: Lazy[FormDecoder[Head]],
    tailDecoder: UrlFormDecoder[Tail]
  ): UrlFormDecoder[FieldType[Key, Head] :: Tail] =
    (fields: Map[String, Chain[String]]) => {
      val fieldKey: String         = witness.value.name
      val fieldValue: List[String] = fields.getOrElse(fieldKey, Chain.empty).toList
      for {
        head <- headDecoder
                 .value(fieldValue)
                 .leftMap(err => DecoderErrorWithKey(s"For key '$fieldKey' error: ~ ${err.msg} ~ has occurred."))
        tail <- tailDecoder.decode(fields)
      } yield field[Key](head) :: tail
    }

  implicit def genericDecoder[T, H](
    implicit generic: LabelledGeneric.Aux[T, H],
    headDecoder: Lazy[UrlFormDecoder[H]]
  ): UrlFormDecoder[T] =
    (fields: Map[String, Chain[String]]) => headDecoder.value.decode(fields).map(generic.from)

}
