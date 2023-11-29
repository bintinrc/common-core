package co.nvqa.common.core.client;

import co.nvqa.common.client.SimpleApiClient;
import co.nvqa.common.constants.HttpConstants;
import co.nvqa.common.core.model.ResponseWrapper;
import co.nvqa.common.core.model.route.RouteTag;
import co.nvqa.common.core.model.route.TagResponse;
import co.nvqa.common.core.model.route.RouteTags;
import co.nvqa.common.utils.NvTestHttpException;
import co.nvqa.common.utils.StandardTestConstants;
import co.nvqa.commonauth.utils.TokenUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
@Singleton
public class TagClient extends SimpleApiClient {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(TagClient.class);

  public TagClient() {
    super(StandardTestConstants.API_BASE_URL, TokenUtils.getOperatorAuthToken(),
        DEFAULT_CAMEL_CASE_MAPPER);
  }

  public TagResponse create(RouteTag tag) {
    String apiMethod = "route/1.0/tags";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .body(toJson(List.of(tag)));

    Response r = doPost("Tag Client - Create Tag", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), TagResponse.class);
  }

  public TagResponse update(RouteTag tag) {
    String apiMethod = "route/1.0/tags/{id}";

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .pathParam("id", tag.getId())
        .body(toJson(tag));

    Response r = doPut("Tag Client - Update tag details", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), TagResponse.class);
  }

  public TagResponse getAllTag() {
    String apiMethod = "route/1.0/tags";

    RequestSpecification requestSpecification = createAuthenticatedRequest();

    Response r = doGet("Tag Client - Get tag", requestSpecification, apiMethod);
    if (r.statusCode() != HttpConstants.RESPONSE_200_SUCCESS) {
      throw new NvTestHttpException("unexpected http status: " + r.statusCode());
    }
    r.then().contentType(ContentType.JSON);
    return fromJson(r.body().asString(), TagResponse.class);
  }

  public void deleteTag(long tagId) {
    String apiMethod = "route/1.0/tags/{tagId}";

    LOGGER.info("Deleting tag with ID = {}", tagId);

    RequestSpecification requestSpecification = createAuthenticatedRequest()
        .contentType(ContentType.JSON)
        .pathParam("tagId", tagId);

    Response response = doDelete("Route - Delete Tag", requestSpecification, apiMethod);
    response.then().assertThat().statusCode(HttpConstants.RESPONSE_200_SUCCESS);
    LOGGER.info("Deleting tag with ID = {} is done", tagId);
  }

  public void deleteTag(String tagName) {
    RouteTag tag = findTagByName(tagName);
    if (tag == null) {
      throw new IllegalArgumentException("Tag " + tagName + " was not found");
    }
    deleteTag(tag.getId());
  }

  public RouteTag findTagByName(String name) {
    RouteTag result = null;

    String apiMethod = "route/1.0/tags";

    RequestSpecification requestSpecification = createAuthenticatedRequest();

    Response response = doGet("Route - Find Tag By Name", requestSpecification,
        apiMethod);
    response.then().assertThat().contentType(ContentType.JSON);
    response.then().assertThat().statusCode(HttpConstants.RESPONSE_200_SUCCESS);

    ResponseWrapper<RouteTags> responseWrapper = fromJsonCamelCase(
        response.then().extract().body().asString(), new TypeReference<ResponseWrapper<RouteTags>>() {
        });
    RouteTags tags = responseWrapper.getData();
    LOGGER.info(f("Number of Tags: %s", tags.getTags().size()));

    for (RouteTag tag : tags.getTags()) {
      if (name.equals(tag.getName())) {
        result = tag;
        break;
      }
    }

    return result;
  }
}
