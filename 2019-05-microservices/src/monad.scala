package syntax

trait MonadOps[M[_]] {
  def pure[A](a: A): M[A]
  //def map[A,B](ma: M[A])(f: A => B): M[B]
  def map[A,B](ma: M[A])(f: A => B): M[B] = flatMap(ma)(a => pure(f(a)))
  def flatMap[A,B](ma: M[A])(f: A => M[B]): M[B]
}

trait MonadSyntax[M[_],A] {
  def map[B](f: A => B): M[B]
  def flatMap[B](f: A => M[B]): M[B]
  def >>=[B](f: A => M[B]): M[B] = flatMap(f)
}

object Implicits {
  implicit def syntax[M[_],A](ma: M[A])(implicit ev: MonadOps[M]): MonadSyntax[M,A] = new MonadSyntax[M,A] {
    def map[B](f: A => B): M[B] = ev.map(ma)(f)
    def flatMap[B](f: A => M[B]): M[B] = ev.flatMap(ma)(f)
  }
}

case class Id[A](a: A)

object Example {
  import Implicits._

  implicit object IdOps extends MonadOps[Id] {
    def pure[A](a: A): Id[A] = Id(a)
    //def map[A,B](ma: Id[A])(f: A => B): Id[B] = Id(f(ma.a))
    def flatMap[A,B](ma: Id[A])(f: A => Id[B]): Id[B] = Id(f(ma.a).a)
  }

  def main(args: Array[String]): Unit = {
    val a: Id[Int] = IdOps.pure(4)
    val b: Id[String] = IdOps.pure("abc")

    val c = for {
      count  <- a
      string <- b
    } yield {
      (1 to count).map(_ => string).mkString
    }

    println(c)
  }
}

