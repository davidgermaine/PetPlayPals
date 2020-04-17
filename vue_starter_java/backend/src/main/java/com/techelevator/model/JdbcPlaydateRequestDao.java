package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcPlaydateRequestDao implements PlaydateRequestDao {
	
	 private JdbcTemplate jdbcTemplate;
	    
    @Autowired
    public JdbcPlaydateRequestDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public List<PlaydateRequest> getPlaydateRequestsByUsername(String username) {
		List<PlaydateRequest> requests = new ArrayList<PlaydateRequest>();
		String sql = "SELECT requestid, pdr.username, pdr.playdateid, pdr.petid FROM playdaterequest pdr" + 
				" JOIN playdate pd ON pd.playdateid = pdr.playdateid" + 
				" WHERE pd.username = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
		while(results.next()) {
			PlaydateRequest request = new PlaydateRequest();
			request.setRequestId(results.getInt("requestid"));
			request.setUsername(results.getString("username"));
			request.setPlaydateId(results.getInt("playdateid"));
			request.setPetId(results.getInt("petid"));
			requests.add(request);
		}
		return requests;
	}

	@Override
	public void deletePlayDate(int requestId) {
		String sql = "DELETE FROM playdaterequest WHERE requestid = ?";
		jdbcTemplate.update(sql, requestId);
	}

	@Override
	public void addPlaydateRequest(PlaydateRequest request) {
		String sql = "INSERT INTO playdaterequest(requestId, username, playdateId, petId) "
				+ " VALUES(?,?,?,?)";
		request.setRequestId(getNextAvailableId());
		jdbcTemplate.update(sql, request.getRequestId(), request.getUsername(), request.getPlaydateId(), request.getPetId());
		
	}
	
	private int getNextAvailableId() {
		int id = 0;
		String sql1 = "SELECT COUNT(*) FROM playdaterequest";
		SqlRowSet countResult = jdbcTemplate.queryForRowSet(sql1);
		while (countResult.next()) {
			int count = countResult.getInt("count");
			if (count == 0) {
				id = 1;
			} else {
				String sql2 = "SELECT requestid FROM playdaterequest ORDER BY requestid DESC LIMIT 1";
				SqlRowSet results = jdbcTemplate.queryForRowSet(sql2);
				while (results.next()) {
					id = results.getInt("requestid") + 1;
				}
			}
		}
		return id;
	}

	@Override
	public List <PlaydateRequest> getPlaydateRequestById(int id) {
		List <PlaydateRequest> requests = new  ArrayList<>();
		String sql = "Select * FROM playdaterequest where playdateid = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
		while(results.next()) {
			PlaydateRequest request = new PlaydateRequest();
			request.setRequestId(results.getInt("requestid"));
			request.setUsername(results.getString("username"));
			request.setPlaydateId(results.getInt("playdateid"));
			request.setPetId(results.getInt("petid"));
			requests.add(request);
		}
		return requests;
	}

}
