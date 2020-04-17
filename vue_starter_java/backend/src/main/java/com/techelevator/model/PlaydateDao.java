package com.techelevator.model;

import java.util.List;

public interface PlaydateDao {
	
	List <Playdate> getPlaydatesByUsername(String username, String now);
	List <Playdate> getPlaydatesInFuture(String date);
	List <Playdate> getScheduledPlaydatesFromNonPoster(String username, String date);
	
	void createNewPlaydate(Playdate playdate);
	void updatePlaydate(Playdate playdate);
	void deletePlaydate(int playdateId);
	Playdate getPlaydateById(int playdateId);
}
