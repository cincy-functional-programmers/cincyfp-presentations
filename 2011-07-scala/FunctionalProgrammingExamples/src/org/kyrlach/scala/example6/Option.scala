package org.kyrlach.scala.example6

object Option {
  def apply[A](x: A):Option[A] = if (x == null) None else Some(x)
}

sealed abstract class Option[+A] {
  def isEmpty:Boolean
  
  def map[B](f:(A => B)):Option[B] = {
    if (isEmpty) None else Some(f(this.get))
  }
  
  def flatMap[B](f:(A => Option[B])):Option[B] = {
    if (isEmpty) None else f(this.get)
  }
  
  def get:A
  
  def getOrElse[B >: A](default: => B):B = if (isEmpty) default else this.get
}

final case class Some[+A](x: A) extends Option[A] {
  def isEmpty = false
  def get = x
}

case object None extends Option[Nothing] {
  def isEmpty = true
  def get = throw new NoSuchElementException("None.get")
}