<template>
  <div id="app">
    <div>
      <banner-pic />
      <navigation />
    </div>
    <router-view/>
  </div>
</template>

<script>
import BannerPic from '@/component/Header'

export default {
  components: {
    BannerPic,
  },
}

</script>

<style>
  body {
    font-family: sans-serif;
  }

  
</style>