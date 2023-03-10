package com.aantkowiak.routefinder.service.route;

import com.aantkowiak.routefinder.service.graph.GraphService;
import com.aantkowiak.routefinder.service.graph.Node;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RouteService {

  private GraphService graphService;

  public RouteDto findRoute(String originCountryCode, String destinationCountryCode) {

    List<String> route =
        graphService
            .findPath(new Node(originCountryCode), new Node(destinationCountryCode))
            .stream()
            .map(Node::getName)
            .collect(Collectors.toList());

    if (route.isEmpty()) {
      throw new NoLandCrossingException();
    }

    return new RouteDto(route);
  }
}
