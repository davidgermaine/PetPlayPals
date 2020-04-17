<template>
    <div id='main'>
        <div v-for= 'pet in allThePets' :key= 'pet.id'>
            <div class = 'petInfo' v-if="!updatePet">
                Pet Name: {{pet.petName}}<br>
                Pet Type: {{pet.petSpecies}}<br>
                Breed: {{pet.petBreed}}<br>
                Age Group: {{pet.petAgeGroup}}<br>
                Activity Level: {{pet.petActivityLevel}}<br>
                Pet Description: {{pet.petDescription}}<br>
                <div class="list-buttons">
                    <button v-on:click="updatePet = true ; selectedPet = pet" class="update-delete-button">Update Pet</button>
                </div>
                <div class="list-buttons">
                    <button v-on:click="deletePet(pet.petId)" class="update-delete-button">Delete Pet</button>
                </div>
                <hr>
            </div>
        </div>
        <update-pet v-on:toggleForm="toggleForm" v-show="updatePet" v-bind:pet="selectedPet"/>
    </div>

</template>

<script>
import authLib from '@/auth';
import UpdatePet from '@/component/UpdatePet';

export default {
    data() {
        return { 
            allThePets : [],
            selectedPet : {},
            updatePet: false,
            petId: ""

        }
    },
    components: {
        UpdatePet
    },
    props: {
        username: String
    },
    created() {         
      this.showPets();
    },
    methods: {
        toggleForm(){
            this.updatePet = false;
    },
    showPets(){
    fetch(`${process.env.VUE_APP_REMOTE_API}/profilePets/${this.username}`,
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
            (petData) => {
                this.allThePets = petData;
                this.$emit('userPets', this.allThePets);
            }
        )
        .catch((err) => {console.error(err + 'you have no pets to view.');})
    },
    deletePet(id) {
        fetch(`${process.env.VUE_APP_REMOTE_API}/profilePets/${id}`,
        {
            method:'DELETE',
            headers: {
                Authorization: "Bearer " + authLib.getToken(),
                Accept: "application/json",
                "Content-Type": "application/json"
                },
            })
            .then ((response) => {
                if(response.ok){
                    const index = this.allThePets.map(pet => pet.id).indexOf(id);
                    this.allThePets.splice(index,1);
                }
            })
            .catch ((err) => console.error(err));

        }                             
     }
}
        
    
</script>

<style>
.petInfo{
    text-align: left;
    font-size: 1.2vw;
}
.update-delete-button {
    border-radius: 4px;
    padding: 1.5%;
    background-color: rgb(207, 207, 207);
    color: black;
    margin: 6px 0 0 0;   
    font-size: 1.2vw;
    width: 30%;
  }

  .update-delete-button:hover {
    box-shadow: 0 12px 16px 0 rgb(105, 105, 105), 0 17px 50px 0 rgb(63, 63, 63);
  }

  .list-buttons {
      display: inline;
      padding-right: 10px;
  }
  
</style>