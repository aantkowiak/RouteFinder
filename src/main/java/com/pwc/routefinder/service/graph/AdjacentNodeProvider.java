package com.pwc.routefinder.service.graph;

import java.util.Set;

public interface AdjacentNodeProvider {
  Set<Node> getAdjacentNodes(Node node);
}
