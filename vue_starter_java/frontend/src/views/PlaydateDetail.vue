<template>
    <div>
        <navigation />
        <h1 id="header"> Playdate Details </h1>
        <div class="location">
            <h2> Location </h2>
            <div>
                <p> {{playdate.address}}</p>
                <p>{{playdate.city}}, {{playdate.region}} {{playdate.zipcode}} </p>
                <p>{{playdate.playdateDate}} at {{playdate.playdateTime}}</p>
            </div>
        </div>

        <button id="playdate-detail-button"><router-link :to="{name: 'profile'}"> Go Back </router-link> </button>

        <div id="playdate-details-grid">
            <div class="pets">
                <h2> Pets Joining this Playdate </h2>
                <div v-for='pet in petsAlreadySignedUp' :key='pet.petName' class="pet-details-card">
                    <p><strong> Pet's Owner: </strong> {{pet.username}}</p>
                    <p><strong>Pet Name: </strong>{{pet.petName}}</p>
                    <p><strong>Pet Type: </strong>{{pet.petSpecies}}</p>
                    <p><strong>Breed: </strong>{{pet.petBreed}}</p>
                    <p><strong>Age Group: </strong>{{pet.petAgeGroup}}</p>
                    <p> <strong>Activity Level: </strong>{{pet.petActivityLevel}}</p>
                    <p><strong>Pet Description: </strong>{{pet.petDescription}}</p>
                </div>
            </div>

            <div class="messenger">
                <h2> Playdate Squad Messenger </h2>
                <button  id="playdate-detail-button" v-show="!messageForm" v-on:click.prevent="messageForm = true"> Add a message </button>
                <div class="messageForm">
                    <form v-show="messageForm">
                        <label for="message">Message:</label> <br>
                        <textarea name="message" id="message" cols="40" rows="10" placeholder="What would you like to say?" v-model="playdateMessage.message"></textarea>
                       <br> <button id="playdate-detail-button" v-on:click.prevent="addNewMessage"> Submit Message </button>
                        <button id="playdate-detail-button" v-on:click.prevent="clearForm"> Cancel </button>
                    </form>
                </div>

                <div v-for='message in chatMessages' :key='message.playdatechatid' class="message">
                    <div id="message-name">{{message.username}} </div> <div id="message-date">{{message.date}}</div>
                    <p> {{message.message}} </p>
                </div>
            </div>
        </div>
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
            messageForm: false,
            petsAlreadySignedUp: [],
            playdate: {},
            chatMessages: [],
            playdateMessage: {
                playdateId: "",
                username: "",
                message: ""
            }
        }
    },
    created() {
        this.playdateMessage.username = authLib.getUser().sub;
        this.playdateMessage.playdateId = this.$route.params.id;
        this.getPetsJoiningPlaydate(this.$route.params.id);
        this.getPlaydate(this.$route.params.id);
        this.getChatMessages(this.$route.params.id);
    },

    methods:{
        addNewMessage(){
                 fetch(`${process.env.VUE_APP_REMOTE_API}/addChatMessage`,
                {
                method: 'POST',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(this.playdateMessage)
            })
            .then (
                (response) => {if(response.ok) {
                    this.clearForm(); 
                }
              }
            )
            .catch(
                (err) => {console.error(err + 'problem adding message!'); }
            )
        },
        clearForm(){
            this.messageForm = false;
            this.getChatMessages(this.$route.params.id);
            this.playdateMessage.message = "";
        },
        getChatMessages(id){
             fetch(`${process.env.VUE_APP_REMOTE_API}/getChatMessages/${id}`,
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
            (data) => {this.chatMessages = data;}
        )
        .catch((err) => {console.error(err + 'no messages to fetch');})
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
    }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Comic+Neue:wght@300&family=Indie+Flower&family=Manrope:wght@300&family=Raleway:wght@300&display=swap');
.messenger form{
    text-align: center;
}
.messenger{
    margin: 30px 20% 30px 20%;
    background: wheat;
}
#playdate-detail-button{
        border-radius: 4px;
    padding: 7px 20px;
    border: 2px solid rgb(181, 181, 252);
    background-color: rgb(203, 203, 250);
    color: black;
    margin: 6px 0 0 0;  
}
.messenger h2{
    text-align: center;
}
.messageForm{
    margin: 40px;
    text-align: right;    
}
.pet-details-card{
    border: 1px solid black;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
    width: 400px;
    height: auto;
    margin: auto;
    padding: 16px 16px 16px 16px;
    text-align: left;
    font-family: arial;
    float: center;
    background-color: #ffe6e6;
    border-radius: 12px;  
}
.pets {
    grid-area: pets;
}

.messenger {
    grid-area: messages;
}

.message {
    border: 1px solid black;
    box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
    width: 400px;
    height: auto;
    margin: auto;
    padding: 8px 8px 8px 8px;
    text-align: left;
    font-family: arial;
    float: center;
    background-color: #ffe6e6;
    border-radius: 12px; 
}

#message-name {
    font-weight: bold;
}

#message-date {
    font-style: italic;
}

#playdate-details-grid {
    display: grid;
    grid-template-columns: 1fr 10fr 1fr 10fr 1fr;
    grid-template-areas: ". pets . messages .";
}
.pets h2{
    font-family: 'Indie Flower';
    font-size: 40px;
}
.messenger h2{
    font-family: 'Indie Flower';
    font-size: 40px;
}
</style>