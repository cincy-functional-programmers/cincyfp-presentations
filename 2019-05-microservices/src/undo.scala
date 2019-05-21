package undo

import io.IO
import io.Implicits._
import syntax.Implicits._

sealed trait UndoResult[+E,+A]
case class Success[A](a: A) extends UndoResult[Nothing,A]
case class Undone[E](err: E) extends UndoResult[E,Nothing]
case class UndoError[E](err: E) extends UndoResult[E,Nothing]

case class UndoAction[E](log: String, action: IO[Either[E,Unit]])

class Undoable[E,A](val pgm: IO[(List[UndoAction[E]],Either[E,A])]) {
  def empty[E]: IO[Either[E,Unit]] = IO(Right(()))

  def runOrUndo: IO[UndoResult[E,A]] = pgm.flatMap {
    case (undo, result) =>
      result match {
        case Right(a) => IO { Success(a) }
        case Left(err) => undo.tail.foldLeft(empty[E]) { (undoPgm, undoAction) => undoPgm.flatMap {
          case Left(err) => IO(Left(err))
          case Right(_) => for {
            _ <- IO(println(undoAction.log))
            r <- undoAction.action
          } yield {
            r
          }
        }}.map {
          case Right(_) => Undone(err)
          case Left(err) => UndoError(err)
        }
      }
  }

  def map[B](f: A => B): Undoable[E,B] = new Undoable[E,B](pgm.map { case (undo, e) => (undo, e.map(f))})

  def flatMap[B](f: A => Undoable[E,B]): Undoable[E,B] = new Undoable(io.Implicits.IoOps.flatMap(pgm) {
    case (undoActions, e) => e match {
      case Right(a) => io.Implicits.IoOps.map(f(a).pgm) { case (moreUndo, result) => (moreUndo ++ undoActions) -> result }
      case Left(err) => IO(List(Undoable.noop[E]) -> Undoable.err[E,B](err))
    }
  })
}

object Undoable {
  def err[E,B](err: E): Either[E,B] = Left(err)
  def success[E]: IO[Either[E,Unit]] = IO(Right(()))
  def noop[E]: UndoAction[E] = UndoAction("noop", success[E])

  def withUndo[A](action: => A)(log: String, undo: => Unit): Undoable[Throwable,A] = {
    val actionPgm: IO[Either[Throwable,A]] = IO {
      try {
        Right(action)
      } catch {
        case t: Throwable => Left(t)
      }
    }

    val undoPgm = IO {
      try {
        Right(undo)
      } catch {
        case t: Throwable => Left(t)
      }
    }

    new Undoable(actionPgm.map(e => List(UndoAction(log,undoPgm)) -> e))
  }
}

object Example1 {
  import services._
  import Undoable._

  def main(args: Array[String]): Unit = {
    val pgm = for {
      _ <- withUndo(UserService.insert(4, "Meghan"))("Removing user 4", UserService.delete(4))
      _ <- withUndo(UserService.insert(5, "Bob"))("Removing user 5", UserService.delete(5))
      _ <- withUndo(TeamService.insert(3, "Team Cool", List(4, 5)))("Removing Team 3", TeamService.delete(3))
    } yield {

    }

    println(UserService.db)
    println(TeamService.db)
    val result = pgm.runOrUndo.unsafePerformIO()
    println(result)
    println(UserService.db)
    println(TeamService.db)
  }
}

object Example2 {
  import services._
  import Undoable._

  def main(args: Array[String]): Unit = {
    val pgm = for {
      _ <- withUndo(UserService.insert(4, "Meghan"))("Removing user 4", UserService.delete(4))
      _ <- withUndo(UserService.insert(1, "Bob"))("Removing user 5", UserService.delete(5))
      _ <- withUndo(TeamService.insert(3, "Team Cool", List(4, 5)))("Removing Team 3", TeamService.delete(3))
    } yield {

    }

    println(UserService.db)
    println(TeamService.db)
    val result = pgm.runOrUndo.unsafePerformIO()
    println(result)
    println(UserService.db)
    println(TeamService.db)
  }
}