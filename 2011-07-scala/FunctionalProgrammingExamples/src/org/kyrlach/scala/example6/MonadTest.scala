package org.kyrlach.scala.example6

object MonadTest {
  def main(args:Array[String]) = {
    val someValue = Option[String]("abc")
    val noValue = Option[String](null)
    System.out.println( (for {
      x <- someValue;
      y <- Some(x.trim);
      z <- Some(y.toUpperCase)
    } yield z).getOrElse("123"))
    System.out.println( (for {
      x <- noValue;
      y <- Some(x.trim)
      z <- Some(y.toUpperCase)
    } yield z).getOrElse("123"))
  }
}