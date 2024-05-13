package nz.ac.auckland.se281.datastructures;

/**
 * A queue is a data structure that uses the first in first out implementaion. It uses nodes to
 * store data.
 */
public class QueueDoubleLinkedList<T> {
  private class Node {
    protected Node previous;
    protected Node next;
    protected T vertex;

    public Node(T vertex, Node next, Node previous) {
      this.vertex = vertex;
      this.next = next;
      this.previous = previous;
    }
  }

  private Node head = null;
  private Node tail = null;
  private int size = 0;

  /**
   * Adds inputted data to the back of the queue. O(1) complexity as we know where the tail of the
   * queue is
   *
   * @param vertex is added to the queue
   */
  public void enqueue(T vertex) {
    // Create a new node with the data inputted
    Node temporaryNode = new Node(vertex, null, tail);

    // If no tail exists, set the next tail as the new node
    if (tail != null) {
      tail.next = temporaryNode;
    }
    tail = temporaryNode;

    // If no head exists set the head as the new node
    if (head == null) {
      head = temporaryNode;
    }

    size++;
  }

  /**
   * Removes the first value of the queue and sets the second element of the queue to the first i.e
   * dequeue's the queue. Has O(1) complexity as we know have references to the heads and tails of
   * proceeding elements of the queue.
   */
  public void dequeue() {
    // If the size of the queue is 1, dequeuing will make the queue empty so no head or tail will
    // exist
    if (size == 1) {
      head = null;
      tail = null;
      size--;
    } else {
      // Set the head to the next head and set the previous head to null
      head = head.next;
      head.previous = null;
      size--;
    }
  }

  public int size() {
    return size;
  }

  /**
   * Returns a boolean for if the queue is empty.
   *
   * @return boolean
   */
  public boolean isEmpty() {
    if (size == 0) {
      return true;
    } else {
      return false;
    }
  }

  public T peek() {
    return head.vertex;
  }
}
