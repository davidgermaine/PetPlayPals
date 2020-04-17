<template>
<div id="new-pet">
  <h3>Register a New Pet</h3>
     <form class="register-pet-form">
      <div id="register-pet-form-input">
        <span class="label">Pet Name:</span> <input type="text" v-model="pet.petName" placeholder="Enter Pet's Name">
      </div>
      <div id="register-pet-form-input">
        <span class="label">Pet Species:</span>
        <select id="species" name="species" v-model="pet.petSpecies">
          <option value="dog">Dog</option>
         <option value="other"> Other </option> 
        </select>
      </div>
      <div id="register-pet-form-input">
        <span class="label">Pet Breed/Type:</span> <input type="text" v-model="pet.petBreed" placeholder="Enter Pet's Breed">
      </div>
      <div id="register-pet-form-input">
        <span class="label">Pet Age Group:</span>
        <select id="age" name="age" v-model="pet.petAgeGroup">
          <option value="baby">Baby</option>
          <option value="young">Young</option>
          <option value="adult">Adult</option>
          <option value="senior">Senior</option>
        </select>
      </div>
      <div id="register-pet-form-input">
        <span class="label">Pet Activity Level:</span>
        <select id="actlvl" name="actlvl" v-model="pet.petActivityLevel">
          <option value="sedentary">Sedentary</option>
          <option value="moderate">Moderate</option>
          <option value="active">Active</option>
          <option value="very active">Very Active</option>
        </select>
      </div>
      <div id="register-pet-form-input">
        <span class="description">Pet Description:</span> <br>
        <textarea name="description" id="description" cols="40" rows="10" placeholder="Make sure to add approximate weight" v-model="pet.petDescription"></textarea>
      </div>
      <button id="add-cancel-button" v-bind:disabled="!isValidForm" v-on:click="addPet">Add Pet!</button>
     </form>
      <button id="add-cancel-button" v-on:click.prevent="cancel">Cancel</button>
    </div>
</template>

<script>
import authLib from '@/auth';
export default {
  data(){
    return {
      pet: {
        username:"",
        petName: "",
        petSpecies: "",
        petBreed: "",
        petAgeGroup: "",
        petActivityLevel: "",
        petDescription: ""
      }
    }
  },
  computed: {
    isValidForm() {
      return this.pet.petName != '' && this.pet.petBreed != '' && this.pet.petDescription != '';
    },
  },
  created(){
        this.pet = {};
        this.pet.username =  authLib.getUser().sub;
  },
  methods: {
    addPet(){
      fetch(`${process.env.VUE_APP_REMOTE_API}/newPet`,
                {
                method: 'POST',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(this.pet)
            })
            .then (
                (response) => {if(response.ok) { this.cancel();
                }
              }
            )
            .catch(
                (err) => {console.error(err + 'problem adding pet!'); }
            )
          },

    cancel(){
      this.$emit('toggleForm', false);
     
    }
    }    
        
}
</script>

<style scoped>
.register-pet-form{
  margin: 20px 30% 20px 30%;
}
.register-pet-form div {
  margin: 10px;
}
#register-pet-form-input {
text-align:left;
}

#add-cancel-button {
    border-radius: 4px;
    padding: 7px 20px;
    border: 2px solid rgb(206, 206, 206);
    background-color: rgb(207, 207, 207);
    color: black;
    margin: 0px 10px 10px 10px;    
}

#add-cancel-button:hover {
    box-shadow: 0 12px 16px 0 rgb(105, 105, 105), 0 17px 50px 0 rgb(63, 63, 63);
}
#new-pet{
   border: 1px solid black;
        box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);
        width: 100%;
        height: auto;
        margin: auto;
        text-align: center;
        font-family: arial;
        float: left;
        background-color: #ffe6e6;
        padding-left: 10px;
        padding-top: 10px;
        border-radius: 12px;  
}
#new-pet h3{
  font-size: 30px;
}

</style>