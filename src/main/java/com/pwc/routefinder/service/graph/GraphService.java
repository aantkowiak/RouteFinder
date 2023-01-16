package com.pwc.routefinder.service.graph;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.Collections.emptyList;
import static java.util.function.Predicate.not;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class GraphService {

  @NonNull private final AdjacentNodeProvider adjacentNodeProvider;

  public List<Node> findPath(Node origin, Node destination) {

    Set<Node> settledNodes = new HashSet<>();
    Set<Node> nodesToVerifyNext = new HashSet<>();
    nodesToVerifyNext.add(origin);

    List<Node> equality = handleOriginEqualToDestination(origin, destination);
    if (equality != null) return equality;

    while (isNotEmpty(nodesToVerifyNext)) {
      Node currentNode = nodesToVerifyNext.iterator().next();

      List<Node> path =
          handleCurrentNode(currentNode, destination, settledNodes, nodesToVerifyNext);

      if (isNotEmpty(path)) return path;

      settledNodes.add(currentNode);
    }
    return emptyList();
  }

  private List<Node> handleOriginEqualToDestination(Node origin, Node destination) {
    return origin.getName().equals(destination.getName()) ? List.of(origin) : null;
  }

  private List<Node> handleCurrentNode(
      Node currentNode, Node destination, Set<Node> settledNodes, Set<Node> nodesToVerifyNext) {
    nodesToVerifyNext.remove(currentNode);

    Predicate<Node> isNodeSettled = settledNodes::contains;
    Predicate<Node> isDestinationNode = node -> node.equals(destination);

    Optional<Node> destinationNode =
        checkAdjacentContainDestination(currentNode, isNodeSettled, isDestinationNode);

    if (destinationNode.isPresent()) {

      destinationNode.get().setPreviousNode(currentNode);

      return mapToPathOfNodes(new LinkedList<>(), destinationNode.get());

    } else {
      adjacentNodeProvider.getAdjacentNodes(currentNode).stream()
          .filter(not(isNodeSettled))
          .forEach(
              node -> {
                node.setPreviousNode(currentNode);
                nodesToVerifyNext.add(node);
              });
    }
    return emptyList();
  }

  private Optional<Node> checkAdjacentContainDestination(
      Node currentNode, Predicate<Node> isNodeSettled, Predicate<Node> isDestinationNode) {
    return adjacentNodeProvider.getAdjacentNodes(currentNode).stream()
        .filter(not(isNodeSettled))
        .filter(isDestinationNode)
        .findFirst();
  }

  private List<Node> mapToPathOfNodes(LinkedList<Node> route, Node node) {
    route.addFirst(node);
    if (node.getPreviousNode() == null) {
      return route;
    }
    return mapToPathOfNodes(route, node.getPreviousNode());
  }
}
