<template>
<div>
    <navigation />
    <div class = "signup"> 
        <h1> Playdate Details </h1>
        <div class="event-pet-details-card">
            <h2> Location </h2>
            {{playdate.address}} {{playdate.city}}, {{playdate.region}} {{playdate.zipcode}} <br>
            Date: {{playdate.playdateDate}} <br>
            Time: {{playdate.playdateTime}} <br>
            <br>   
        </div>
        <div class="event-pet-details-card">
            <h2> Pets already Signed Up </h2>
            <p v-for='pet in petsAlreadySignedUp' :key='pet.petName'>
            Pet's Owner : {{pet.username}} <br>
            Pet Name: {{pet.petName}}<br>
            Pet Type: {{pet.petSpecies}}<br>
            Breed: {{pet.petBreed}}<br>
            Age Group: {{pet.petAgeGroup}}<br>
            Activity Level: {{pet.petActivityLevel}}<br>
            Pet Description: {{pet.petDescription}}<br>
            </p>
        </div>
     
        <div>
            <div  v-for= 'pet in usersPets' :key= 'pet.petId'> 
                <div>
                    <button  v-on:click.prevent="playdateRequest.petId = pet.petId, 
                    checkIfAlreadyRequested(pet.petId)" class="pet-submit-button"> Request Playdate for {{pet.petName}} </button>
                </div>
            </div>
        </div>
    </div>
    <router-link :to="{name: 'profile'}" tag="button" class="pet-submit-button"> View playdates that have accepted you </router-link>
    <h3 v-if="requested" class="denied"> Pet has already requested this playdate </h3>
    <h3 v-if="signUp" class="accepted"> Playdate has been requested </h3>
    <button class="pet-submit-button"><router-link :to="{name: 'playdates'}"> Cancel </router-link> </button>
</div>

</template>

<script>
import authLib from '@/auth';
import Navigation from '@/component/Nav'

export default {
    components: {
        Navigation
    },
    data(){
        return{
            requested: false,
            signUp: false,
            petsAlreadySignedUp: [],
            usersPets: [],
            signedUpPets: [],
            playdatesById: [],
            playdate: {},
            playdateRequest: {
            username: "",
            petId: "",
            playdateId:""
            }
        }
    },
    created() {
        this.playdateRequest.playdateId = this.$route.params.id;
        this.playdateRequest.username = authLib.getUser().sub;
        this.getPetsJoiningPlaydate(this.$route.params.id);
        this.getPlaydate(this.$route.params.id);
        this.getOtherRequests();
        fetch(`${process.env.VUE_APP_REMOTE_API}/profilePets/${this.playdateRequest.username}`,
           {
                method: 'GET',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
            })

        .then( 
            (response) => {return response.json();} 
        )
        .then(
            (petData) => {this.usersPets = petData;}
        )
        .catch((err) => {console.error(err + 'you have no pets to view.');});
       
    },
    methods:{
        getPlaydate(id){
         fetch(`${process.env.VUE_APP_REMOTE_API}/getPlaydate/${id}`,
           {
                method: 'GET',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
            })
        .then( 
            (response) => {return response.json();} 
        )
        .then(
            (data) => {this.playdate = data;
         })
        .then(()=>{
         this.petPlayDate.playdateId = this.playdate.playdateId;
        })
        .catch((err) => {console.error(err + 'no playdate to fetch');})
        },
        getPetsJoiningPlaydate(id){ 
             fetch(`${process.env.VUE_APP_REMOTE_API}/getPetsByPlaydateId/${id}`,
           {
                method: 'GET',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
            })
        .then( 
            (response) => {return response.json();} 
        )
        .then(
            (data) => {this.petsAlreadySignedUp = data;}
        )
        .catch((err) => {console.error(err + 'no signed up pets to fetch');})
        },
        getOtherRequests(){
        fetch(`${process.env.VUE_APP_REMOTE_API}/getPlaydateRequests/${this.playdateRequest.playdateId}`,
           {
                method: 'GET',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
            })
        .then( 
            (response) => {return response.json();} 
        )
        .then(
            (data) => {this.playdatesById = data;}
        )
        .catch((err) => {console.error(err + 'no playdates requests to fetch');})
        },

        checkIfAlreadyRequested(id){
        this.signUp = false;
        this.requested = false;
        let alreadySignedUp = false;
        this.playdatesById.forEach(element => {
            if(element.petId == id){
               alreadySignedUp = true; 
            }
        });
         this.signedUpPets.forEach(element => {
            if(element == id){
               alreadySignedUp = true; 
            }
        });

        this.petsAlreadySignedUp.forEach(element => {
            if(element.petId == id){
                alreadySignedUp = true; 
            }
        });

        if(alreadySignedUp == false){
            this.signUpPet();
            this.signedUpPets.push(id);
        } else {
            this.requested = true;
        }
    },
    
    signUpPet(){
          fetch(`${process.env.VUE_APP_REMOTE_API}/newPlaydateRequest`,
                {
                method: 'POST',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(this.playdateRequest)
            })
            .then (
                (response) => {if(response.ok) { this.signUp = true;
                }
              }
            )
            .catch(
                (err) => {console.error(err + 'problem requesting playdate'); }
            )
        }
    }
}
</script>

<style>
body{
    text-align: center;
}
.accepted{
    color:blue;
}
.denied{
    color: red;
}
.event-pet-details-card{
    border: 1px solid black;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
    width: 40%;
    height: auto;
    margin: auto;
    text-align: center;
    font-family: arial;
    float: center;
    background-color: #ffe6e6;
    padding-left: 10px;
    padding-top: 10px;
    padding-bottom: 10px;
    border-radius: 12px;  
}
.pet-submit-button {
    border-radius: 4px;
    padding: 1%;
    border: 2px solid rgb(206, 206, 206);
    background-color: rgb(207, 207, 207);
    color: black;
    margin: 6px 10px 10px 10px;   
    width: 22%;
    
  }

  .pet-submit-button:hover {
    box-shadow: 0 12px 16px 0 rgb(105, 105, 105), 0 17px 50px 0 rgb(63, 63, 63);
  }
</style>