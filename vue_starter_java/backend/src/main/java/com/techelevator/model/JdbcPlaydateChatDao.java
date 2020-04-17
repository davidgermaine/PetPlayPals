package com.techelevator.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcPlaydateChatDao implements PlaydateChatDao {
	
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public JdbcPlaydateChatDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	@Override
	public void addNewMessage(PlaydateChat playdateChat) {
		playdateChat.setPlaydateChatId(getNextAvailableId());
		String sql = "INSERT INTO playdatechat(playdatechatid, playdateid, messagedate, username, message)"
				+ " VALUES(?,?,?,?,?)";
		jdbcTemplate.update(sql, playdateChat.getPlaydateChatId(), playdateChat.getPlaydateId(),
				LocalDate.parse(playdateChat.getDate()), playdateChat.getUsername(), playdateChat.getMessage());
	}
	
	@Override
	public void deleteMessages(int playdateId) {
		String sql = "DELETE FROM playdatechat WHERE playdateid = ?";
		jdbcTemplate.update(sql, playdateId);
	}

	@Override
	public List <PlaydateChat> getAllMessagesByPlaydateId(int playdateId) {
		List <PlaydateChat> messages = new ArrayList<>();
		String sql = "SELECT * FROM playdatechat WHERE playdateid = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, playdateId);
		while(results.next()) {
			PlaydateChat message = new PlaydateChat();
			message.setPlaydateChatId(results.getInt("playdatechatid"));
			message.setPlaydateId(results.getInt("playdateid"));
			message.setDate(results.getString("messagedate"));
			message.setUsername(results.getString("username"));
			message.setMessage(results.getString("message"));
			messages.add(message);
		}
		return messages;
	}
	
	private int getNextAvailableId() {
		int id = 0;
		String sql1 = "SELECT COUNT(*) FROM playdatechat";
		SqlRowSet countResult = jdbcTemplate.queryForRowSet(sql1);
		while (countResult.next()) {
			int count = countResult.getInt("count");
			if (count == 0) {
				id = 1;
			} else {
				String sql2 = "SELECT playdatechatid FROM playdatechat ORDER BY playdatechatid DESC LIMIT 1";
				SqlRowSet results = jdbcTemplate.queryForRowSet(sql2);
				while (results.next()) {
					id = results.getInt("playdatechatid") + 1;
				}
			}
		}
		return id;
	}

}
