package ru.mail.decoders

sealed trait UrlFormDecoderError { val msg: String }
object UrlFormDecoderError {
  final case class EmptyInput(msg: String)          extends UrlFormDecoderError
  final case class NumberCastFailed(msg: String)    extends UrlFormDecoderError
  final case class JsonParsingFailed(msg: String)   extends UrlFormDecoderError
  final case class DecoderErrorWithKey(msg: String) extends UrlFormDecoderError
}
