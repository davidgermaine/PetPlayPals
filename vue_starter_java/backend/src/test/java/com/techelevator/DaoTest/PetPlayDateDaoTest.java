package com.techelevator.DaoTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.JdbcPetDao;
import com.techelevator.model.JdbcPetPlayDateDao;
import com.techelevator.model.JdbcPlaydateDao;
import com.techelevator.model.Pet;
import com.techelevator.model.PetPlayDate;
import com.techelevator.model.Playdate;

public class PetPlayDateDaoTest {
	
	private static SingleConnectionDataSource dataSource;
	private JdbcPetPlayDateDao petPlayDateDAO;
	private JdbcPetDao petDAO;
	private JdbcPlaydateDao playdateDAO;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/userdb");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		petPlayDateDAO = new JdbcPetPlayDateDao(dataSource);
		petDAO = new JdbcPetDao(dataSource);
		playdateDAO = new JdbcPlaydateDao(dataSource);

		String testSql = "INSERT INTO users (id, username, password, salt) VALUES (100, 'testAccount', 'unhashedPassword', 'unsalted')";
		jdbcTemplate.update(testSql);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void createPetPlayDate_creates_new_pet_playdate() {
		Playdate playdate = createTempPlaydate("testAccount", "2120-01-01");
		Pet pet = createTempPet();
		
		petDAO.createPet(pet);
		playdateDAO.createNewPlaydate(playdate);

		PetPlayDate petPlayDate = new PetPlayDate();
		petPlayDate.setPlaydateId(playdate.getPlaydateId());
		petPlayDate.setPetId(pet.getPetId());
		assertNotNull(petPlayDate);
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int initialCount = 0;
		String sql1 = "SELECT COUNT(*) FROM pet_playdate";
    	SqlRowSet initialCountResult = jdbcTemplate.queryForRowSet(sql1);
    	while (initialCountResult.next()) {
    		initialCount = initialCountResult.getInt("count");
    	}
    	
		petPlayDateDAO.createPetPlayDate(petPlayDate);
		
		int postCount = 0;
    	SqlRowSet postCountResult = jdbcTemplate.queryForRowSet(sql1);
    	while (postCountResult.next()) {
    		postCount = postCountResult.getInt("count");
    	}
		
    	assertEquals(initialCount + 1, postCount);
	}
	
	@Test
	public void deletePetPlayDate_removes_pet_playdate_from_database() {
		Playdate playdate = createTempPlaydate("testAccount", "2120-01-01");
		Pet pet = createTempPet();
		
		petDAO.createPet(pet);
		playdateDAO.createNewPlaydate(playdate);

		PetPlayDate petPlayDate = new PetPlayDate();
		petPlayDate.setPlaydateId(playdate.getPlaydateId());
		petPlayDate.setPetId(pet.getPetId());
		assertNotNull(petPlayDate);
		
		petPlayDateDAO.createPetPlayDate(petPlayDate);
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int initialCount = 0;
		String sql1 = "SELECT COUNT(*) FROM pet_playdate";
    	SqlRowSet initialCountResult = jdbcTemplate.queryForRowSet(sql1);
    	while (initialCountResult.next()) {
    		initialCount = initialCountResult.getInt("count");
    	}
    	
		petPlayDateDAO.deletePetPlayDate(petPlayDate.getPetPlayDateId());
		
		int postCount = 0;
    	SqlRowSet postCountResult = jdbcTemplate.queryForRowSet(sql1);
    	while (postCountResult.next()) {
    		postCount = postCountResult.getInt("count");
    	}
		
    	assertEquals(initialCount - 1, postCount);
		
		
	}
	
	private Playdate createTempPlaydate(String username, String date) {
		Playdate playdate = new Playdate();
		playdate.setUsername(username);
		playdate.setPlaydateDate(date);
		playdate.setAddress("1234 Generic Rd");
		playdate.setCity("Podunk");
		playdate.setRegion("Ohio");
		playdate.setZipcode("12345");
		playdate.setPlaydateTime("2:00 pm");
		
		return playdate;
	}
	
	private Pet createTempPet() {
		Pet pet = new Pet();
    	pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	
    	return pet;
	}

}
