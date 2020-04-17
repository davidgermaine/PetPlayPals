package com.techelevator.model;

import java.time.LocalDate;

public class ForumPost {
	
	private int forumPostId;
	private String username;
	private String forumPostTitle;
	private String forumPostComment;
	private LocalDate forumPostDate;
	
	public int getForumPostId() {
		return forumPostId;
	}
	
	public void setForumPostId(int forumPostId) {
		this.forumPostId = forumPostId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getForumPostTitle() {
		return forumPostTitle;
	}
	
	public void setForumPostTitle(String forumPostTitle) {
		this.forumPostTitle = forumPostTitle;
	}
	
	public String getForumPostComment() {
		return forumPostComment;
	}
	
	public void setForumPostComment(String forumPostComment) {
		this.forumPostComment = forumPostComment;
	}
	
	public LocalDate getForumPostDate() {
		return forumPostDate;
	}
	
	public void setForumPostDate(LocalDate forumPostDate) {
		this.forumPostDate = forumPostDate;
	}
	
}
