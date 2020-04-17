package com.techelevator.model;

import java.util.List;

public interface ForumPostDao {
	
List <ForumPost> getAllPosts();
List<ForumPost> getPostsByUsername(String username);
List <ForumPost> getPostsByPetName(String petName);

public void createNewPost(ForumPost post);
public void updatePost(int forumPostId, ForumPost post);

public void deletePost(int forumPostid);
}
