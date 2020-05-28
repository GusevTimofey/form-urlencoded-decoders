package ru.mail.decoders

import cats.effect.{ ExitCode, IO, IOApp }

object Application extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    HttpServer[IO].run.compile.drain.as(ExitCode.Success)

  final case class Test(a: Int, b: List[String], c: Option[String], d: String, e: Option[Int])

}
