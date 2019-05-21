package either

sealed trait Either[+L,+R]
case class Left[L](l: L) extends Either[L,Nothing]
case class Right[R](r: R) extends Either[Nothing,R]

object Implicits {
  import syntax.MonadOps

  implicit def eitherOps[L]: MonadOps[Either[L,?]] = new MonadOps[Either[L,?]] {
    def pure[R](r: R): Either[L,R] = Right(r)
//    def map[R,S](ma: Either[L,R])(f: R => S): Either[L,S] = ma match {
//      case Left(l) => Left(l)
//      case Right(r) => Right(f(r))
//    }
    def flatMap[R,S](ma: Either[L,R])(f: R => Either[L,S]): Either[L,S] = ma match {
      case Left(l) => Left(l)
      case Right(r) => f(r)
    }
  }
}

object Example {
  import Implicits._
  import syntax.Implicits._

  def main(args: Array[String]): Unit = {
    val successInt: Either[String,Int] = Right(3)
    val successString: Either[String,String] = Right("hello")

    val failInt: Either[String,Int] = Left("I've got a bad feeling about this.")
    val failString: Either[String,Int] = Left("I'm endangering the mission; I shouldn't have come.")

    val test1 = for {
      count  <- successInt
      string <- successString
    } yield {
      (1 to count).map(_ => string).mkString
    }

    println(test1)

    val test2 = for {
      count  <- failInt
      string <- successString
    } yield {
      (1 to count).map(_ => string).mkString
    }

    println(test2)

    val test3 = for {
      count  <- successInt
      string <- failString
    } yield {
      (1 to count).map(_ => string).mkString
    }

    println(test3)

    val test4 = for {
      count  <- failInt
      string <- failString
    } yield {
      (1 to count).map(_ => string).mkString
    }

    println(test4)
  }
}