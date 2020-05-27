package test

import cats.data.Kleisli
import cats.effect.{ Concurrent, ConcurrentEffect, Sync, Timer }
import cats.syntax.flatMap._
import cats.syntax.functor._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._
import org.http4s.{ HttpRoutes, _ }
import test.Appl.Test
import test.formOf._

import scala.concurrent.ExecutionContext

trait HttpServer[F[_]] {
  def run: fs2.Stream[F, Unit]
}

object HttpServer {
  def apply[F[_]: Sync: Concurrent: Timer: ConcurrentEffect]: HttpServer[F] = new HttpServer[F] {
    override def run: fs2.Stream[F, Unit] =
      BlazeServerBuilder(ExecutionContext.global)
        .bindHttp(8080, "localhost")
        .withHttpApp(httpApp)
        .serve
        .void

    val routes: HttpRoutes[F] = HttpRoutes.of[F] {
      case res @ POST -> Root / "test" =>
        import test.UrlFormDecoder.urlFormDecoderInstances._
        import test.FormDecoder.instances._
        import cats.instances.either._
        implicit val decoder: EntityDecoder[F, Test] = formOf[F, Test]
        for {
          v <- res.as[Test]
          _ <- Sync[F].delay(println(s"Received $v"))
        } yield Response[F]()
    }

    val httpApp: Kleisli[F, Request[F], Response[F]] = Router("" -> routes).orNotFound

  }
}
