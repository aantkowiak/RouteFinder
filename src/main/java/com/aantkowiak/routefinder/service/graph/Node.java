package com.aantkowiak.routefinder.service.graph;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Node {
  private String name;
  @EqualsAndHashCode.Exclude private Node previousNode;

  public Node(String name) {
    this.name = name;
  }
}
