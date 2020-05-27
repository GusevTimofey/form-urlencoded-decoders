package test

import cats.effect.{ ExitCode, IO, IOApp }

object Appl extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    HttpServer[IO].run.compile.drain.as(ExitCode.Success)

  case class Test(a: Int, b: List[String], c: Option[String], d: String, e: Option[Int])

}
