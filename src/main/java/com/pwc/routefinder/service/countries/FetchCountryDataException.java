package com.pwc.routefinder.service.countries;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FetchCountryDataException extends ResponseStatusException {
  public FetchCountryDataException() {
    super(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "An exception was thrown while trying to fetch countries data");
  }
}
