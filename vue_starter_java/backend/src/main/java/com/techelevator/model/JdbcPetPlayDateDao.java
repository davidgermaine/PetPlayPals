package com.techelevator.model;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


@Component
public class JdbcPetPlayDateDao implements PetPlayDateDao {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JdbcPetPlayDateDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void createPetPlayDate(PetPlayDate petPlayDate) {
		String sql = "INSERT INTO pet_playdate (petplaydateid, playdateid, petid) "
				+ "VALUES (?,?,?)";
		petPlayDate.setPetPlayDateId(getNextAvailableId());
		jdbcTemplate.update(sql, petPlayDate.getPetPlayDateId(), petPlayDate.getPlaydateId(),
				petPlayDate.getPetId());
	}
	
	@Override
	public void deletePetPlayDateByPet(int petId) {
		String sql = "DELETE FROM pet_playdate WHERE petid = ?";
		jdbcTemplate.update(sql, petId);
	}

	@Override
	public void deletePetPlayDate(int playdateId) {
		String sql = "DELETE FROM pet_playdate WHERE playdateid = ?";
		jdbcTemplate.update(sql, playdateId);
	}
	
	private int getNextAvailableId() {
		int id = 0;
		String sql1 = "SELECT COUNT(*) FROM pet_playdate";
		SqlRowSet countResult = jdbcTemplate.queryForRowSet(sql1);
		while (countResult.next()) {
			int count = countResult.getInt("count");
			if (count == 0) {
				id = 1;
			} else {
				String sql2 = "SELECT petplaydateid FROM pet_playdate ORDER BY petplaydateid DESC LIMIT 1";
				SqlRowSet results = jdbcTemplate.queryForRowSet(sql2);
				while (results.next()) {
					id = results.getInt("petplaydateid") + 1;
				}
			}
		}
		return id;
	}

}
