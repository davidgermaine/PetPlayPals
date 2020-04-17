package com.techelevator.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JdbcPlaydateDao implements PlaydateDao{
	
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public JdbcPlaydateDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


	@Override
	public List<Playdate> getPlaydatesByUsername(String username, String now) {
		List<Playdate> playdateList = new ArrayList<>();
		String sql = "SELECT * FROM playdate WHERE username = ? AND playdatedate > ? ORDER BY playdatedate ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, LocalDate.parse(now));
		
		while(results.next()) {
			Playdate playdate = mapRowToPlaydate(results);
			playdateList.add(playdate);
		}
		return playdateList;
	}

	@Override
	public List<Playdate> getPlaydatesInFuture(String date) {
		List<Playdate> playdateList = new ArrayList<>();
		String sql = "SELECT * FROM playdate WHERE playdatedate > ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, LocalDate.parse(date));
		
		while(results.next()) {
			Playdate playdate = mapRowToPlaydate(results);
			playdateList.add(playdate);
		}
		return playdateList;
	}

	@Override
	public void createNewPlaydate(Playdate playdate) {
		String latitude = "";
		String longitude = "";
		HttpEntity<String> httpEntity = new HttpEntity<>("");
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper objectMapper = new ObjectMapper();
		String textAddress = playdate.getAddress();
		String key = "&key=AIzaSyBmXt7ruin_vtd_9WJOm_zxpkg1ofGnCVk";
		String googleGeoLocationUrl = "https://maps.googleapis.com/maps/api/geocode/json?" + 
		"address=" + textAddress + key;
		ResponseEntity<String> response = restTemplate.exchange(googleGeoLocationUrl,
				HttpMethod.GET, httpEntity, String.class);// 4. Read the response into a format I can understand. 		JsonNode jsonNode = objectMapper.readTree(response.getBody());
		try {
			JsonNode jsonNode = objectMapper.readTree(response.getBody());
			latitude = jsonNode.path("results").path(0).path("geometry").path("location").path("lat").asText();
			longitude = jsonNode.path("results").path(0).path("geometry").path("location").path("lng").asText();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (latitude.length() > 9) {
			latitude = latitude.substring(0, 9);
		}
		if (longitude.length() > 9) {
			longitude = longitude.substring(0, 9);
		}	
		
		System.out.println(response.getStatusCode());
		String sql = "INSERT INTO playdate(playdateid, username, playdatedate, address,"
				+ " city, region, zipcode, "
				+ "latitude, longitude, "
				+ "playdatetime) VALUES(?,?,?,?,?,?,?,?,?,?)";
		playdate.setPlaydateId(getNextAvailableId());
		
		//puts time into HH:MM AM/PM format
		int hours = Integer.parseInt(playdate.getPlaydateTime().substring(0,2));
		String minutes = playdate.getPlaydateTime().substring(2);
		if(hours > 12) {
			hours = hours - 12;
			minutes = minutes + " PM";
		} else {
			minutes = minutes + " AM";
		}
		playdate.setPlaydateTime(hours + minutes);
		jdbcTemplate.update(sql, playdate.getPlaydateId(), playdate.getUsername(),
				LocalDate.parse(playdate.getPlaydateDate()), playdate.getAddress(), playdate.getCity(),
				playdate.getRegion(), 
				playdate.getZipcode(), 
				latitude, longitude, 
				playdate.getPlaydateTime());
	}

	@Override
	public void updatePlaydate(Playdate playdate) {
		String sql = "UPDATE playdate SET playdatedate = ?, address = ?, city = ?, region = ?,"
				+ "zipcode = ?, playdatetime = ?  WHERE playdateid = ?";
		jdbcTemplate.update(sql, LocalDate.parse(playdate.getPlaydateDate()),
				playdate.getAddress(), playdate.getCity(), playdate.getRegion(), playdate.getZipcode(),
				playdate.getPlaydateTime(), playdate.getPlaydateId());
	}

	@Override
	public void deletePlaydate(int playdateId) {
		String sql = "DELETE FROM playdate WHERE playdateid = ?";
		jdbcTemplate.update(sql, playdateId);
	}
	
	public Playdate mapRowToPlaydate(SqlRowSet results) {
		Playdate playdate = new Playdate();
		playdate.setPlaydateId(results.getInt("playdateid"));
		playdate.setUsername(results.getString("username"));
		playdate.setPlaydateDate(results.getString("playdatedate"));
		playdate.setAddress(results.getString("address"));
		playdate.setCity(results.getString("city"));
		playdate.setRegion(results.getString("region"));
		playdate.setZipcode(results.getString("zipcode"));
		playdate.setLatitude(results.getString("latitude"));
		playdate.setLongitude(results.getString("longitude"));
		playdate.setPlaydateTime(results.getString("playdatetime"));
		
		return playdate;
	}
	
	private int getNextAvailableId() {
		int id = 0;
		String sql1 = "SELECT COUNT(*) FROM playdate";
		SqlRowSet countResult = jdbcTemplate.queryForRowSet(sql1);
		while (countResult.next()) {
			int count = countResult.getInt("count");
			if (count == 0) {
				id = 1;
			} else {
				String sql2 = "SELECT playdateid FROM playdate ORDER BY playdateid DESC LIMIT 1";
				SqlRowSet results = jdbcTemplate.queryForRowSet(sql2);
				while (results.next()) {
					id = results.getInt("playdateid") + 1;
				}
			}
		}
		return id;
	}

	@Override
	public Playdate getPlaydateById(int playdateId) {
		String sql = "SELECT * FROM playdate WHERE playdateId = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, playdateId);
		Playdate playdate = new Playdate();
		while(results.next()) {
		 playdate = mapRowToPlaydate(results);
		}
		return playdate;
	}

	@Override
	public List<Playdate> getScheduledPlaydatesFromNonPoster(String username, String date) {
		List<Playdate> playdateList = new ArrayList<>();
		String sql = "SELECT COUNT(ppd.playdateid), pd.playdateid, pd.username, pd.address, pd.city, pd.region, pd.zipcode" + 
				", pd.playdatedate, pd.playdatetime, pd.latitude, pd.longitude FROM playdate pd" + 
				" JOIN pet_playdate ppd ON ppd.playdateid = pd.playdateid" + 
				" JOIN pet p on p.petid = ppd.petid" + 
				" JOIN users u on u.username = p.username" + 
				" WHERE u.username = ? AND pd.username != ? AND  pd.playdatedate > ?"
				+ " GROUP BY pd.playdateid, pd.username, pd.address, pd.city, pd.region, pd.zipcode" + 
				" , pd.playdatedate, pd.playdatetime, pd.latitude, pd.longitude";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username, username, LocalDate.parse(date));
		
		while(results.next()) {
			Playdate playdate = mapRowToPlaydate(results);
			playdateList.add(playdate);
		}
		return playdateList;
	}

}

