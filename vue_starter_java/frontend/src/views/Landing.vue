<template>
    <div class="nav">
        <div>
            <navigation />
        <div class="landing-image"> 
            <img :src= "require(`@/data/img/pups.png`)" id="dogs-playing">
            <div class="landing-text">
            <h1> Welcome to Pet Play Pals! </h1>
            <p>Sign in to create playdates for your pets! </p>
            <router-link :to="{ name: 'playdates' }" tag="button" id="landing-button">See Events Nearby</router-link>
            </div>
        </div>
        </div>
    </div>
    
</template>


<script>
 import Navigation from '@/component/Nav'

export default {
    components: {
        Navigation
    }
}
</script>

<style>
#dogs-playing {
    max-width: 100%;
    height: auto;
}

.landing-image {
    background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url("/data/img/pups.png");
    background-position: center;
    background-repeat: no-repeat;
    background-size: cover;
    position: relative;
}

.landing-text {
    text-align: center;
    position: absolute;
    font-size: 2vw;
    top: 20%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: white;
}

.landing-desc {
    font-size: 25px;
    text-align: center;
    position: relative;
    top: 10%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: white;
}

#landing-button {
    border-radius: 4px;
    padding: 1.5%;
    box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2), 0 6px 20px 0 rgba(0,0,0,0.19);
    /* opacity: 60%; */
    border: 2px solid white;
    color: white;
    background-color: rgba(0,0,0, .25);
    transition-duration: 0.4s;
}
#landing-button:hover {
    font-weight: bolder;
  }
</style>