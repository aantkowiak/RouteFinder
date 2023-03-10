package com.aantkowiak.routefinder.service.graph;

import com.aantkowiak.routefinder.service.countries.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GraphServiceTest {

  GraphService graphService;

  @Mock CountryService countryService;

  @BeforeEach
  void init() {
    graphService = new GraphService(countryService);
  }

  @Test
  void should_returnOriginNode_when_destinationNodeIsEqualToOrigin() {
    // given
    Node origin = new Node("CZE");

    // when
    List<Node> path = graphService.findPath(origin, origin);
    List<Node> expected = Stream.of("CZE").map(Node::new).toList();

    // then
    assertEquals(expected, path);
  }

  @Test
  void should_returnEmptyList_when_noLandCrossingAvailable() {
    // given
    Node origin = new Node("CZE");
    Node destination = new Node("AUS");
    Set<Node> adjacentNodes =
        new HashSet<>(Stream.of("AUT", "DEU", "POL", "SVK").map(Node::new).toList());

    when(countryService.getAdjacentNodes(origin)).thenReturn(adjacentNodes);

    // when
    List<Node> path = graphService.findPath(origin, destination);

    // then
    assertTrue(path.isEmpty());
  }

  @Test
  void should_returnCorrectRoute_when_landCrossingIsAvailable() {
    // given
    Node origin = new Node("CZE");
    Node destination = new Node("ITA");

    Set<Node> czeAdjacentNodes =
        new HashSet<>(Stream.of("AUT", "DEU", "POL", "SVK").map(Node::new).toList());

    Set<Node> polAdjacentNodes =
        new HashSet<>(
            Stream.of("BLR", "CZE", "DEU", "LTU", "RUS", "SVK", "UKR").map(Node::new).toList());

    Set<Node> deuAdjacentNodes =
        new HashSet<>(
            Stream.of("AUT", "BEL", "CZE", "DNK", "FRA", "LUX", "NLD", "POL", "CHE")
                .map(Node::new)
                .toList());

    Set<Node> autAdjacentNodes =
        new HashSet<>(
            Stream.of("CZE", "DEU", "HUN", "ITA", "LIE", "SVK", "SVN", "CHE")
                .map(Node::new)
                .toList());

    when(countryService.getAdjacentNodes(any())).thenReturn(new HashSet<>());
    lenient().when(countryService.getAdjacentNodes(origin)).thenReturn(czeAdjacentNodes);
    lenient().when(countryService.getAdjacentNodes(new Node("POL"))).thenReturn(polAdjacentNodes);
    lenient().when(countryService.getAdjacentNodes(new Node("DEU"))).thenReturn(deuAdjacentNodes);
    lenient().when(countryService.getAdjacentNodes(new Node("AUT"))).thenReturn(autAdjacentNodes);

    // when
    List<Node> path = graphService.findPath(origin, destination);
    List<Node> expected = Stream.of("CZE", "AUT", "ITA").map(Node::new).toList();

    // then
    assertEquals(expected, path);
  }
}
