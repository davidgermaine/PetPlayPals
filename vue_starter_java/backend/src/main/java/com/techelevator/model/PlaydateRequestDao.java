package com.techelevator.model;

import java.util.List;

public interface PlaydateRequestDao {
	
	List<PlaydateRequest> getPlaydateRequestsByUsername(String username);
	void deletePlayDate(int requestId);
	void addPlaydateRequest(PlaydateRequest request);
	List <PlaydateRequest> getPlaydateRequestById(int id);

}
