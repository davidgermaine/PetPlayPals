<template>
<div>
  <navigation />
  <div id="date" v-bind:innerText="dateFilter"/>
    <div>
        <h1> Available Playdates </h1>
        <SearchBar v-on:cityFilter="handleCityFilter" v-on:dateFilter="handleDateFilter"/>
        <div class="playdates-card" id = "playdates-each" v-for= 'playdate in filteredPlaydates' :key= 'playdate.playdateid'>
            <div id="card-container">
            <p id="date"><strong>Date:</strong> {{playdate.playdateDate}} </p>
            <p><strong>Time:</strong> {{playdate.playdateTime}} </p>
            <p><strong>Location:</strong> {{playdate.address}}, {{playdate.city}}
            <p><strong>Hosted By:</strong> {{playdate.username}} </p>
            <span v-show="username != playdate.username">
            <router-link :to="{name: 'playdateSignUp', params: {id: playdate.playdateId}}" tag="button" class="playdates-detail-button"> Details </router-link>
            </span>
            <span v-show="username == playdate.username">
            <router-link :to="{name: 'profile'}" tag="button" class="playdates-detail-button"> Edit your listing </router-link>
            </span>
            </div>
        </div>
     <google-map/>
    </div>
  
</div>
</template>

<script>
import authLib from '@/auth';
import GoogleMap from "@/component/GoogleMap";
import Navigation from '@/component/Nav';
import SearchBar from '@/component/SearchBar';
 // import moment from 'moment';



export default {

    data(){
        return {
            availablePlaydates: [],
            username: "",
            dateFilter: "",
            cityFilter: ""
        }
    },
    computed:{
        filteredPlaydates(){

            const filteredArrayByLocation = this.availablePlaydates.filter((playdate) => {
                    return playdate.city.toUpperCase().includes(this.cityFilter.toUpperCase());
            });
     
            const filteredArrayByDate = this.availablePlaydates.filter((playdate) => {
                return playdate.playdateDate.includes(this.dateFilter);
            });

            const intersection = filteredArrayByLocation.filter((x) => filteredArrayByDate.includes(x));

            return intersection;
        }
    },
    methods: {

        handleDateFilter(search){
            console.log("this search: " + search);
            this.dateFilter = search; 
        },
        handleCityFilter(search){
            this.cityFilter = search;
        }
    },
    created() {
            this.username = authLib.getUser().sub;
            fetch(`${process.env.VUE_APP_REMOTE_API}/availablePlaydates/`,
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
            (data) => {this.availablePlaydates = data;}
        )
        .catch((err) => {console.error(err + 'you have no playdates to view.');})
    },
    name: 'mapdiv',
    components: {
    SearchBar,
    GoogleMap,
    Navigation,
    },
    mounted() {
        let map = this.$refs.map;
        map.dropMarker({ Latitude: 37, Longitude: -121 });
    }
}
</script>

<style>
.playdates-card {
    display: inline-block;
    width: 25%;
    border: 2px solid black;
    margin: 8px 12px 8px 0;
    padding: 0 0 8px 0;
    background-color: #ffe6e6;
    border-radius: 12px;
    
   
}
.card-image {
     width:100%; 
  
}

#playdates-each {
    color: black;
}
.playdates-detail-button {
    border-radius: 4px;
    border: 2px solid rgb(206, 206, 206);
    background-color: rgb(207, 207, 207);
    color: black;
    margin: 6px 10px 10px 10px;   
    max-width: 100%;
}
 .playdates-detail-button:hover {
    box-shadow: 0 12px 16px 0 rgb(105, 105, 105), 0 17px 50px 0 rgb(63, 63, 63);
  }

</style>