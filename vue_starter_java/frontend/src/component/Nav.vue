<template>
  <div class="nav">
      
        <router-link :to="{ name: 'landing' }" tag="li" exact>Home</router-link>
        <router-link :to="{ name: 'login' }" tag="li" v-if="!isLoggedIn">Login</router-link> 
        <router-link :to="{ name: 'register' }" tag="li" v-if="!isLoggedIn">Register</router-link>
        <router-link :to="{ name: 'profile' }" tag="li" v-if="isLoggedIn" exact>My Profile</router-link>
        <router-link :to="{ name: 'playdates'}" tag = "li"> Playdates </router-link> 
        <router-link :to="{ name: 'logout' }" tag = "li" id= "navright" v-if="isLoggedIn"> Log Out </router-link>
      
      </div>
    
</template>

<script>
import authLib from '@/auth';

export default {
    data() {
        return {
        isLoggedIn: this.checkIfLoggedIn()
      }
    },

    methods: {
        checkIfLoggedIn() {
        console.log('user is: ' + authLib.getUser() );
        if (authLib.getUser() === null) {
        return false;
        } else {
        return true;  
        }
    }
    },

    components: {
    
  },

}


</script>

<style>
.nav {
    overflow: hidden;
    margin:0;
    padding:0;
    list-style-type: none;
    background-color: #cc0000;
  }

  .nav li {
    float: left;
    text-align: center;
    display: block;
    padding:2%;
    color: #fff;
    font-size: 1.4vw;
  }

  .nav li a {
    color: #fff;
    text-transform: uppercase;
    font-size: 16px;
    text-decoration: none;
  }
  
  .nav li:hover {
    background-color: rgb(216, 216, 216);
    cursor: pointer;
    color:rgb(44, 44, 44);
  }

  .nav li a:hover {
    color:rgb(44, 44, 44);
  }

  .router-link-active {
    background-color: rgb(44, 44, 44);
  }

  #navright {
      float: right;
      padding-right: 4% ;
  }
</style>