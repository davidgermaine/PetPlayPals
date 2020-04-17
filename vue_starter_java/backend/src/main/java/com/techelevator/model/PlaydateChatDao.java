package com.techelevator.model;

import java.util.List;

public interface PlaydateChatDao {
	
	void addNewMessage(PlaydateChat playdateChat);
	List <PlaydateChat> getAllMessagesByPlaydateId(int playdateId);
	public void deleteMessages(int playdateId);
	
}
