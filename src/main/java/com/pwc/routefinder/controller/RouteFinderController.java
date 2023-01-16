package com.pwc.routefinder.controller;

import com.pwc.routefinder.service.route.RouteDto;
import com.pwc.routefinder.service.route.RouteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class RouteFinderController {

  private final RouteService routeService;

  @GetMapping(value = "/routing/{origin}/{destination}")
  public ResponseEntity<Object> fetchRoute(
      @PathVariable String origin, @PathVariable String destination) {
    try {
      RouteDto route = routeService.findRoute(origin, destination);

      return ResponseEntity.status(HttpStatus.OK).body(route);

    } catch (ResponseStatusException e) {
      return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }
  }
}
