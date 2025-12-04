package de.local.energycharts.infrastructure.opendatasoft.adapter;

import com.github.tomakehurst.wiremock.WireMockServer;
import de.local.energycharts.core.solarcity.ports.out.OpendatasoftGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

class OpendatasoftRestApiGatewayTest {

  private WireMockServer wireMockServer;
  private OpendatasoftGateway opendatasoftGateway;

  @BeforeEach
  void setUp() {
    wireMockServer = new WireMockServer();
    wireMockServer.start();
    opendatasoftGateway = new OpendatasoftRestApiGateway(
        WebClient.create(wireMockServer.baseUrl())
    );

    // first page
    stubFor(get(urlPathMatching(
        "/api/records/1.0/search/"
    ))
        .withQueryParam("dataset", equalTo("georef-germany-postleitzahl"))
        .withQueryParam("refine.plz_name", equalTo("Frankfurt"))
        .withQueryParam("rows", equalTo("100"))
        .withQueryParam("start", equalTo("0"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody("""
                {
                  "records": [
                    {
                      "fields": {
                        "plz_code": "60529"
                      }
                    },
                    {
                      "fields": {
                        "plz_code": "60599"
                      }
                    }
                  ]
                }
                """)
        ));

    // next page
    stubFor(get(urlPathMatching(
        "/api/records/1.0/search/"
    ))
        .withQueryParam("dataset", equalTo("georef-germany-postleitzahl"))
        .withQueryParam("refine.plz_name", equalTo("Frankfurt"))
        .withQueryParam("rows", equalTo("100"))
        .withQueryParam("start", equalTo("100"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody("""
                {
                  "records": [{
                    "fields": {
                      "plz_code": "60314"
                    }
                 }]
                }
                """)
        ));

    // page with empty records, to mark the stop
    stubFor(get(urlPathMatching(
        "/api/records/1.0/search/"
    ))
        .withQueryParam("dataset", equalTo("georef-germany-postleitzahl"))
        .withQueryParam("refine.plz_name", equalTo("Frankfurt"))
        .withQueryParam("rows", equalTo("100"))
        .withQueryParam("start", equalTo("200"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBody("""
                {
                  "records": []
                }
                """)
        ));
  }

  @AfterEach
  void tearDown() {
    wireMockServer.stop();
  }

  @Test
  void test_pagination() {
    List<Integer> postcodes = opendatasoftGateway.getPostcodes("Frankfurt").collectList().block();

    assertThat(postcodes).containsExactly(60314, 60529, 60599);
  }
}