<template>
    <div>
        <div class="card">
        <div class="playdate">
            <div id="user">{{initial.username}}</div>
            <div id="time">{{initial.playdateDate}} at {{initial.playdateTime}}</div>
            <div id="space"></div>
            <div id="address">
                {{initial.address}}<br>
                {{initial.city}}, {{initial.region}} {{initial.zipcode}}
            </div>
        </div>
        </div>
        <form class="add-pet-form">
            <label for="petId"> What pet would you like to sign up? </label>
            <select id="petId" name="petId" v-model="petPlayDate.petId">
                <option  v-for="pet in userPets" :key="pet.petId" v-bind:value="pet.petId" v-on:change="petPlayDate.petId = $event.target.value"> {{pet.petName}}</option>
            </select><br>
            <button v-bind:disabled="!isValidForm" v-on:click.prevent="addPetToPlaydate"> Add this pet! </button>
            <button v-on:click="cancel"> Cancel </button>
        </form>
    </div>
</template>

<script>
import authLib from '@/auth';
export default {
    props: {
        initial: {
            playdateId: "",
            username: "",
            playdateDate: "",
            address: "",
            city: "",
            region: "",
            zipcode: "",
            playdateTime: ""
        },
        userPets: Array
    },
    computed: {
        isValidForm() {
            return this.petPlayDate.petId != 0 && this.petPlayDate.playdateId != 0;
        }
    },
    data() {
        return {
            petPlayDate: {
                playdateId: this.initial.playdateId,
                petId: 0
            }
        }
    },
    methods: {
        addPetToPlaydate() {
            fetch(`${process.env.VUE_APP_REMOTE_API}/addPlayDatePet`,
            {
                method: 'POST',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                        "Content-Type": "application/json"
                },
                body: JSON.stringify(this.petPlayDate)
            })
            .then (
                (response) => {if(response.ok) { this.cancel(); }}
            )
            .catch(
                (err) => {console.error(err + 'problem adding pet!'); }
            )
        },
        
        cancel() {
            this.$emit('resetpage')
        }
    }

}
</script>

<style scoped>
div{
    text-align: left;
}
.add-pet-form{
    margin: 20px;
    font-size: 20px;
}
.add-pet-form button{
        border-radius: 4px;
    padding: 1.5%;
    border: 2px solid rgb(206, 206, 206);
    background-color: rgb(207, 207, 207);
    color: black;
    margin: 10px 10px 10px 10px;    
}
.add-pet-form button:hover{
    box-shadow: 0 12px 16px 0 rgb(105, 105, 105), 0 17px 50px 0 rgb(63, 63, 63);
}
.add-pet-form select{
  -webkit-appearance: menulist-button;
   height: 30px;
   margin: 10px;
   }
</style>