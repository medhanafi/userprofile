package com.kmsoft.userprofile.service;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmsoft.userprofile.model.Location;
import com.kmsoft.userprofile.model.Login;
import com.kmsoft.userprofile.model.Picture;
import com.kmsoft.userprofile.model.User;
import com.kmsoft.userprofile.utils.DateParser;
import com.kmsoft.userprofile.utils.JDException;

@Service
public final class UserProfileParser {

	@Autowired
	private UserProfileConnect connect;

	@Autowired
	private UserProfileParser parse;

	
	private DateParser dateParser;

	public UserProfileParser() throws JDException {
		super();
		this.dateParser=new DateParser();
	}

	public static JsonNode getJsonNodeFromString(final String jsonString) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(jsonString);
	}

	public User jsonParser(User user, String url) throws IOException, JDException {
		JsonNode jsonNodeApp = connect.getDataFromUrl(url);
		
		if (jsonNodeApp != null) {
			user.setGender(this.getValueAsText(jsonNodeApp, "gender"));
			user.setTitle(this.getValueAsText(jsonNodeApp.get("name"), "title"));
			user.setName(this.getValueAsText(jsonNodeApp.get("name"), "last"));
			user.setFirstname(this.getValueAsText(jsonNodeApp.get("name"), "first"));
			user.setEmail(this.getValueAsText(jsonNodeApp, "email"));
			user.setBirth(this.getValueAsDate(jsonNodeApp.get("dob"), "date"));
			user.setRegister(this.getValueAsDate(jsonNodeApp.get("registered"), "date"));
			user.setPhone(this.getValueAsText(jsonNodeApp, "phone"));
			user.setCell(this.getValueAsText(jsonNodeApp, "cell"));
			user.setNat(this.getValueAsText(jsonNodeApp, "nat"));

			Location location=new Location();
			location.setStreet(this.getValueAsText(jsonNodeApp.get("location"), "street"));
			location.setCity(this.getValueAsText(jsonNodeApp.get("location"), "city"));
			location.setState(this.getValueAsText(jsonNodeApp.get("location"), "state"));
			location.setPostcode(this.getValueAsText(jsonNodeApp.get("location"), "postcode"));
			location.setCoLatitude(this.getValueAsFloat(jsonNodeApp.get("location").get("coordinates"), "latitude"));
			location.setCoLongitude(this.getValueAsFloat(jsonNodeApp.get("location").get("coordinates"), "longitude"));
			location.setTzOffset(this.getValueAsText(jsonNodeApp.get("location").get("timezone"), "offset"));
			location.setTz_description(this.getValueAsText(jsonNodeApp.get("location").get("timezone"), "description"));
			
			Login login=new Login();
			login.setUuid(this.getValueAsText(jsonNodeApp.get("login"), "uuid"));
			login.setUsername(this.getValueAsText(jsonNodeApp.get("login"), "username"));
			login.setPassword(this.getValueAsText(jsonNodeApp.get("login"), "password"));
			login.setSalt(this.getValueAsText(jsonNodeApp.get("login"), "salt"));
			login.setMd5(this.getValueAsText(jsonNodeApp.get("login"), "md5"));
			login.setSha1(this.getValueAsText(jsonNodeApp.get("login"), "sha1"));
			login.setSha256(this.getValueAsText(jsonNodeApp.get("login"), "sha256"));
			
			Picture picture=new Picture();
			picture.setLarge(this.getValueAsText(jsonNodeApp.get("picture"), "large"));
			picture.setMedium(this.getValueAsText(jsonNodeApp.get("picture"), "medium"));
			picture.setThumbnail(this.getValueAsText(jsonNodeApp.get("picture"), "thumbnail"));
			
			
			user.setLocation(location);
			user.setLogin(login);
			user.setPicture(picture);
			
			
			System.out.println(jsonNodeApp);
		}
		return user;
	}

	private Date getValueAsDate(JsonNode jsonNode, String fieldName) {
		return dateParser.parse(this.getValueAsText(jsonNode, fieldName));
	}

	private String getValueAsText(final JsonNode jsonNode, final String fieldName) {
		if (jsonNode.findValue(fieldName) != null) {
			final JsonNode jsonNodeField = jsonNode.get(fieldName);
			if (jsonNodeField != null) {
				return jsonNodeField.asText();
			}
		}
		return null;
	}

	private Float getValueAsFloat(final JsonNode jsonNode, final String fieldName) {
		final String string = this.getValueAsText(jsonNode, fieldName);
		if (string != null) {
			return Float.parseFloat(string);
		}
		return null;
	}

	private Long getValueAsLong(final JsonNode jsonNode, final String fieldName) {
		final String string = this.getValueAsText(jsonNode, fieldName);
		if (string != null) {
			return Long.parseLong(string);
		}
		return null;
	}

	private Integer getValueAsInt(final JsonNode jsonNode, final String fieldName) {
		final String string = this.getValueAsText(jsonNode, fieldName);
		if (string != null) {
			return Integer.parseInt(string);
		}
		return null;
	}
}
