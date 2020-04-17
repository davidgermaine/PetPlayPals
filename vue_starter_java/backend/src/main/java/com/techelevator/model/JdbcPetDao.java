package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


@Component
public class JdbcPetDao implements PetDao {


	private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcPetDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    private Pet mapRowToPet(SqlRowSet result) {
    	Pet pet = new Pet();
    	pet.setPetId(result.getInt("petid"));
    	pet.setUsername(result.getString("username"));
    	pet.setPetName(result.getString("petname"));
    	pet.setPetSpecies(result.getString("petspecies"));
    	pet.setPetBreed(result.getString("petbreed"));
    	pet.setPetAgeGroup(result.getString("petagegroup"));
    	pet.setPetActivityLevel(result.getString("petactivitylevel"));
    	pet.setPetDescription(result.getString("petdescription"));
    
    	return pet;
    }
    
    private int getNextAvailableId() {
    	int id = 0;
    	String sql1 = "SELECT COUNT(*) FROM pet";
    	SqlRowSet countResult = jdbcTemplate.queryForRowSet(sql1);
    	while (countResult.next()) {
    		int count = countResult.getInt("count");
    		if (count == 0) {
    			id = 1;
    		} else {
    			String sql2 = "SELECT petid FROM pet ORDER BY petid DESC LIMIT 1";
    	    	SqlRowSet results = jdbcTemplate.queryForRowSet(sql2);
    	    	while (results.next()) {
    	    		id = results.getInt("petid") + 1;
    	    	}
    		}
    	}
    	return id;
    }
    
	
	@Override
	public List<Pet> getAllPets() {
		List <Pet> allPets = new ArrayList<>();
		String sql = "SELECT * FROM pet";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			Pet pet = mapRowToPet(results);
			allPets.add(pet);
		}
		return allPets;
	}
	
	@Override
	public List<Pet> getPetsByUsername(String username) {
		List <Pet> petByusername = new ArrayList<>();
		String sql = "SELECT * FROM pet WHERE username = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
		while(results.next()) {
			Pet pet = mapRowToPet(results);
			petByusername.add(pet);
		}
		return petByusername;
	}

	@Override
	public List<Pet> getPetsBySpecies(String petSpecies) {
		List <Pet> petByspecies = new ArrayList<>();
		String sql = "SELECT * FROM pet WHERE petspecies = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, petSpecies);
		while(results.next()) {
			Pet pet = mapRowToPet(results);
			petByspecies.add(pet);
		}
		return petByspecies;
	}
	
	@Override
	public List<Pet> getPetsByBreed(String petBreed) {
		List <Pet> petByBreed = new ArrayList<>();
		String sql = "SELECT * FROM pet WHERE petbreed = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, petBreed);
		while(results.next()) {
			Pet pet = mapRowToPet(results);
			petByBreed.add(pet);
		}
		return petByBreed;
	}

	@Override
	public Pet getPetById(int petId) {
		Pet petById = new Pet();
		String sql = "SELECT * FROM pet WHERE petid = ?";
		SqlRowSet results =jdbcTemplate.queryForRowSet(sql, petId);
		while (results.next()) {
			petById = mapRowToPet(results);
		}
		return petById;
	}
	
	@Override
	public void createPet(Pet petToSave) {
		String sql = "INSERT INTO pet (petid, username, petname, petspecies, petbreed, petagegroup,"
				+ "petactivitylevel, petdescription) VALUES(?,?,?,?,?,?,?,?)";
		petToSave.setPetId(getNextAvailableId());
		jdbcTemplate.update(sql, petToSave.getPetId(), petToSave.getUsername(), petToSave.getPetName(), petToSave.getPetSpecies(), 
				petToSave.getPetBreed(), petToSave.getPetAgeGroup(), petToSave.getPetActivityLevel(), petToSave.getPetDescription());
	}

	@Override
	public void updatePet(int petId, Pet petToUpdate) {
		String sql = "UPDATE pet SET username =?, petname =?, petspecies =?, petbreed =?,"
				+ " petagegroup =?, petactivitylevel =?, petdescription =? WHERE petid =?";
		jdbcTemplate.update(sql, petToUpdate.getUsername(), petToUpdate.getPetName(),
				petToUpdate.getPetSpecies(), petToUpdate.getPetBreed(), petToUpdate.getPetAgeGroup(), 
				petToUpdate.getPetActivityLevel(), petToUpdate.getPetDescription(), petId);
	}

	@Override
	public void removePetById(int petId) {
		String sql ="DELETE FROM pet WHERE petid=?";
		jdbcTemplate.update(sql, petId);
	}

	@Override
	public List<Pet> getPetsByPlaydateId(int playdateId) {
		List <Pet> pets = new ArrayList<>();
		String sql = "SELECT * FROM pet p" + 
				" JOIN pet_playdate pp ON p.petid = pp.petid WHERE pp.playdateId = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, playdateId);
		while(results.next()) {
			Pet pet = mapRowToPet(results);
			pets.add(pet);
		}
		return pets;	
		}

}
