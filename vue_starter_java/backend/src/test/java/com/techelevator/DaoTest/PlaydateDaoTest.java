package com.techelevator.DaoTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.JdbcPlaydateDao;
import com.techelevator.model.Playdate;

public class PlaydateDaoTest {
	
	private static SingleConnectionDataSource dataSource;
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
		playdateDAO = new JdbcPlaydateDao(dataSource);
		
		String testSql = "INSERT INTO users (id, username, password, salt) VALUES (100, 'testAccount', 'unhashedPassword', 'unsalted')";
		String testSql2 = "INSERT INTO users (id, username, password, salt) VALUES (101, 'bill', 'unhashedPassword', 'unsalted')";
		jdbcTemplate.update(testSql);
		jdbcTemplate.update(testSql2);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void createNewPlaydate_creates_new_playdate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int initialCount = 0;
		String sql1 = "SELECT COUNT(*) FROM playdate";
    	SqlRowSet initialCountResult = jdbcTemplate.queryForRowSet(sql1);
    	while (initialCountResult.next()) {
    		initialCount = initialCountResult.getInt("count");
    	}
    	
		Playdate playdate = createTempPlaydate("testAccount", "2120-01-01");
		playdateDAO.createNewPlaydate(playdate);
		
		int postCount = 0;
    	SqlRowSet postCountResult = jdbcTemplate.queryForRowSet(sql1);
    	while (postCountResult.next()) {
    		postCount = postCountResult.getInt("count");
    	}
    	
    	assertEquals(initialCount + 1, postCount);
	}
	
//	@Test
//	public void getAllPlaydates_returns_all_playdates() {
//		List<Playdate> initialList = playdateDAO.getAllPlaydates();
//		assertNotNull(initialList);
//		
//		Playdate playdate = createTempPlaydate("testAccount", "2120-01-01");
//		playdateDAO.createNewPlaydate(playdate);
//		
//		List<Playdate> postList = playdateDAO.getAllPlaydates();
//		assertNotNull(postList);
//		assertTrue(postList.size() != 0);
//		assertEquals(initialList.size() + 1, postList.size());
//	}
	
	@Test
	public void getPlaydatesByUsername_returns_playdates_created_by_username() {
		Playdate playdate = createTempPlaydate("testAccount", "2120-01-01");
		playdateDAO.createNewPlaydate(playdate);
		Playdate playdate2 = createTempPlaydate("testAccount", "2020-01-01");
		playdateDAO.createNewPlaydate(playdate2);
		Playdate playdate3 = createTempPlaydate("bill", "2220-01-01");
		playdateDAO.createNewPlaydate(playdate3);
		
		List<Playdate> testList = playdateDAO.getPlaydatesByUsername("testAccount", LocalDate.now().toString());
		assertNotNull(testList);
		assertEquals(2, testList.size());
		
		Playdate testPlaydate = testList.get(0);
		assertTrue(playdate.getPlaydateId() == testPlaydate.getPlaydateId());
		assertTrue(playdate.getUsername().equals(testPlaydate.getUsername()));
		assertTrue(playdate.getPlaydateDate().equals(testPlaydate.getPlaydateDate()));
		assertTrue(playdate.getAddress().equals(testPlaydate.getAddress()));
		assertTrue(playdate.getCity().equals(testPlaydate.getCity()));
		assertTrue(playdate.getRegion().equals(testPlaydate.getRegion()));
		assertTrue(playdate.getZipcode().equals(testPlaydate.getZipcode()));
		assertTrue(playdate.getPlaydateTime().equals(testPlaydate.getPlaydateTime()));
	}
	
	@Test
	public void getPlaydatesInFuture_returns_all_future_playdates() {
		Playdate playdate = createTempPlaydate("testAccount", "2120-01-01");
		playdateDAO.createNewPlaydate(playdate);
		Playdate playdate2 = createTempPlaydate("testAccount", "2020-01-01");
		playdateDAO.createNewPlaydate(playdate2);
		
		List<Playdate> testList = playdateDAO.getPlaydatesInFuture("2021-01-01");
		assertNotNull(testList);
		assertEquals(1, testList.size());
		
		Playdate testPlaydate = testList.get(0);
		assertTrue(playdate.getPlaydateId() == testPlaydate.getPlaydateId());
		assertTrue(playdate.getUsername().equals(testPlaydate.getUsername()));
		assertTrue(playdate.getPlaydateDate().equals(testPlaydate.getPlaydateDate()));
		assertTrue(playdate.getAddress().equals(testPlaydate.getAddress()));
		assertTrue(playdate.getCity().equals(testPlaydate.getCity()));
		assertTrue(playdate.getRegion().equals(testPlaydate.getRegion()));
		assertTrue(playdate.getZipcode().equals(testPlaydate.getZipcode()));
		assertTrue(playdate.getPlaydateTime().equals(testPlaydate.getPlaydateTime()));
	}
	
