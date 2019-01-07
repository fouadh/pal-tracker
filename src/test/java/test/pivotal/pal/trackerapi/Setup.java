package test.pivotal.pal.trackerapi;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

public class Setup {
  public static TestRestTemplate buildTestRestTemplate(final String port) {
    RestTemplateBuilder builder = new RestTemplateBuilder()
        .rootUri("http://localhost:" + port)
        .basicAuthorization("user", "password");
    return new TestRestTemplate(builder);
  }
}
