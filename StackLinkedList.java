package nz.ac.auckland.se281.datastructures;

/**
 * A stack is a data structure that uses a last in first out implementation. It uses nodes to store
 * the data. This specific stack has been implemented with a single linked list and thus the class
 * is named StackLinkedList.
 */
public class StackLinkedList<T> {

  private class Node {
    protected T vertex;
    protected Node next;

    public Node(T inputVertex) {
      this.next = null;
      this.vertex = inputVertex;
    }

    public Node getNextNode() {
      return next;
    }

    public void setNextNode(Node node) {
      next = node;
    }

    public T getVertex() {
      return vertex;
    }
  }

  private int size = 0;
  private Node top = null;

  /**
   * Adds data to the top of the stack (push). Has O(1) complexity as we have a reference to the
   * top.
   *
   * @param vertex data that is added to the stack
   */
  public void push(T vertex) {
    Node temporaryNode = new Node(vertex);
    temporaryNode.setNextNode(top);
    top = temporaryNode;
    size++;
  }

  /** Removes the value at the top of the stack (pop). */
  public void pop() {
    if (size == 1) {
      top = null;
      size--;
    } else {
      top = top.getNextNode();
      size--;
    }
  }

  public T peek() {
    return top.getVertex();
  }

  /**
   * Returns true if the stack is empty.
   *
   * @return boolean
   */
  public boolean isStackEmpty() {
    if (size == 0) {
      return true;
    } else {
      return false;
    }
  }

  public int getSize() {
    return size;
  }
}
