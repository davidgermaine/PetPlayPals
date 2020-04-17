<template>
<div id="edit-form">
  <h3>Create a new playdate</h3>
   <div class="form">
     <form>
       <div class="form-questions">
      <div class="form-input">
        <span class="label">Date:  </span>
        <input type="date" v-model="playdate.playdateDate">
      </div>
      <div class="form-input">
        <span class="label">Address:  </span>
        <input type="text" v-model="playdate.address">
      </div>
      <div class="form-input">
        <span class="label">City:  </span>
        <input type="text" v-model="playdate.city">
      </div>
      <div class="form-input">
        <span class="label">State/Providence:  </span>
        <input type="text" v-model="playdate.region">
      </div>
      <div class="form-input">
        <span class="label">Zip Code:  </span>
        <input type="text" v-model="playdate.zipcode">
      </div>
      <div class="form-input">
        <span class="label">Time:  </span>
        <input type="time" v-model="playdate.playdateTime">
      </div>
       </div>
      <button id="pd-button" v-bind:disabled="!isValidForm" v-on:click="addPlaydate"> Create playdate! </button>
     </form>
      <button id="pd-button" v-on:click.prevent="cancel">Cancel</button>
    </div>
</div>
</template>

<script>
import authLib from '@/auth';
export default {
  props: {
    userPets: Array
  },
  data(){
    return {
      playdate: {
        username:"",
        playdateDate:"",
        address:"",
        city:"",
        region:"",
        zipcode:"",
        latitude: "",
        longitude: "",
        playdateTime:""
      }
    }
  },
  computed: {
    isValidForm() {
      return this.playdate.playdateDate != '' && this.playdate.address != '' && this.playdate.city != '' 
        && this.playdate.region != '' && this.playdate.zipcode != '' && this.playdate.playdateTime != '';
    },
  },
  created(){
        this.playdate = {};
        this.playdate.username = authLib.getUser().sub;
    },
  methods: {
    addPlaydate(){
      fetch(`${process.env.VUE_APP_REMOTE_API}/newPlaydate`,
        {
          method: 'POST',
            headers: {
              Authorization: "Bearer " + authLib.getToken(),
              Accept: "application/json",
               "Content-Type": "application/json"
            },
            body: JSON.stringify(this.playdate)
        }
      )
      .then (
          (response) => {if(response.ok) {
            this.cancel()
          }
        }
      )
      .catch(
        (err) => {console.error(err + 'problem adding playdate!'); }
      )
    },

    cancel(){
      this.$emit('toggleThisForm');
      console.log("canceling....");
    },
  },
}    
</script>

<style >
#pd-button{
  margin: 5px;
}
.form-questions{
  display:flex;
  flex-direction: column;
  flex-wrap:flex;
  -ms-flex-align: center;
}
.form-questions div{
  text-align: center;
}
#edit-form{
  background-color: wheat;
  width: 75%;
  border-style: solid;
  border-width: 1px;
  margin: 0px 20px 20px 0px;
  padding: 20px 0px 20px 0px;
}

</style>