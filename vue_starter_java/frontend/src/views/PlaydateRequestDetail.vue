<template>
<div>
    <Navigation />
    <div class="request-card">
    <p id="header"> Playdate Request Details: </p>
    <p id="location">{{request.username}} has requested to join your playdate at {{playdate.address}} on {{playdate.playdateDate}}
    at {{playdate.playdateTime}}</p>
    <div class="pet-info">
    <h3> Pet Information: </h3>
    <p> Name: {{pet.petName}} </p>
    <p> Breed: {{pet.petBreed}} </p>
    <p> Activity Level: {{pet.petActivityLevel}} </p>
    <p> Age: {{pet.petAgeGroup}} </p>
    <p> Description: {{pet.petDescription}} </p>
    </div>
    <div id="buttons">
    <button v-on:click.prevent="acceptRequest()"> Accept Playdate </button>
    <button v-on:click.prevent="deleteRequest()"> Delete Playdate </button>
    <button><router-link :to="{name: 'playdateRequests'}"> Cancel </router-link> </button>
    
    </div></div>
</div>
</template>

<script>
import authLib from '@/auth';
import Navigation from '@/component/Nav'

export default {
data(){
    return{
        pet: {},
        playdate: {},
        petPlayDate: {
            playdateId: "",
            petId: ""
        }
    }
},
components: {
    Navigation
},
props: {
    request: {}
},
created(){
    this.getPet(this.request.petId);
    this.getPlaydate(this.request.playdateId);


},
methods: {
    getPet(id){
         fetch(`${process.env.VUE_APP_REMOTE_API}/getPet/${id}`,
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
            (data) => {this.pet = data;
        }
        )
        .then(()=>{
         this.petPlayDate.petId = this.pet.petId;
        })
        .catch((err) => {console.error(err + 'no pet to fetch');})
    },
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
    deleteRequest(){
         fetch(`${process.env.VUE_APP_REMOTE_API}/deleteRequest/${this.request.requestId}`, {
                method: 'DELETE',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
            })
            .then (
                (response) => {if(response.ok) {
                  this.$router.go(-1);
                }
              }
            )
            .catch(
                (err) => {console.error(err + 'problem deleting request'); }
            )
    },
    acceptRequest(){
         fetch(`${process.env.VUE_APP_REMOTE_API}/addPlayDatePet`,
                {
                method: 'POST',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(this.petPlayDate)
            })
            .then (
                (response) => {if(response.ok) { this.deleteRequest()
                }
              }
            )
            .catch(
                (err) => {console.error(err + 'problem adding request!'); }
            )
        }
    }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Comic+Neue:wght@300&family=Indie+Flower&family=Manrope:wght@300&family=Raleway:wght@300&display=swap');

.request-card{
    margin: 50px;
    text-align: center;
}
#location {
    font-size: 20px;
}
#header{
    font-size: 60px;
 
}
#buttons{
    display: flex;
    justify-content: center;
}
.pet-info{
    margin: 30px 35% 30px 35%;
    min-width: 500px;
    padding: 30px;
    font-size: 30px;
    font-family: 'Indie Flower';
    background-color: papayawhip;
    border-style: solid;
    border-width: 2px;
}
.request-card button{
        text-align: center;
    font-size: 20px;
    margin: 20px;
    border-radius: 15px;
    width:120px;
    border-style: solid;
    border-color: black;
    height: 50px;
     cursor:pointer;
}
</style>