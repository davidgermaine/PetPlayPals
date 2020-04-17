package com.techelevator.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcForumPostDao implements ForumPostDao {
	
	private final JdbcTemplate jdbcTemplate;


    @Autowired
    public JdbcForumPostDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    private ForumPost mapRowToForumPost(SqlRowSet result) {
    	ForumPost post = new ForumPost();
    	post.setForumPostId(result.getInt("forumid"));
    	post.setUsername(result.getString("username"));
    	post.setForumPostTitle(result.getString("forumposttitle"));
    	post.setForumPostComment(result.getString("forumpostcomment"));
    	post.setForumPostDate(LocalDate.parse(result.getString("forumpostdate")));
    	return post;
    }
	
	@Override
	public List<ForumPost> getAllPosts() {
		List<ForumPost>	allPosts = new ArrayList<>();
		String sql = "SELECT * FROM forumpost";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			ForumPost post = mapRowToForumPost(results);
			allPosts.add(post);
		}
		return allPosts;
	}
	

	@Override
	public List<ForumPost> getPostsByUsername(String username) {
		List<ForumPost>	postsByUsername = new ArrayList<>();
		String sql = "SELECT * FROM forumpost WHERE username=?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
		while(results.next()) {
			ForumPost post = mapRowToForumPost(results);
			postsByUsername.add(post);
		}
		return postsByUsername;
	}

	/*
	 * This method currently retrieves posts by checking for posts by users that have
	 * a pet with the specified name registered to their account.
	 * 
	 * Possibly add functionality for just checking if a post contains the specified string?
	 */
	@Override
	public List<ForumPost> getPostsByPetName(String petName) {
		List<ForumPost>	postByPet = new ArrayList<>();
		String sql = "SELECT forumpost.* FROM forumpost " + 
				"JOIN pet ON pet.username = forumpost.username " + 
				"WHERE pet.petname = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, petName);
		while(results.next()) {
			ForumPost post = mapRowToForumPost(results);
			postByPet.add(post);
		}
		return postByPet;
	}

	@Override
	public void createNewPost(ForumPost post) {
		String sql = "INSERT INTO forumpost (forumid, username, forumposttitle, forumpostcomment,"
				+ "forumpostdate) VALUES(?,?,?,?,?)";
		post.setForumPostId(getNextAvailableId());
		jdbcTemplate.update(sql, post.getForumPostId(), post.getUsername(), 
				post.getForumPostTitle(), post.getForumPostComment(),
				post.getForumPostDate());
	}

	@Override
	public void updatePost(int forumPostId, ForumPost post) {
		String sql = "UPDATE forumpost SET forumposttitle = ?, forumpostcomment = ? WHERE forumid = ?";	
		jdbcTemplate.update(sql, post.getForumPostTitle(),
				post.getForumPostComment(), forumPostId);				
	}

	@Override
	public void deletePost(int forumPostid) {
		String sql = "DELETE FROM forumpost WHERE forumid = ?";
			jdbcTemplate.update(sql, forumPostid);
	}
	
	private int getNextAvailableId() {
		int id = 0;
		String sql1 = "SELECT COUNT(*) FROM forumpost";
		SqlRowSet countResult = jdbcTemplate.queryForRowSet(sql1);
		
		while (countResult.next()) {
			int count = countResult.getInt("count");
			if (count == 0) {
				id = 1;
			} else {
				String sql2 = "SELECT forumid FROM forumpost ORDER BY forumid DESC LIMIT 1";
				SqlRowSet results = jdbcTemplate.queryForRowSet(sql2);
				while (results.next()) {
					id = results.getInt("forumid") + 1;
				}
			}
		}
		return id;
	}

}
