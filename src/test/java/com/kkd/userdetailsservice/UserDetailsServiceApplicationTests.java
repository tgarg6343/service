package com.kkd.userdetailsservice;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.kkd.userdetailsservice.model.FarmerBean;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserDetailsServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserDetailsServiceApplicationTests {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	@Test
	public void retreiveFarmerByIdTest() throws JSONException {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/farmer/kkd123"), HttpMethod.GET,
				entity, String.class);
		String expected = "[{\"kkdFarmId\":\"FARM1000\",\"mobileNo\":\"1234567890\",\"password\":\"pass\","
				+ "\"alternateNo\":\"0987654321\",\"cities\":null,\"currentAddress\":null,\"status\":\"active\","
				+ "\"autoConfirm\":true,\"aadharData\":null}]";
		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}