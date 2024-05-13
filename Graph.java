package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A graph that is composed of a set of verticies and edges.
 *
 * <p>You must NOT change the signature of the existing methods or constructor of this class.
 *
 * @param <T> The type of each vertex, that have a total ordering.
 */
public class Graph<T extends Comparable<T>> {

  private Set<T> verticies;
  private Set<Edge<T>> edges;
  private Map<T, ArrayList<T>> hashMapAscending = new HashMap<T, ArrayList<T>>();
  private Map<T, ArrayList<T>> hashMapDescending = new HashMap<T, ArrayList<T>>();

  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
  }

  /**
   * Returns a set of the roots of a graph where a vertex is considered to be a root if (1) the in
   * degree of a node is 0 or (2) the vertex is part of an equivelance class. If the vertex is part
   * of a equivalence class the minimum value of the class is returned as the root
   *
   * @return a set of verticies of type T that are considered the roots of a graph
   */
  public Set<T> getRoots() {
    Set<T> roots = new LinkedHashSet<>();

    // Add all destinations of edges that match the vertex to a set
    for (T vertex : verticies) {
      Set<T> setOfDestination = new HashSet<>();
      if (isEquivalence() == false) {
        // Check if for the edges where the destination is the vertex
        for (Edge<T> edge : edges) {
          if (edge.getDestination() == vertex) {
            setOfDestination.add(edge.getDestination());
          }
        }
        // If the set is empty then the vertex has an in-degree of 0 and is a root so add it to the
        // roots set
        if (setOfDestination.isEmpty() == true) {
          roots.add(vertex);
        }
      }
    }

    // Loop through all the verticies and add the minimum value of each equivalence class to the
    // roots set
    for (T vertex : verticies) {
      if (!getEquivalenceClass(vertex).isEmpty()) {
        roots.add(Collections.min(getEquivalenceClass(vertex)));
      }
    }

    ArrayList<T> arrayList = new ArrayList<>(roots);
    for (int i = 0; i < arrayList.size(); i++) {
      for (int j = 0; j < arrayList.size(); j++) {
        if ((arrayList.get(i) instanceof String) && (arrayList.get(j) instanceof String)) {
          T holder;
          String tempOne = (String) arrayList.get(i);
          String tempTwo = (String) arrayList.get(j);
          if (Integer.parseInt(tempOne) < Integer.parseInt(tempTwo)) {
            holder = arrayList.get(i);
            arrayList.set(i, arrayList.get(j));
            arrayList.set(j, holder);
          }
        }
      }
    }
    roots.clear();
    for (T element : arrayList) {
      roots.add(element);
    }

    return roots;
  }

  /**
   * Returns a boolean for if the graph is reflexive. A reflexive graph is one that satisifies the
   * relation, for all verticies in the graph, there must be an edge from each vertex to itself
   *
   * @return boolean if the graph is reflexive or not
   */
  public boolean isReflexive() {
    // Loop through all the verticies
    for (T vertexChecking : verticies) {
      // Create a new Hashset
      Set<T> setOfDestination = new HashSet<>();
      // Loop through all the edges, if the edge is equal to the vertex we are checking add the edge
      // destination to the set
      for (Edge<T> edge : edges) {
        if (edge.getSource() == vertexChecking) {
          setOfDestination.add(edge.getDestination());
        }
      }
      // If the set of edge destinations DOES NOT contain a destination which is the same as the
      // vertex we are checking, return false
      if (!setOfDestination.contains(vertexChecking)) {
        return false;
      }
    }
    // If all verticies contain an edge with a source and a destination of its own vertex return
    // true
    return true;
  }

  /**
   * Returns a boolean for if the graph is symmetric. A symmetric graph is one that satisfies the
   * relation, for all edges in the graph if an edge exists from a -> b, then there must exist an
   * edge from b -> a.
   *
   * @return boolean for if the graph is symmetric
   */
  public boolean isSymmetric() {
    // Loop through edges, focusing on edge 1
    for (Edge<T> edgeChecking : edges) {
      Set<T> sourcesOfDestination = new HashSet<>();
      // Loop through edges to compare edge 1 to edge 2
      for (Edge<T> edgeCheckingForSource : edges) {
        // If the destination of edge 1 is = the source of edge 2 (i.e the edges coming out of the
        // destination of edge 1)
        if (edgeCheckingForSource.getSource() == edgeChecking.getDestination()) {
          // add the destinations of edge 2 to the set
          sourcesOfDestination.add(edgeCheckingForSource.getDestination());
        }
      }
      // if the destinations of edge 2 dont contain the source of edge 1, the graph is not symmetric
      if (!sourcesOfDestination.contains(edgeChecking.getSource())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns a boolean for if the graph is anti-symmetric. A graph is anti-symmetric if it satisfies
   * the relation, for all edges in tha graph ig there exists an edge from a -> b then the vertcies
   * a = b.
   *
   * @return boolean for if the graph is anti-symmetric
   */
  public boolean isAntiSymmetric() {
    // Loop through all the edges twice
    for (Edge<T> edge : edges) {
      for (Edge<T> edgeTwo : edges) {
        // if the source of the second edge is = to the source of the first edge, and the
        // destination of the second edge is equal to the destination of the first edge
        if ((edgeTwo.getSource() == edge.getDestination())
            && (edgeTwo.getDestination() == edge.getSource())) {
          // check if the source and destination of the first edge are equal
          if (edge.getSource() != edge.getDestination()) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Returns a boolean for if the graph is transitive. A transitive graph satisfies the relation,
   * for all edges in the graph, if an edges exists from a -> b and b -> c there there must exist an
   * edge from a -> c.
   *
   * @return boolean for if graph is transitive.
   */
  public boolean isTransitive() {
    // Loop through all the verticies
    for (T firstVertex : verticies) {
      Set<T> setOfDestinationForVertex = new HashSet<>();
      Set<T> setOfDestinationForVertextDestination = new HashSet<>();

      // Get a set of all the destinations of the vertex (destinations 1), not including self loops
      for (Edge<T> edge : edges) {
        if ((edge.getSource().equals(firstVertex))
            && (!(edge.getDestination().equals(firstVertex)))) {
          setOfDestinationForVertex.add(edge.getDestination());
        }
      }

      // Loop through all the destinations of the vertex. For each destination get a set of their
      // own destination (destinations 2), not including self loops
      for (T destinationOfVertexDestination : setOfDestinationForVertex) {
        for (Edge<T> edge : edges) {
          if ((edge.getSource().equals(destinationOfVertexDestination))
              && (!(edge.getDestination().equals(destinationOfVertexDestination)))) {
            if (!(edge.getDestination().equals(firstVertex))) {
              setOfDestinationForVertextDestination.add(edge.getDestination());
            }
          }
        }
        // Loop through the destinations (destinations 2) to get destination 3 and if the set of the
        // destinations of the initial vertex (destinations 1) does not contain this destination 3,
        // then the graph is not transitive
        if (!(setOfDestinationForVertextDestination.isEmpty())) {
          for (T destination : setOfDestinationForVertextDestination) {
            if (!(setOfDestinationForVertex.contains(destination))) {
              return false;
            }
          }
        }
        setOfDestinationForVertextDestination.clear();
      }
      setOfDestinationForVertex.clear();
    }
    return true;
  }

  /**
   * Returns a boolean for if the graph is an equivelance relation. A graph that is an equivalence
   * relation is satisfies all three relations; reflexivity, symmetry and transitivity.
   *
   * @return boolean for if the graph is an equivelance relation
   */
  public boolean isEquivalence() {
    // If the relation is symmetric, reflexive and transitive return true and false otherwise
    if ((isSymmetric() == true) && (isReflexive() == true) && (isTransitive() == true)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns a set of verticies which are in the equivalence class of the given vertex. A vertex
   * will only have an equivalance class if the graph is an equivalence relation.
   *
   * @param vertex a vertex which we want the equivalance class of
   * @return set of verticies which are in the equivalence class of the given vertex
   */
  public Set<T> getEquivalenceClass(T vertex) {
    Set<T> equivalenceClass = new HashSet<>();

    // If the relation is not an equivalence relation return false
    if (isEquivalence() == false) {
      return equivalenceClass;
    } else {
      // Loop through all the edges
      for (Edge<T> edge : edges) {
        // If the vertex inputted = the source of the edge, add the edge destination to the set
        if (vertex.equals(edge.getSource())) {
          equivalenceClass.add(edge.getDestination());
        }
      }
      return equivalenceClass;
    }
  }

  /**
   * Helper method to sort verticies and edges into a hashmap where the keys are the verticies and
   * the values are an array of destinations of edges for each vertex (in ascending order).
   */
  public void createHashMapAscending() {
    // Create a new key/arraylist pair in the hashmap where the key is the vertex and the arraylist
    // holds all the destinations of the edges that stem from the vertex
    for (Edge<T> edge : edges) {
      hashMapAscending.put(edge.getSource(), new ArrayList<T>());
    }
    // Loop through each edge and add its destination to the arraylist value corresponding to the
    // key of the Source of the vertex
    for (Edge<T> edge : edges) {
      hashMapAscending.get(edge.getSource()).add(edge.getDestination());
    }
    // Loop through the edges and sort each arraylist in ascending order using the source of the
    // edge as the key
    for (Edge<T> edge : edges) {
      Collections.sort(hashMapAscending.get(edge.getSource()));
    }
    // Sort each array from each value
    for (T key : hashMapAscending.keySet()) {
      // Create a temportary arraylist
      ArrayList<T> hashMapAscendingTemp = new ArrayList<T>();
      for (T element : hashMapAscending.get(key)) {
        hashMapAscendingTemp.add(element);
      }
      T elementTwo = hashMapAscendingTemp.get(hashMapAscendingTemp.size() - 1);
      // Check if each element in the array is a string
      for (T elementOne : hashMapAscendingTemp) {
        // Compare each element to the last element of the original array, if it is larger more it
        // to the enf of the array
        if (elementOne instanceof String) {
          if (elementTwo instanceof String) {
            String tempOne = (String) elementOne;
            String tempTwo = (String) elementTwo;
            if (Integer.valueOf(tempOne) > Integer.valueOf(tempTwo)) {
              hashMapAscending.get(key).remove(elementOne);
              hashMapAscending.get(key).add(elementOne);
            }
          }
        }
      }
    }
  }

  /**
   * Helper method to sort verticies and edges into a hashmap where the keys are the verticies and
   * the values are an array of destinations of edges for each vertex (in descending order).
   */
  public void createHashMapDescending() {
    // Create a new key/arraylist pair in the hashmap where the key is the vertex and the arraylist
    // holds all the destinations of the edges that stem from the vertex
    for (Edge<T> edge : edges) {
      hashMapDescending.put(edge.getSource(), new ArrayList<T>());
    }
    // Loop through each edge and add its destination to the arraylist value corresponding to the
    // key of the Source of the vertex
    for (Edge<T> edge : edges) {
      hashMapDescending.get(edge.getSource()).add(edge.getDestination());
    }
    // Loop through the edges and sort each arraylist in descending order using the source of the
    // edge as the key
    for (Edge<T> edge : edges) {
      Collections.sort(hashMapDescending.get(edge.getSource()), Collections.reverseOrder());
    }
    // Sort each array for each given value
    for (T key : hashMapDescending.keySet()) {
      // Create a temporary array
      ArrayList<T> hashMapDescendingTemp = new ArrayList<T>();
      for (T element : hashMapDescending.get(key)) {
        hashMapDescendingTemp.add(element);
      }
      T elementTwo = hashMapDescendingTemp.get(hashMapDescendingTemp.size() - 1);

      for (T elementOne : hashMapDescendingTemp) {
        // Check if each lement is a string
        if (elementOne instanceof String) {
          if (elementTwo instanceof String) {
            String tempOne = (String) elementOne;
            String tempTwo = (String) elementTwo;
            // If the first elements is greater than the second element, swap the elements.
            if (Integer.valueOf(tempTwo) > Integer.valueOf(tempOne)) {
              hashMapDescending.get(key).remove(elementTwo);
              hashMapDescending.get(key).add(0, elementTwo);
            }
          }
        }
      }
    }
  }

  /**
   * Returns a list of verticies in a iterative breadth first search traversal order. Traversal
   * starts at the root or any vertex(given no roots) and traverses each level of verticies before
   * moving on.
   *
   * @return a list of verticies in a breadth first search traversal order.
   */
  public List<T> iterativeBreadthFirstSearch() {
    List<T> visitedVerticies = new ArrayList<>();
    QueueDoubleLinkedList<T> queue = new QueueDoubleLinkedList<>();
    T root;
    createHashMapAscending();

    Set<T> rootsSet = getRoots();

    // If there are no roots, start at the first vertex in the set
    if (rootsSet.isEmpty()) {
      root = verticies.iterator().next();
      queue.enqueue(root);
      visitedVerticies = useQueue(visitedVerticies, queue);
    } else {
      // Loop through all the roots so that even if two roots are isolated from one another they are
      // all traversed
      for (T rootStarting : rootsSet) {
        // Queue the root onto int the queue so the while loop in the useQueue method can begin
        queue.enqueue(rootStarting);
        // Get the list of visited verticies by calling the useQueue method
        visitedVerticies = useQueue(visitedVerticies, queue);
      }
    }
    return visitedVerticies;
  }

  /**
   * Returns a list of verticies in a depth first search traversal order using an iterative
   * approach. Traversal starts at the root or any vertex(given no roots) and explores each vertex
   * until a vertex has no more outgoing edges, it then returns to its parent vertexs repeats the
   * exploration.
   *
   * @return a list of verticies in a depth first search traversal order
   */
  public List<T> iterativeDepthFirstSearch() {
    List<T> visitedVerticies = new ArrayList<>();
    StackLinkedList<T> stack = new StackLinkedList<>();
    T root;

    createHashMapDescending();

    Set<T> rootsSet = getRoots();

    // If there are no roots, start at the first vertex in the set
    if (rootsSet.isEmpty()) {
      root = verticies.iterator().next();
      stack.push(root);
      visitedVerticies = useStack(visitedVerticies, stack);
    } else {
      // Loop through all the roots so that even if two roots are isolated from one another they are
      // all traversed
      for (T rootStarting : rootsSet) {
        // Push the root onto the stack so the while loop in the useStack method can begin
        stack.push(rootStarting);
        // Get the list of visited verticies by calling the useStack method
        visitedVerticies = useStack(visitedVerticies, stack);
      }
    }
    return visitedVerticies;
  }

  /**
   * Returns a list of verticies in a breadth first search traversal order using a recursive
   * approach. Traverseal starts at the root or any vertex(given no roots) and explores each level
   * of verticies before moving on.
   *
   * @return a list of verticies in a breadth first search traversal order
   */
  public List<T> recursiveBreadthFirstSearch() {
    List<T> visitedVerticies = new ArrayList<>();
    T root;
    QueueDoubleLinkedList<T> queue = new QueueDoubleLinkedList<>();

    Set<T> rootsSet = getRoots();
    createHashMapAscending();

    // If there are no roots, call the recursive function on the first vertex in the set of
    // verticies
    if (rootsSet.isEmpty()) {
      root = verticies.iterator().next();
      queue.enqueue(root);
      visitedVerticies = breadthFirstSearchRecursive(visitedVerticies, root, queue);
    } else {
      // Loop through all the roots so that even if two roots are isolated from one another they are
      // all traversed
      for (T rootStarting : rootsSet) {
        // Add root to queue so that the recursive function can work
        queue.enqueue(rootStarting);
        visitedVerticies = breadthFirstSearchRecursive(visitedVerticies, rootStarting, queue);
      }
    }
    return visitedVerticies;
  }

  /**
   * A recursive method that returns a list of verticies in a breadth first search traversal order.
   * Function recursiveBreadthFirstSearch() calls this recursive function.
   *
   * @param visitedVerticiesList List that explored verticies are stored in
   * @param vertex Vertex that is being explored for the breadth first search
   * @param queue Data structure to help aid in the traversal
   * @return A list of verticies in a breadth first search traversal order
   */
  public List<T> breadthFirstSearchRecursive(
      List<T> visitedVerticiesList, T vertex, QueueDoubleLinkedList<T> queue) {
    ArrayList<T> listOfDestinations = hashMapAscending.get(queue.peek());
    T rootExploring;

    // If the vextex being explored hasn't been visited before, add it to the list.
    if (!(visitedVerticiesList.contains(vertex))) {
      visitedVerticiesList.add(vertex);
    }
    // If the vertex has other verticies connecting to it, add them to the queue in ascending order
    if (listOfDestinations != null) {
      for (T vertexAdded : listOfDestinations) {
        if (!(visitedVerticiesList.contains(vertexAdded))) {
          queue.enqueue(vertexAdded);
        }
      }
    }
    // Dequeue the vertex we are exploring
    queue.dequeue();

    // If the queue is not empty, recursively call the function with the peak of the queue
    if (queue.size() != 0) {
      rootExploring = queue.peek();

      breadthFirstSearchRecursive(visitedVerticiesList, rootExploring, queue);
    }

    return visitedVerticiesList;
  }

  /**
   * Returns a list of verticies in a depth first search traversal order using a recursive approach.
   * Traversal starts at the root or any vertex(given no roots) and explores each vertex until a
   * vertex has no more outgoing edges, it then returns to the parent vertex and repeats the
   * exploration.
   *
   * @return A list of verticies in a depth first search traversal order
   */
  public List<T> recursiveDepthFirstSearch() {
    List<T> visitedVerticies = new ArrayList<>();
    T root;
    StackLinkedList<T> stack = new StackLinkedList<>();

    Set<T> rootsSet = getRoots();
    createHashMapDescending();

    // If there are no roots, call the recursive function on the first vertex in the set of
    // verticies
    if (rootsSet.isEmpty()) {
      root = verticies.iterator().next();
      stack.push(root);
      visitedVerticies = depthFirstSearchRecursive(visitedVerticies, root, stack);
    } else {
      // Loop through all the roots so that if even if two roots are isolated from one another they
      // are all
      // traversed
      for (T rootStarting : rootsSet) {
        // Add the root to the stack so the recursive function can start
        stack.push(rootStarting);
        visitedVerticies = depthFirstSearchRecursive(visitedVerticies, rootStarting, stack);
      }
    }
    return visitedVerticies;
  }

  /**
   * A recursive method that returns a list of verticies in a depth first search traversal order.
   * Function recursiveDepthFirstSearch() calls this recursive function.
   *
   * @param visitedVerticiesList List that explored verticies are stored in
   * @param vertex vertex that is being explored for the depth first search
   * @param stack data structure used to aid depth first search
   * @return A list of verticies in a depth first search traversal order.
   */
  public List<T> depthFirstSearchRecursive(
      List<T> visitedVerticiesList, T vertex, StackLinkedList<T> stack) {
    ArrayList<T> listOfDestinations = hashMapDescending.get(stack.peek());
    T rootExploring;
    // Add the vertex to the vistedVerticies if it has not already been visited
    if (!(visitedVerticiesList.contains(vertex))) {
      visitedVerticiesList.add(vertex);
    }

    // Remove the top vertex from the stack
    stack.pop();

    // if the vertex has other verticies connected to it (destinations) and they have not been
    // visited before, add them to the stack in descending order
    if (listOfDestinations != null) {
      for (T destination : listOfDestinations) {
        if (!(visitedVerticiesList.contains(destination))) {
          stack.push(destination);
        }
      }
    }

    // If the stack is not empty, call the function recusively using the peek of the stack as the
    // new exploration vertex
    if (stack.getSize() != 0) {
      rootExploring = stack.peek();
      depthFirstSearchRecursive(visitedVerticiesList, rootExploring, stack);
    }

    return visitedVerticiesList;
  }

  /**
   * Method that uses a queue to perform a breadth first search on a graph.
   *
   * @param queue Instance of a QueueDoubleLinkedList which is used to perform a breadth first
   *     search
   * @return A list of verticies in a breadth first search traversal order
   */
  public List<T> useQueue(List<T> visitedList, QueueDoubleLinkedList<T> queue) {
    T vertexExploring;
    T vertexConnected;
    ArrayList<T> arrayList;

    while (!queue.isEmpty()) {
      // Get the vertex we are exploring from the queu
      vertexExploring = queue.peek();
      // Add the vertex to the visited verticies if it has not been visited before
      if (!(visitedList.contains(vertexExploring))) {
        visitedList.add(vertexExploring);
      }
      // Add all the verticies connected to the vertext we are exploring to the queue
      arrayList = hashMapAscending.get(vertexExploring);
      if (!(arrayList == null)) {
        for (int i = 0; i < arrayList.size(); i++) {
          vertexConnected = arrayList.get(i);
          if (!(visitedList.contains(vertexConnected))) {
            queue.enqueue(vertexConnected);
          }
        }
      }
      // Remove the vertex we are exploring from the queue
      queue.dequeue();
    }
    return visitedList;
  }

  /**
   * Method that uses a stack to perform a depth first search on a graph.
   *
   * @param list List that explored verticies are stored in
   * @param stack Instance of a StackLinkedList which is used to perform a breadth first search
   * @return A list of verticies in a depth first search traversal order
   */
  public List<T> useStack(List<T> list, StackLinkedList<T> stack) {
    T vertexExploring;
    ArrayList<T> arrayList;

    while (stack.isStackEmpty() == false) {
      vertexExploring = stack.peek();

      if (!(list.contains(vertexExploring))) {
        list.add(vertexExploring);
      }

      // remove the vertex from the top of the stack since it has been visited
      stack.pop();

      // Add the verticies of the vertex we are exploring to the stack if they have not been visited
      // before
      arrayList = hashMapDescending.get(vertexExploring);
      if (!(arrayList == null)) {
        for (int i = 0; i < arrayList.size(); i++) {
          if (!(list.contains(arrayList.get(i)))) {
            stack.push(arrayList.get(i));
          }
        }
      }
    }
    return list;
  }
}
