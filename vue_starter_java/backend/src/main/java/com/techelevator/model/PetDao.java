package com.techelevator.model;

import java.util.List;

public interface PetDao {
	
	public List<Pet> getAllPets();
	public List<Pet> getPetsByUsername(String username);
	public List<Pet> getPetsBySpecies(String petSpecies);
	public List<Pet> getPetsByBreed(String petBreed);
	public List<Pet> getPetsByPlaydateId(int playdateId);
	
	public Pet getPetById(int petId);
	
	public void createPet(Pet petToSave);

	public void removePetById(int petId);
	public void updatePet(int petId, Pet petToUpdate);

}
