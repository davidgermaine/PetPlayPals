package com.techelevator.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.techelevator.authentication.AuthProvider;
import com.techelevator.authentication.JwtTokenHandler;
import com.techelevator.authentication.UnauthorizedException;
import com.techelevator.authentication.UserCreationException;
import com.techelevator.model.Pet;
import com.techelevator.model.PetDao;
import com.techelevator.model.PetPlayDate;
import com.techelevator.model.PetPlayDateDao;
import com.techelevator.model.Playdate;
import com.techelevator.model.PlaydateChat;
import com.techelevator.model.PlaydateChatDao;
import com.techelevator.model.PlaydateDao;
import com.techelevator.model.PlaydateRequest;
import com.techelevator.model.PlaydateRequestDao;
import com.techelevator.model.User;
import com.techelevator.model.UserDao;


/**
 * AccountController
 */
@RestController
@CrossOrigin   
public class AccountController {
    @Autowired
    private AuthProvider auth;
    
    @Autowired 
    private PetDao petDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private PlaydateDao playdateDao;
    
    @Autowired
    private PetPlayDateDao petPlaydateDao;
    
    @Autowired
    private JwtTokenHandler tokenHandler;
    
    @Autowired
    private PlaydateRequestDao playdateRequestDao;
    
    @Autowired
    private PlaydateChatDao playdateChatDao;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(@RequestBody User user, RedirectAttributes flash) throws UnauthorizedException {
        if (auth.signIn(user.getUsername(), user.getPassword())) {
            User currentUser = auth.getCurrentUser();
            return tokenHandler.createToken(user.getUsername(), currentUser.getRole());
        } else {
            throw new UnauthorizedException();
        }
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(@Valid @RequestBody User user, BindingResult result) throws UserCreationException {
        if (result.hasErrors()) {
            String errorMessages = "";
            for (ObjectError error : result.getAllErrors()) {
                errorMessages += error.getDefaultMessage() + "\n";
            }
            throw new UserCreationException(errorMessages);
        }
        auth.register(user.getUsername(), user.getPassword(), user.getRole());
        return "{\"success\":true}";
    }
    
    @PostMapping("/newPet")   // indicates this method with handle HTTP POST requests for the /newPuppy
	@ResponseStatus(HttpStatus.CREATED)
	public void addPet(@RequestBody Pet pet) { 
    	petDao.createPet(pet); // add the puppy passed in to the database
	}
    
    @PostMapping("/newPlaydate")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPlaydate(@RequestBody Playdate playdate) {
    	playdateDao.createNewPlaydate(playdate);
    }
    
    @GetMapping(path= {"/profilePets/{username}"})
    public List<Pet> returnPetsData(@PathVariable String username) {
    List<Pet> allPets = petDao.getPetsByUsername(username);
    return allPets;
    }
    
    @GetMapping(path = {"/profilePlaydates/{username}"})
    public List<Playdate> returnPlaydateForUser(@PathVariable String username) {
    	String now = LocalDate.now().toString();
    	List<Playdate> userPlaydates = playdateDao.getPlaydatesByUsername(username, now);
    	return userPlaydates;
    }
    
    @PutMapping("/profilePets/{petId}")  
	@ResponseStatus(HttpStatus.OK)
	public void updatePet(@RequestBody Pet petToUpdate, @PathVariable int petId) {
					
	petDao.updatePet(petId, petToUpdate); 
	}
    
    @GetMapping(path = {"/signedUpPlaydatesForNonPoster/{username}"})
    public List<Playdate> returnPlaydateForNonUser(@PathVariable String username) {
    	String now = LocalDate.now().toString();
    	List<Playdate> playdates = playdateDao.getScheduledPlaydatesFromNonPoster(username, now);
    	return playdates;
    }
    
    @DeleteMapping("/profilePets/{petId}")
    @ResponseStatus(HttpStatus.OK)
    public void removePetById(@PathVariable int petId) {
    	petPlaydateDao.deletePetPlayDateByPet(petId);
    	petDao.removePetById(petId);
	}
    
    @PutMapping("/profilePlaydate")
    @ResponseStatus(HttpStatus.OK)
    public void updatePlaydate(@RequestBody Playdate playdate) {
    	playdateDao.updatePlaydate(playdate);
    }
    
    @DeleteMapping("/profilePlaydate/{playdateId}")
    @ResponseStatus(HttpStatus.OK)
    public void removePlaydate(@RequestBody Playdate playdate) {
    	petPlaydateDao.deletePetPlayDate(playdate.getPlaydateId());
    	playdateChatDao.deleteMessages(playdate.getPlaydateId());
    	playdateDao.deletePlaydate(playdate.getPlaydateId());
    }
    
    @GetMapping(path = {"/availablePlaydates"})
    public List<Playdate> returnAvailablePlaydates() {
    	String date = LocalDate.now().toString();
    	List<Playdate> playdates = playdateDao.getPlaydatesInFuture(date);
    	return playdates;
    }
    
    @GetMapping(path = {"/getPlaydateRequests/{playdateId}"})
    public List<PlaydateRequest> returnPlaydateRequestById(@PathVariable int playdateId) {
    	List<PlaydateRequest> requests = playdateRequestDao.getPlaydateRequestById(playdateId);
    	return requests;
    }
    
    @GetMapping(path = {"/getPlaydateRequestsByUsername/{username}"})
    public List<PlaydateRequest> returnPlaydateRequestByUsername(@PathVariable String username) {
    	List<PlaydateRequest> requests = playdateRequestDao.getPlaydateRequestsByUsername(username);
    	return requests;
    }
    
    @PostMapping("/newPlaydateRequest")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPlaydateRequest(@RequestBody PlaydateRequest playdateRequest) {
    	playdateRequestDao.addPlaydateRequest(playdateRequest);
    }
    
    @GetMapping(path = {"/getPet/{petId}"})
    public Pet returnPetbyId(@PathVariable int petId) {
    	Pet pet = petDao.getPetById(petId);
    	return pet;
    }
    
    @GetMapping(path = {"/getPlaydate/{playdateId}"})
    public Playdate returnPlaydateById(@PathVariable int playdateId) {
    	Playdate playdate = playdateDao.getPlaydateById(playdateId);
    	return playdate;
    }
    
    @DeleteMapping("/deleteRequest/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeRequest(@PathVariable int requestId) {
    	playdateRequestDao.deletePlayDate(requestId);
    }
    
    @PostMapping("/addPlayDatePet")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPlaydatPet(@RequestBody PetPlayDate petPlayDate) {
    	petPlaydateDao.createPetPlayDate(petPlayDate);
    }
    
    @GetMapping(path = {"/getPetsByPlaydateId/{playdateId}"})
    public List<Pet> returnPetsByPlaydateId(@PathVariable int playdateId) {
    	List<Pet> pets = petDao.getPetsByPlaydateId(playdateId);
    	return pets;
    }

    @GetMapping(path = {"/getChatMessages/{playdateId}"})
    public List<PlaydateChat> returnChatMessagesPlaydateId(@PathVariable int playdateId) {
    	List<PlaydateChat> messages = playdateChatDao.getAllMessagesByPlaydateId(playdateId);
    	return messages;
    }
    
    @PostMapping("/addChatMessage")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPlaydatPet(@RequestBody PlaydateChat playdateChat) {
    	String now = LocalDate.now().toString();
    	playdateChat.setDate(now);
    	playdateChatDao.addNewMessage(playdateChat);
    }
    
    
}