package scalaz.zio.lockfree.impls

import scalaz.zio.lockfree.LockFreeQueue

class JCToolsQueue[A](desiredCapacity: Int) extends LockFreeQueue[A] {
  private val jctools = new org.jctools.queues.MpmcArrayQueue[A](desiredCapacity)

  override val capacity: Int = jctools.capacity()

  override def relaxedSize(): Int = jctools.size()

  override def enqueuedCount(): Long = jctools.currentProducerIndex()

  override def dequeuedCount(): Long = jctools.currentConsumerIndex()

  override def offer(a: A): Boolean = jctools.offer(a)

  override def poll(): Option[A] = Option(jctools.poll())

  override def isEmpty(): Boolean = jctools.isEmpty()

  override def isFull(): Boolean =
    jctools.currentProducerIndex() == jctools.currentConsumerIndex() + jctools.capacity() - 1
}
