package com.techelevator.DaoTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.ForumPost;
import com.techelevator.model.JdbcForumPostDao;
import com.techelevator.model.JdbcPetDao;
import com.techelevator.model.Pet;

public class ForumPostDaoTest {
	
	private static SingleConnectionDataSource dataSource;
	private JdbcForumPostDao forumPostDAO;
	private JdbcPetDao petDAO;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/userdb");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		forumPostDAO = new JdbcForumPostDao(dataSource);
		petDAO = new JdbcPetDao(dataSource);
		
		String testSql = "INSERT INTO users (id, username, password, salt) VALUES (100, 'testAccount', 'unhashedPassword', 'unsalted')";
		jdbcTemplate.update(testSql);
	}
	
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void createNewPost_creates_new_post() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = "01/01/2000";
		LocalDate testDate = LocalDate.parse(date, formatter);
		
		int initialCount = 0;
		String sql1 = "SELECT COUNT(*) FROM forumpost";
    	SqlRowSet initialCountResult = jdbcTemplate.queryForRowSet(sql1);
    	while (initialCountResult.next()) {
    		initialCount = initialCountResult.getInt("count");
    	}
		
		ForumPost testPost = new ForumPost();
		testPost.setUsername("testAccount");
		testPost.setForumPostTitle("Test Title");
		testPost.setForumPostComment("This is a generic test comment.");
		testPost.setForumPostDate(testDate);
		forumPostDAO.createNewPost(testPost);
		
		int postCount = 0;
    	SqlRowSet postCountResult = jdbcTemplate.queryForRowSet(sql1);
    	while (postCountResult.next()) {
    		postCount = postCountResult.getInt("count");
    	}
    	
    	assertEquals(initialCount + 1, postCount);
	}
	
	@Test
	public void getAllPosts_returns_all_posts() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = "01/01/2000";
		LocalDate testDate = LocalDate.parse(date, formatter);
		
		List<ForumPost> initialPosts = forumPostDAO.getAllPosts();
		
		ForumPost testPost = new ForumPost();
		testPost.setUsername("testAccount");
		testPost.setForumPostTitle("Test Title");
		testPost.setForumPostComment("This is a generic test comment.");
		testPost.setForumPostDate(testDate);
		forumPostDAO.createNewPost(testPost);
		
		List<ForumPost> postPosts = forumPostDAO.getAllPosts();
		
		assertEquals(initialPosts.size() + 1, postPosts.size());
	}
	
	@Test
	public void getPostsByUsername_returns_post_by_user() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = "01/01/2000";
		LocalDate testDate = LocalDate.parse(date, formatter);
		
		ForumPost testPost = new ForumPost();
		testPost.setUsername("testAccount");
		testPost.setForumPostTitle("Test Title");
		testPost.setForumPostComment("This is a generic test comment.");
		testPost.setForumPostDate(testDate);
		forumPostDAO.createNewPost(testPost);
		
		int testPostId = testPost.getForumPostId();
		
		List<ForumPost> returnPosts = forumPostDAO.getPostsByUsername("testAccount");
		assertNotNull(returnPosts);
		assertEquals(1, returnPosts.size());
		
		ForumPost returnedPost = returnPosts.get(0);
		assertTrue(returnedPost.getForumPostId() == testPostId);
		assertTrue(returnedPost.getUsername().equals(testPost.getUsername()));
		assertTrue(returnedPost.getForumPostTitle().equals(testPost.getForumPostTitle()));
		assertTrue(returnedPost.getForumPostComment().equals(testPost.getForumPostComment()));
		assertTrue(returnedPost.getForumPostDate().equals(testPost.getForumPostDate()));
	}
	
	@Test
	public void getPostsByPetName_returns_post_with_pet_name() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = "01/01/2000";
		LocalDate testDate = LocalDate.parse(date, formatter);
		
		ForumPost testPost = new ForumPost();
		testPost.setUsername("testAccount");
		testPost.setForumPostTitle("Test Title");
		testPost.setForumPostComment("This is a generic test comment.");
		testPost.setForumPostDate(testDate);
		forumPostDAO.createNewPost(testPost);
		
		ForumPost testPost2 = new ForumPost();
		testPost2.setUsername("testAccount");
		testPost2.setForumPostTitle("Test Title 2");
		testPost2.setForumPostComment("This is somehow not a generic test comment.");
		testPost2.setForumPostDate(testDate);
		forumPostDAO.createNewPost(testPost2);
		
		Pet pet = new Pet();
		pet.setUsername("testAccount");
    	pet.setPetName("Francis");
    	pet.setPetSpecies("Dog");
    	pet.setPetBreed("Mix");
    	pet.setPetAgeGroup("Middle-aged");
    	pet.setPetActivityLevel("Relaxed");
    	pet.setPetDescription("Rescue, pitbull-mix");
    	petDAO.createPet(pet);
    	
    	List<ForumPost> postsWithPet = forumPostDAO.getPostsByPetName(pet.getPetName());
    	assertNotNull(postsWithPet);
    	assertEquals(2, postsWithPet.size());
    	
    	ForumPost returnedPost = postsWithPet.get(0);
		assertTrue(returnedPost.getForumPostId() == testPost.getForumPostId());
		assertTrue(returnedPost.getUsername().equals(testPost.getUsername()));
		assertTrue(returnedPost.getForumPostTitle().equals(testPost.getForumPostTitle()));
		assertTrue(returnedPost.getForumPostComment().equals(testPost.getForumPostComment()));
		assertTrue(returnedPost.getForumPostDate().equals(testPost.getForumPostDate()));
	}
	
	@Test
	public void updatePost_changes_post_details() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = "01/01/2000";
		LocalDate testDate = LocalDate.parse(date, formatter);
		
		ForumPost testPost = new ForumPost();
		testPost.setUsername("testAccount");
		testPost.setForumPostTitle("Test Title");
		testPost.setForumPostComment("This is a generic test comment.");
		testPost.setForumPostDate(testDate);
		forumPostDAO.createNewPost(testPost);
		
		ForumPost changedPost = new ForumPost();
		changedPost.setForumPostTitle("A different title");
		changedPost.setForumPostComment("A different comment");
		forumPostDAO.updatePost(testPost.getForumPostId(), changedPost);
		
		List<ForumPost> updatedPost = forumPostDAO.getPostsByUsername("testAccount");
		assertNotNull(updatedPost);
		assertEquals(1, updatedPost.size());
		
		ForumPost returnedPost = updatedPost.get(0);
		assertTrue(returnedPost.getForumPostId() == testPost.getForumPostId());
		assertTrue(returnedPost.getUsername().equals(testPost.getUsername()));
		assertTrue(returnedPost.getForumPostTitle().equals(changedPost.getForumPostTitle()));
		assertTrue(returnedPost.getForumPostComment().equals(changedPost.getForumPostComment()));
		assertTrue(returnedPost.getForumPostDate().equals(testPost.getForumPostDate()));
	}
	
	@Test
	public void deletePost_removes_post_from_database() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		String date = "01/01/2000";
		LocalDate testDate = LocalDate.parse(date, formatter);
		
		ForumPost testPost = new ForumPost();
		testPost.setUsername("testAccount");
		testPost.setForumPostTitle("Test Title");
		testPost.setForumPostComment("This is a generic test comment.");
		testPost.setForumPostDate(testDate);
		forumPostDAO.createNewPost(testPost);
		
		List<ForumPost> preList = forumPostDAO.getAllPosts();
		forumPostDAO.deletePost(testPost.getForumPostId());
		List<ForumPost> postList = forumPostDAO.getAllPosts();
		
		assertEquals(preList.size() - 1, postList.size());
	}

}
