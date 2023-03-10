package com.aantkowiak.routefinder.controller;

import com.aantkowiak.routefinder.RouteFinderApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = {RouteFinderApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RouteFinderControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void should_returnProperRoute_whenLandCrossingIsAvailable() throws Exception {
    this.mockMvc
        .perform(get("/api/v1/routing/CZE/ITA"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.route").value(is(List.of("CZE", "AUT", "ITA"))));
  }
}