//	@Test
//	public void signUpForPlaydate_signs_pet_up_for_playdate() {
//		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//		int initialCount = 0;
//		String sql1 = "SELECT COUNT(*) FROM pet_playdate";
//    	SqlRowSet initialCountResult = jdbcTemplate.queryForRowSet(sql1);
//    	while (initialCountResult.next()) {
//    		initialCount = initialCountResult.getInt("count");
//    	}
//    	
//		Playdate playdate = createTempPlaydate("testAccount", LocalDate.parse("2120-01-01"));
//		playdateDAO.createNewPlaydate(playdate);
//		Pet pet = createTempPet();
//		petDAO.createPet(pet);
//		
//		playdateDAO.signUpForPlaydate(playdate.getPlaydateId(), pet.getPetId());
//		
//		int postCount = 0;
//    	SqlRowSet postCountResult = jdbcTemplate.queryForRowSet(sql1);
//    	while (postCountResult.next()) {
//    		postCount = postCountResult.getInt("count");
//    	}
//    	assertEquals(initialCount + 1, postCount);
//	}
	
	@Test
	public void updatePlaydate_changes_playdate_attributes() {
		Playdate playdate = createTempPlaydate("testAccount", "2120-01-01");
		playdateDAO.createNewPlaydate(playdate);
		
		Playdate updatedPlaydate = new Playdate();
		updatedPlaydate.setPlaydateId(playdate.getPlaydateId());
		updatedPlaydate.setUsername(playdate.getUsername());
		updatedPlaydate.setPlaydateDate("2420-12-12");
		updatedPlaydate.setAddress("9876 Road St.");
		updatedPlaydate.setCity("MiddleOfNowhere");
		updatedPlaydate.setRegion("Indiana");
		updatedPlaydate.setZipcode("29445");
		updatedPlaydate.setPlaydateTime("12:00pm");
		
		assertTrue(updatedPlaydate.getPlaydateId() == playdate.getPlaydateId());
		
		playdateDAO.updatePlaydate(updatedPlaydate);
		Playdate test = playdateDAO.getPlaydatesByUsername("testAccount", LocalDate.now().toString()).get(0);
		assertTrue(test.getPlaydateId() == playdate.getPlaydateId());
		assertTrue(test.getUsername().equals(playdate.getUsername()));
		assertTrue(test.getPlaydateDate().equals(updatedPlaydate.getPlaydateDate()));
		assertTrue(test.getAddress().equals(updatedPlaydate.getAddress()));
		assertTrue(test.getCity().equals(updatedPlaydate.getCity()));
		assertTrue(test.getRegion().equals(updatedPlaydate.getRegion()));
		assertTrue(test.getZipcode().equals(updatedPlaydate.getZipcode()));
		assertTrue(test.getPlaydateTime().equals(updatedPlaydate.getPlaydateTime()));
	}
	
//	@Test
//	public void deletePlaydate_deletes_playdate() {
//		Playdate playdate = createTempPlaydate("testAccount", "2120-01-01");
//		playdateDAO.createNewPlaydate(playdate);
//		
//		List<Playdate> list = playdateDAO.getAllPlaydates();
//		int initialCount = list.size();
//		assertTrue(initialCount != 0);
//		
//		int id = playdate.getPlaydateId();
//		playdateDAO.deletePlaydate(id);
//		list = playdateDAO.getAllPlaydates();
//		int postCount = list.size();
//		
//		assertEquals(initialCount - 1, postCount);
//	}
	
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

}
