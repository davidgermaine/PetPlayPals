package com.techelevator.DaoTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.JdbcPetDao;
import com.techelevator.model.Pet;

public class PetDaoTest {
	
	private static SingleConnectionDataSource dataSource;
	private JdbcPetDao petDAO;
	
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
		petDAO = new JdbcPetDao(dataSource);
		
		String testSql = "INSERT INTO users (id, username, password, salt) VALUES (100, 'testAccount', 'unhashedPassword', 'unsalted')";
		jdbcTemplate.update(testSql);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void createPet_adds_pet_to_pet_table() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		int initialCount = 0;
		String sql1 = "SELECT COUNT(*) FROM pet";
    	SqlRowSet initialCountResult = jdbcTemplate.queryForRowSet(sql1);
    	while (initialCountResult.next()) {
    		initialCount = initialCountResult.getInt("count");
    	}
    	
    	Pet pet = new Pet();
    	pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	petDAO.createPet(pet);
    	
    	int postCount = 0;
		String sql2 = "SELECT COUNT(*) FROM pet";
    	SqlRowSet postCountResult = jdbcTemplate.queryForRowSet(sql2);
    	while (postCountResult.next()) {
    		postCount = postCountResult.getInt("count");
    	}
    	
    	assertEquals(initialCount + 1, postCount);
	}
	
	@Test
	public void getAllPets_returns_all_pets() {
		List<Pet> initialPets = petDAO.getAllPets();
		int initialCount = initialPets.size();
		
		Pet pet = new Pet();
		pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	petDAO.createPet(pet);
    	
    	List<Pet> postPets = petDAO.getAllPets();
    	assertNotNull(postPets);
    	
    	int postCount = postPets.size();
    	assertEquals(initialCount + 1, postCount);
	}
	
	@Test
	public void getPetsByUserId_returns_all_pets_under_username() {
		Pet pet = new Pet();
		pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	petDAO.createPet(pet);
    	
    	Pet pet2 = new Pet();
    	pet2.setUsername("testAccount");
    	pet2.setPetName("Benny");
    	pet2.setPetSpecies("Dog");
    	pet2.setPetBreed("Beagle");
    	pet2.setPetAgeGroup("Middle-aged");
    	pet2.setPetActivityLevel("Lazy");
    	pet2.setPetDescription("Hind right leg injury, otherwise normal");
    	petDAO.createPet(pet2);
    	
    	List<Pet> testPets = petDAO.getPetsByUsername("testAccount");
    	assertNotNull(testPets);
    	assertEquals(2, testPets.size());
	}
	
	@Test
	public void getPetsBySpecies_returns_all_pets_of_one_species() {
		List<Pet> testDogs = petDAO.getPetsBySpecies("Dog");
		assertNotNull(testDogs);
		int dogCount = testDogs.size();
		
		List<Pet> testCats = petDAO.getPetsBySpecies("Cat");
		assertNotNull(testCats);
		int catCount = testCats.size();
		
		List<Pet> testHamsters = petDAO.getPetsBySpecies("Hamster");
		int hamsterCount = testHamsters.size();
		
		Pet pet = new Pet();
		pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	petDAO.createPet(pet);
    	
    	Pet pet2 = new Pet();
    	pet2.setUsername("testAccount");
    	pet2.setPetName("Felix");
    	pet2.setPetSpecies("Cat");
    	pet2.setPetBreed("Tabby");
    	pet2.setPetAgeGroup("Old");
    	pet2.setPetActivityLevel("Aggressive");
    	pet2.setPetDescription("Fat");
    	petDAO.createPet(pet2);
    	
    	Pet pet3 = new Pet();
    	pet3.setUsername("testAccount");
    	pet3.setPetName("Benny");
    	pet3.setPetSpecies("Dog");
    	pet3.setPetBreed("Beagle");
    	pet3.setPetAgeGroup("Middle-aged");
    	pet3.setPetActivityLevel("Lazy");
    	pet3.setPetDescription("Hind right leg injury, otherwise normal");
    	petDAO.createPet(pet3);
    	
    	testDogs = petDAO.getPetsBySpecies("Dog");
    	assertNotNull(testDogs);
    	assertEquals(dogCount + 2, testDogs.size());
    	
    	testCats = petDAO.getPetsBySpecies("Cat");
    	assertNotNull(testCats);
    	assertEquals(catCount + 1, testCats.size());
    	
    	testHamsters = petDAO.getPetsBySpecies("Hamster");
    	assertEquals(hamsterCount, testHamsters.size());
	}
	
	@Test
	public void getPetsByBreed_returns_all_pets_of_one_breed() {
		List<Pet> testMix = petDAO.getPetsByBreed("Mix");
		assertNotNull(testMix);
		int initialMix = testMix.size();
		
		List<Pet> testTabby = petDAO.getPetsByBreed("Tabby");
		assertNotNull(testTabby);
		int initialTabby = testTabby.size();
		
		Pet pet = new Pet();
		pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	petDAO.createPet(pet);
    	
    	Pet pet2 = new Pet();
    	pet2.setUsername("testAccount");
    	pet2.setPetName("Felix");
    	pet2.setPetSpecies("Cat");
    	pet2.setPetBreed("Tabby");
    	pet2.setPetAgeGroup("Old");
    	pet2.setPetActivityLevel("Aggressive");
    	pet2.setPetDescription("Fat");
    	petDAO.createPet(pet2);
    	
    	Pet pet3 = new Pet();
    	pet3.setUsername("testAccount");
    	pet3.setPetName("Benny");
    	pet3.setPetSpecies("Dog");
    	pet3.setPetBreed("Beagle");
    	pet3.setPetAgeGroup("Middle-aged");
    	pet3.setPetActivityLevel("Lazy");
    	pet3.setPetDescription("Hind right leg injury, otherwise normal");
    	petDAO.createPet(pet3);
    	
    	testMix = petDAO.getPetsByBreed("Mix");
    	assertEquals(initialMix + 1, testMix.size());
    	testTabby = petDAO.getPetsByBreed("Tabby");
    	assertEquals(initialTabby + 1, testTabby.size());
	}
	
	@Test
	public void getPetById_returns_single_pet_by_petId() {
		Pet pet = new Pet();
		pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	petDAO.createPet(pet);
    	
    	Pet pet2 = petDAO.getPetsByUsername(pet.getUsername()).get(0);
    	assertNotNull(pet2);
    	assertTrue(petDAO.getPetsByUsername(pet.getUsername()).get(0).getPetId() == pet2.getPetId());
    	assertTrue(pet.getUsername().equals(pet2.getUsername()));
    	assertTrue(pet.getPetName().equals(pet2.getPetName()));
    	assertTrue(pet.getPetSpecies().equals(pet2.getPetSpecies()));
    	assertTrue(pet.getPetBreed().equals(pet2.getPetBreed()));
    	assertTrue(pet.getPetAgeGroup().equals(pet2.getPetAgeGroup()));
    	assertTrue(pet.getPetActivityLevel().equals(pet2.getPetActivityLevel()));
    	assertTrue(pet.getPetDescription().equals(pet2.getPetDescription()));
	}
	
	@Test
	public void updatePet_updates_pet_info() {
		Pet pet = new Pet();
		pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	petDAO.createPet(pet);
    	
    	int testPetId = petDAO.getPetsByUsername(pet.getUsername()).get(0).getPetId();
    	
    	Pet pet2 = new Pet();
    	pet2.setUsername("testAccount");
    	pet2.setPetName("Felix");
    	pet2.setPetSpecies("Cat");
    	pet2.setPetBreed("Tabby");
    	pet2.setPetAgeGroup("Old");
    	pet2.setPetActivityLevel("Aggressive");
    	pet2.setPetDescription("Fat");
    	
    	petDAO.updatePet(testPetId, pet2);
    	Pet testPet = petDAO.getPetById(testPetId);
    	assertNotNull(pet2);
    	assertTrue(testPet.getUsername().equals(pet2.getUsername()));
    	assertTrue(testPet.getPetName().equals(pet2.getPetName()));
    	assertTrue(testPet.getPetSpecies().equals(pet2.getPetSpecies()));
    	assertTrue(testPet.getPetBreed().equals(pet2.getPetBreed()));
    	assertTrue(testPet.getPetAgeGroup().equals(pet2.getPetAgeGroup()));
    	assertTrue(testPet.getPetActivityLevel().equals(pet2.getPetActivityLevel()));
    	assertTrue(testPet.getPetDescription().equals(pet2.getPetDescription()));
	}
	
	@Test
	public void removePetById_removes_pet_from_database() {
		Pet pet = new Pet();
		pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	petDAO.createPet(pet);
    	
    	List<Pet> preDelete = petDAO.getAllPets();
    	int testPetId = petDAO.getPetsByUsername(pet.getUsername()).get(0).getPetId();
    	petDAO.removePetById(testPetId);
    	List<Pet> postDelete = petDAO.getAllPets();
    	
    	assertEquals(preDelete.size() - 1, postDelete.size());
	}

}
