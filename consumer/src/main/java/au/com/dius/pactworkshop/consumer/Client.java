package au.com.dius.pactworkshop.consumer;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.time.ZoneId;

public class Client {
  private JsonNode loadProviderJson() throws UnirestException {
    return Unirest.get("http://localhost:8080/provider.json")
      .queryString("validDate", LocalDateTime.now().toString())
      .asJson().getBody();
  }

  public List<Object> fetchAndProcessData() throws UnirestException{
    //该data的数据为response body的数据
    JsonNode data = loadProviderJson();
    System.out.println("data="+data);

    JSONObject jsonObject = data.getObject();
    //value存的是count对应的值
    int value = 100/jsonObject.getInt("count");
    //此处的parse方法需要增加时区
    DateTimeFormatter time_zone = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withZone(ZoneId.of("Europe/Paris"));
    System.out.println(jsonObject.getString("validDate"));
    ZonedDateTime date = ZonedDateTime.parse(jsonObject.getString("validDate"),time_zone);
    // ZonedDateTime date = ZonedDateTime.parse("2007-12-03T10:15:30+01:00[Europe/Paris]");
    System.out.println("value="+value);
    System.out.println("date="+date);
    return Arrays.asList(value,date);
  }
}