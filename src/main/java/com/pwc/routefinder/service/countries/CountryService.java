package com.pwc.routefinder.service.countries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pwc.routefinder.config.ApplicationProperties;
import com.pwc.routefinder.service.graph.AdjacentNodeProvider;
import com.pwc.routefinder.service.graph.Node;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;

@Service
@AllArgsConstructor
@Slf4j
public class CountryService implements AdjacentNodeProvider {

  private ApplicationProperties applicationProperties;

  private HashMap<String, Set<String>> countriesData;

  private Map<String, Set<String>> getCountriesData() {
    if (isNotEmpty(countriesData)) {
      return countriesData;
    }

    HashMap<String, Set<String>> data = new HashMap<>();

    fetchCountriesDataFromExternalSource()
        .forEach(dto -> data.put(dto.getCode(), dto.getBorders()));

    countriesData = data;
    return countriesData;
  }

  private Set<CountryDto> fetchCountriesDataFromExternalSource() {
    try {
      HttpRequest request =
          HttpRequest.newBuilder()
              .uri(new URI(applicationProperties.getCountriesDataUrl()))
              .timeout(Duration.of(30, SECONDS))
              .GET()
              .build();

      log.info("Countries data - fetching data from external source");

      HttpResponse<String> response =
          HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
      log.info("Countries data - fetching data from external source - success");

      CountryDto[] countriesDataDto =
          (new ObjectMapper()).readValue(response.body(), CountryDto[].class);
      log.info("Countries data - mapping from json to objects - success");

      return Arrays.stream(countriesDataDto).collect(toSet());

    } catch (URISyntaxException e) {
      log.error("URI syntax exception occurred");
      throw new FetchCountryDataException();
    } catch (InterruptedException e) {
      log.error("HttpClient exception occurred");
      Thread.currentThread().interrupt();
      throw new FetchCountryDataException();
    } catch (IOException e) {
      log.error("Json parsing exception occurred");
      throw new FetchCountryDataException();
    }
  }

  public Set<Node> getAdjacentNodes(Node currentNode) {
    return ofNullable(getCountriesData().get(currentNode.getName()))
        .map(
            countryCodes ->
                countryCodes.stream().filter(Objects::nonNull).map(Node::new).collect(toSet()))
        .orElse(emptySet());
  }
}
