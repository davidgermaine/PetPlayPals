<template>
<div>
  <h2 class="center" id="other-title"> Other User's Playdates I'm Attending </h2>
   <div class = "card" v-for="playdate in playdates" :key= 'playdate.playdateid'>
       <div class = "playdate">
            <div id="user">Posted By: {{playdate.username}}</div>
            <div id="time">{{playdate.playdateDate}} at {{playdate.playdateTime}}</div>
            <div id="space"></div>
            <div id="address">
                {{playdate.address}}<br>
                {{playdate.city}}, {{playdate.region}} {{playdate.zipcode}}
            </div>
        <div></div>
        </div>
        <router-link tag="button" :to="{name: 'playdateDetail', params: {id: playdate.playdateId}}" id="chat-btn"> Details/Chat </router-link>
   </div>
</div>
</template>

<script>
import authLib from '@/auth';
export default {
    data() {
        return {
            username: "",
            playdates: []
        }
    },
    created(){
        this.username = authLib.getUser().sub;
            fetch(`${process.env.VUE_APP_REMOTE_API}/signedUpPlaydatesForNonPoster/${authLib.getUser().sub}/`,
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
            (data) => {this.playdates = data;}
        )
        .catch((err) => {console.error(err + 'you have no playdates to view.');})
    }
}
</script>

<style scoped>
/* h1{
    text-align: left;
} */
/* .card {
    border: 2px solid black;
    margin: 8px 0 8px 0;
    padding: 0 0 8px 0;
    border-radius: 15px;
    width: 100%;
    float: center;
} */
.playdate {
    display: grid;
        margin: 4px 0 4px 0;
        grid-template-columns: 2fr 10fr 1fr 10fr 2fr;
        grid-template-areas:
            ". username . time ."
            "space space space space space"
            ". address . . .";
}
.playdate #user {
    display: inline-block;
    text-align: left;
    font-weight: bold;
    grid-area: username;
}
.playdate #time {
    display: inline-block;
    font-style: italic;
    grid-area: time;
        
}
.playdate #address {
    text-align: left;
    display: inline-block;
    grid-area: address;
}
.playdate #space {
    padding: 8px;
    grid-area: space;
}
#chat-btn {
    border-radius: 4px;
    padding: 1.5%;
    border: 2px solid rgb(206, 206, 206);
    background-color: rgb(207, 207, 207);
    color: black;
    margin: 6px 0 0 0; 
    width: 40%;
    font-size: 1.2vw; 
}
#other-title {
    font-size: 3vw;
}

</style>