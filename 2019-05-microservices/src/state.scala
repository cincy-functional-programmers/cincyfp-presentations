package state

class State[S,A](val run: S => (S,A)) {
  def runS(s: S): S = run(s)._1
  def runA(s: S): A = run(s)._2
}

object State {
  def put[S,A](a: A): State[S,A] = new State[S,A](s => (s, a))
  def get[S]: State[S,S] = new State[S,S](s => (s,s))
  def inspect[S,A](f: S => A): State[S,A] = new State[S,A](s => (s,f(s)))
  def modify[S](f: S => S): State[S,Unit] = new State[S,Unit](s => (f(s),()))
  def postMod[S](f: S => S): State[S,S] = new State[S,S](s => {
    val s1 = f(s)
    (s1,s1)
  })
}

object Implicits {
  import syntax.MonadOps

  implicit def stateOps[S]: MonadOps[State[S,?]] = new MonadOps[State[S,?]] {
    def pure[A](a: A): State[S,A] = State.put(a)
//    def map[A,B](ma: State[S,A])(f: A => B): State[S,B] = new State(s => {
//      val (s1,a) = ma.run(s)
//      (s1,f(a))
//    })
    def flatMap[A,B](ma: State[S,A])(f: A => State[S,B]): State[S,B] = new State(s => {
      val (s1, a) = ma.run(s)
      f(a).run(s1)
    })
  }
}

object Example {
  import Implicits._
  import syntax.Implicits._

  sealed trait TransactionResult
  case object Overdraw extends TransactionResult
  case class CurrentBalance(x: Int) extends TransactionResult

  def addToTotal(x: Int): State[Int,TransactionResult] = for {
    _       <- State.modify[Int](_ + x)
    balance <- State.get[Int]
  } yield {
    if(balance >= 0) CurrentBalance(balance) else Overdraw
  }

  def addToTotal2(x: Int): State[Int,TransactionResult] = State.postMod[Int](_ + x)
    .map(balance => if(balance <= 0) Overdraw else CurrentBalance(balance))

  def main(args: Array[String]): Unit = {
    val pgm1 = for {
      r1 <- addToTotal(100)
      r2 <- addToTotal(-176)
    } yield {
      List(r1,r2)
    }

    println(pgm1.run(42))
    println(pgm1.run(-142))
    println(pgm1.run(77))
    println(pgm1.run(9999))
  }
}