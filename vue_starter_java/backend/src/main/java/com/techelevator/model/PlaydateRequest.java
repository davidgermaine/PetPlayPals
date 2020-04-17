package com.techelevator.model;

public class PlaydateRequest {
	
	private int requestId;
	private int playdateId;
	private String username;
	private int petId;
	
	
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public int getPlaydateId() {
		return playdateId;
	}
	public void setPlaydateId(int playdateId) {
		this.playdateId = playdateId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getPetId() {
		return petId;
	}
	public void setPetId(int petId) {
		this.petId = petId;
	}
	@Override
	public String toString() {
		return "PlaydateRequest [requestId=" + requestId + ", playdateId=" + playdateId + ", username=" + username
				+ ", petId=" + petId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + petId;
		result = prime * result + playdateId;
		result = prime * result + requestId;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlaydateRequest other = (PlaydateRequest) obj;
		if (petId != other.petId)
			return false;
		if (playdateId != other.playdateId)
			return false;
		if (requestId != other.requestId)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
