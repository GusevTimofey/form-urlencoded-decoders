package ru.mail.decoders

import cats.effect.Sync
import org.http4s._

object formOf {
  def formOf[F[_]: Sync, V](implicit decoder: UrlFormDecoder[Either, V]): EntityDecoder[F, V] =
    UrlForm.entityDecoder.flatMapR { urlForm =>
      decoder
        .decode(urlForm.values)
        .fold(
          err => DecodeResult.failure(MalformedMessageBodyFailure(s"Incorrect request cause: $err.", None)),
          value => DecodeResult.success(value)
        )
    }
}
