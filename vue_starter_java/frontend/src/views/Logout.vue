<template>
<div id='main'>
  <navigation />
    <div class="goodbye-image"> 
      <img :src= "require(`@/data/img/comebacksoon.jpg`)" id="sad-dog">
        <div class="goodbye-text">
            <h3> Come Back Soon! </h3>
            <router-link :to="{ name: 'login' }" tag="button" id="goodbye-button">Log Me Back In!</router-link>
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

created() {
authLib.logout();
}
}
</script>

<style>
#sad-dog {
    max-width: 100%;
    height: auto;
}

.goodbye-image {
    background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url("/data/img/pups.png");
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
    position: relative;
}

.goodbye-text {
    text-align: center;
    position: absolute;
    font-size: 2vw;
    top: 8%;
    left: 15%;
    transform: translate(-50%, -50%);
    color: white;
}
#goodbye-button {
    border-radius: 4px;
    padding: 1.5%;
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19);
    /* opacity: 60%; */
    border: 2px solid white;
    color: white;
    background-color: rgba(0,0,0, .25);
    transition-duration: 0.4s;
}
#goodbye-button:hover {
    font-weight: bolder;
  }
</style>