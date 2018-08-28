package au.com.dius.pactworkshop.consumer;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Rule;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ClientTest {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(8080);

  @Test
  public void canProcessTheJsonPayloadFromTheProvider() throws UnirestException {

  String date = "2018-08-28T14:55:22.000+00:00";
  stubFor(get(urlPathEqualTo("/provider.json"))
  .withQueryParam("validDate", matching(".+"))
  .willReturn(aResponse()
    .withStatus(200)
    .withHeader("Content-Type", "application/json")
    .withBody("{\"test\": \"NO\", \"date\": \"" + date + "\", \"count\": 100}")));

    List<Object> data = new Client().fetchAndProcessData();
  
  assertThat(data, hasSize(2));
  assertThat(data.get(0), is(1));
  assertThat(data.get(1), is(ZonedDateTime.parse(date)));
  }
}
