package ru.mail.decoders

sealed trait UrlFormDecoderError { val msg: String }
object UrlFormDecoderError {
  final case class DecodingFailed(msg: String) extends UrlFormDecoderError
}
