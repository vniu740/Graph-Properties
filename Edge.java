package nz.ac.auckland.se281.datastructures;

// import javax.print.attribute.standard.Destination;

/**
 * An edge in a graph that connects two verticies.
 *
 * <p>You must NOT change the signature of the constructor of this class.
 *
 * @param <T> The type of each vertex.
 */
public class Edge<T> {
  private T destination;
  private T source;

  public Edge(T source, T destination) {
    this.destination = destination;
    this.source = source;
  }

  public T getSource() {
    return source;
  }

  public T getDestination() {
    return destination;
  }

}
