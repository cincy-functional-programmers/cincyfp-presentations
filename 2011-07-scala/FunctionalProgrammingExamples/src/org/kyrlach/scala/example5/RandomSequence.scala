package org.kyrlach.scala.example5

import java.util.Random

import scala.collection.immutable.Stream

object RandomSequence {
  val r:Random = new Random();
  def apply(x:Int):Stream[Int] = Stream.cons(r.nextInt(x), apply(x));
}