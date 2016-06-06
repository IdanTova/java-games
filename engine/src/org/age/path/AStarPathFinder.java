package org.age.path;

import javafx.collections.transformation.SortedList;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AStarPathFinder {
  private AStarMap map;
  private Point start;
  private Point target;
  Node startNode;
  Node targetNode;
//  int[] dx = new int[]{-1, +0, +1, -1, +0, +1, -1, +0, +1};
//  int[] dy = new int[]{-1, -1, -1, +0, +0, +0, +1, +1, +1};
  int[] dx = new int[]{ +0, -1, +0, +1, +0};
  int[] dy = new int[]{ -1, +0, +0, +0, +1};
  HashMap<Point, Node> nodeCache = new HashMap<>();

  public AStarPathFinder(AStarMap map, Point start, Point target) {
    this.map = map;
    this.start = start;
    this.target = target;
    startNode = getNode(start, null);
    targetNode = getNode(target, null);
  }

  private Node getNode(Point p, Node current) {
    Node node = nodeCache.get(p);
    if (node == null) {
      node = new Node(p, current);
      nodeCache.put(p, node);
    }
    return node;
  }

  public List<Point> computeShortestPath() {
    Set<Node> open = new HashSet<>();
    Set<Node> closed = new HashSet<>();
    open.add(startNode);

    while (!open.isEmpty()) {
      Node current = findMin(open);
      open.remove(current);
      closed.add(current);

      if (current.position.equals(targetNode.position)) {
        return reconstructPath(current);
      }

      for (int i = 0; i < 5; i++) {
        Node neighbor = getNode(new Point(current.position.x + dx[i], current.position.y + dy[i]), current);
        if(map.isObstacle(neighbor.position) || closed.contains(neighbor)) {
          continue;
        }
        int tentative_g_score = current.g + distance(current.position, neighbor.position);
        if(!open.contains(neighbor) || tentative_g_score < neighbor.g) {
          neighbor.g = tentative_g_score;
          neighbor.f = neighbor.g + distance(neighbor.position, targetNode.position);
          neighbor.parent = current;
          if(!open.contains(neighbor)) {
            open.add(neighbor);
          }
        }
      }
    }

    return null;
  }

  public List<Point> computeLongestPath() {
    Set<Node> open = new HashSet<>();
    Set<Node> closed = new HashSet<>();
    open.add(startNode);

    while (!open.isEmpty()) {
      Node current = findMax(open);
      open.remove(current);
      closed.add(current);

      if (current.position.equals(targetNode.position)) {
        return reconstructPath(current);
      }

      for (int i = 0; i < 5; i++) {
        Node neighbor = getNode(new Point(current.position.x + dx[i], current.position.y + dy[i]), current);
        if(map.isObstacle(neighbor.position) || closed.contains(neighbor)) {
          continue;
        }
        int tentative_g_score = current.g + distance(current.position, neighbor.position);
        if(!open.contains(neighbor) || tentative_g_score < neighbor.g) {
          neighbor.g = tentative_g_score;
          neighbor.f = neighbor.g + distance(neighbor.position, targetNode.position);
          neighbor.parent = current;
          if(!open.contains(neighbor)) {
            open.add(neighbor);
          }
        }
      }
    }

    return null;
  }

  private Node findMin(Set<Node> open) {
    Node min = null;
    for (Node node : open) {
      if (min == null) {
        min = node;
      } else if (min.compareTo(node) > 0) {
        min = node;
      }
    }
    return min;
  }

  private Node findMax(Set<Node> open) {
    Node max = null;
    for (Node node : open) {
      if (max == null) {
        max = node;
      } else if (max.compareTo(node) < 0) {
        max = node;
      }
    }
    return max;
  }

  private List<Point> reconstructPath(Node node) {
    LinkedList<Point> path = new LinkedList<>();
    while (node.parent != null) {
      path.addFirst(node.position);
      node = node.parent;
    }
    return path;
  }

  private int distance(Point n1, Point n2) {
    int dx = n1.x - n2.x;
    int dy = n1.y - n2.y;
    return (int) Math.round( 10 * Math.sqrt(dx * dx + dy * dy));
  }

  public class Node implements Comparable<Node> {
    Point position;
    int g;
    int h;
    int f;
    public Node parent;

    public Node(Point p, Node current) {
      position = p;
      g = distance(p, start);
      h =  distance(p, target);
      f = g + h;
    }

    @Override
    public int compareTo(Node that) {
      int df = this.f - that.f;
      if (df == 0) {
        return this.h - that.h;
      }
      return df;
    }

    @Override
    public boolean equals(Object obj) {
      Node that = (Node) (obj);
      return this.position.equals(that.position);
    }

    @Override
    public int hashCode() {
      return position.hashCode();
    }
  }

}
