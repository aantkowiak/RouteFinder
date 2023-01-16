package com.pwc.routefinder.service.route;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NoLandCrossingException extends ResponseStatusException {
  public NoLandCrossingException() {
    super(HttpStatus.BAD_REQUEST, "No available land crossing");
  }
}
