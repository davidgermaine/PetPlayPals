<template>
<div id="main">
  <h3>Update Your Pet</h3>
     <form>
       <div class="update-pet-flexbox">
      <div class="form-input">
        <span class="label">Pet Name:</span> <input type="text" v-model="pet.petName" placeholder="Enter Pet's Name">
      </div>
      <div class="form-input">
        <span class="label">Pet Species:</span>
        <select id="species" name="species" v-model="pet.petSpecies">
          <option value="dog">Dog</option>
          <option value="other"> Other </option>
        </select>
      </div>
      <div class="form-input">
        <span class="label">Pet Breed/Type:</span> <input type="text" v-model="pet.petBreed" placeholder="Enter Pet's Breed">
      </div>
      <div class="form-input">
        <span class="label">Pet Age Group:</span>
        <select id="age" name="age" v-model="pet.petAgeGroup">
          <option value="puppy">Baby</option>
          <option value="young">Young</option>
          <option value="adult">Adult</option>
          <option value="senior">Senior</option>
        </select>
      </div>
      <div class="form-input">
        <span class="label">Pet Activity Level:</span>
        <select id="actlvl" name="actlvl" v-model="pet.petActivityLevel">
          <option value="sedentary">Sedentary</option>
          <option value="moderate">Moderate</option>
          <option value="active">Active</option>
          <option value="very active">Very Active</option>
        </select>
      </div>
      <div class="form-input">
        <span class="description">Pet Description:</span> <br>
        <textarea name="description" id="description" cols="40" rows="10" v-model="pet.petDescription"></textarea>
      </div>
      </div>
      <div>
        <button class="submit-button" v-bind:disabled="!isValidForm" v-on:click.prevent="updatePet">Update Pet!</button>
      </div>
     </form>
    <div>
    <button class="submit-button" v-on:click.prevent="cancel">Cancel</button>
    </div>
    </div>
</template>

<script>
import authLib from '@/auth';
export default {
  data(){
    return {

    }
  },
  props: {
      pet: {
        petId: "",
        username:"",
        petName: "",
        petSpecies: "",
        petBreed: "",
        petAgeGroup: "",
        petActivityLevel: "",
        petDescription: ""
      }
  },
    computed: {
    isValidForm() {
      return this.pet.petName != '' && this.pet.petBreed != '' && this.pet.petDescription != '';
    }
    },
    methods: {
    updatePet(){

      console.log('the pet being sent: ' + JSON.stringify(this.pet));

     fetch(`${process.env.VUE_APP_REMOTE_API}/profilePets/${this.pet.petId}`,
       {
            method: 'PUT',
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
      .catch((err) => {console.error(err + 'problem updating pet!');}
      )
      },
    cancel(){
      this.$emit('toggleForm', false);
        } 
    }
}

</script>

<style>
#main h3{
  font-size: 30px;
}
.update-pet-flexbox{
  display: flex;
  flex-direction: column;
}
.update-pet-flexbox div{
margin: 15px 30% 15px 30%;

}
.form-input span{
  font-size: 20px;
}

  .submit-button {
    border-radius: 4px;
    padding: 1.5%;
    border: 2px solid rgb(206, 206, 206);
    background-color: rgb(207, 207, 207);
    color: black;
    margin: 6px 10px 10px 10px;    
  }

  .submit-button:hover {
    box-shadow: 0 12px 16px 0 rgb(105, 105, 105), 0 17px 50px 0 rgb(63, 63, 63);
  }
  textarea{
    max-width: 800px;
  }
</style>