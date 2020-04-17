<template>
  <div>
    <div>
      <img :src= "require(`@/data/img/petplaypalsnewlogo.png`)" class="title-image" />
    </div>
    <div>
      <img :src= "require(`@/data/img/petsgeekbanner.png`)" class = "header-image" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'banner-pic'
}
</script>

<style>
.header-image {
 
    width : 100%;
}

.title-image {
  max-width: 100%;
  height: auto;
  float: left;
}

</style>