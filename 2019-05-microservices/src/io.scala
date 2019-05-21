package io

import scala.io.StdIn

class IO[A](val unsafePerformIO: () => A)

object IO {
  def apply[A](effect: => A): IO[A] = new IO(() => effect)
}

object Implicits {
  import syntax.MonadOps

  implicit object IoOps extends MonadOps[IO] {
    def pure[A](a: A): IO[A] = IO(a)
    //def map[A,B](ma: IO[A])(f: A => B): IO[B] = IO(f(ma.unsafePerformIO()))
    def flatMap[A,B](ma: IO[A])(f: A => IO[B]): IO[B] = IO(
      f(ma.unsafePerformIO()).unsafePerformIO()
    )
  }
}



object Example {
  import Implicits._
  import syntax.Implicits._

  def launchNukes: Unit = println("launch all the things!")

  def main(args: Array[String]): Unit = {
    val pgm1 = for {
      name <- IO(StdIn.readLine("What is your name? "))
      _    <- IO(println(s"Hello, $name."))
    } yield {

    }

    pgm1.unsafePerformIO()

    def safePrintLine(s: String): IO[Unit] = IO(println(s))
    def safePrompt(text: String): IO[String] = IO(StdIn.readLine(text))

    def greet(name: String): IO[Unit] = safePrintLine(s"Hello, $name.")

    val pgm2 = safePrompt("What is your name? ") >>= greet

    pgm2.unsafePerformIO()
  }
}