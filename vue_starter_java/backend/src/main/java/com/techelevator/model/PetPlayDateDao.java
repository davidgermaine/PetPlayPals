package com.techelevator.model;

public interface PetPlayDateDao {
	
	public void createPetPlayDate(PetPlayDate petPlayDate);	
	public void deletePetPlayDate(int playdateId);
	public void deletePetPlayDateByPet(int petId);

}
