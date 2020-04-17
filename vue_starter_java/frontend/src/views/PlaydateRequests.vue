<template>
<div class="requests">
    <navigation />
    <div class="main-body">
    <div class="header">
  <p> My Requests </p>
    </div>
  <p class="no-requests" v-show="noMoreRequests"> No more Requests </p>
  <div class="request" v-for= 'request in playdateRequests' :key= 'request.requestId'>
  <p> Request from <span> {{request.username}}</span> </p>

   <router-link tag="button" :to="{name: 'playdateRequestDetail', params: { request}}"> Get Details </router-link>
  </div>
  <div class = "back-button">
   <button> <router-link :to="{name: 'profile'}"> Back </router-link> </button>
  </div>
</div>
</div>
</template>

<script>
import authLib from '@/auth';
import Navigation from '@/component/Nav'

export default {
    components:{
        Navigation
    },
    data(){
        return {
            username: "",
            playdateRequests: []
        }
    },
    computed: {
        noMoreRequests(){
            if(this.playdateRequests.length == 0){
                return true;
            } else{
                return false;
            }
        }
    },
    created(){
        this.username = authLib.getUser().sub;
        fetch(`${process.env.VUE_APP_REMOTE_API}/getPlaydateRequestsByUsername/${this.username}`,
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
            (data) => {this.playdateRequests = data;}
        )
        .catch((err) => {console.error(err + 'no playdates requests to fetch');})
    }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Comic+Neue:wght@300&family=Indie+Flower&family=Manrope:wght@300&family=Raleway:wght@300&display=swap');
.requests p{
    font-family: 'Raleway', sans-serif;
    float: none;
    font-size: 40px;
}
.header{
    text-align: center;
    width: 23%;
    padding: 20px 5px 20px 5px;
    margin: 30px;
   }
.request{
    padding: 30px 60px;
    border-style: solid;
    width: 20%;
    margin: 30px;
    border-width: 1px;
    background-color: wheat;
}
.no-requests{
    padding: 30px 60px;
    border-style: solid;
    margin: 30px 35% 30px 35%;
    border-width: 1px;
    background-color: wheat;
}
.request p {
    padding: 10px;
}
.request button{
    text-align: center;
    font-size: 20px;
    border-radius: 15px;
    width:100px;
    border-style: solid;
    border-color: black;
    height: 50px;
    cursor:pointer;
}
.back-button{
    text-align: left;
}
.back-button button{
    text-align: center;
    font-size: 20px;
    margin: 30px;
    border-radius: 15px;
    width:100px;
    border-style: solid;
    border-color: black;
    height: 50px;
     cursor:pointer;
}
.request p {
    font-size: 30px;
    font-family: 'Indie Flower', cursive;
}
.requests{
    text-align: center;
}
</style>