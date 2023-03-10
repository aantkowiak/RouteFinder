package com.aantkowiak.routefinder.service.route;

import com.aantkowiak.routefinder.service.graph.GraphService;
import com.aantkowiak.routefinder.service.graph.Node;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
final class RouteServiceTest {

  @Mock
  GraphService graphService;

  RouteService routeService;

  @BeforeEach
  void init() {
    graphService = mock(GraphService.class);
    routeService = new RouteService(graphService);
  }

  @Test
  void should_returnProperRoute_when_inputParametersAreCorrect() {

    when(graphService.findPath(any(), any()))
        .thenReturn(Stream.of("CZE", "AUT", "ITA").map(Node::new).toList());

    RouteDto route = routeService.findRoute("CZE", "ITA");

    assertEquals(new RouteDto(of("CZE", "AUT", "ITA")), route);
  }

  @Test
  void should_throwException_when_inputNoLandCrossing() {

    when(graphService.findPath(any(), any())).thenReturn(emptyList());

    NoLandCrossingException thrown =
        Assertions.assertThrows(
            NoLandCrossingException.class, () -> routeService.findRoute("CZE", "ITA"));

    assertEquals("No available land crossing", thrown.getReason());
  }
}
