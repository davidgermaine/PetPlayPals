<template>
    <div class = "editPlaydate-card">
        <h3>Edit playdate</h3>
        <div class="form">
            <form>
                <div class="form-questions">
                <div class="form-input">
                    <span class="label">Date:  </span>
                    <input type="date" v-model="playdate.playdateDate"><br>
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
                    <input type="text" v-model="playdate.playdateTime">
                </div>
                <div>
                <button id="button-playdate" v-bind:disabled="!isValidForm" v-on:click="editPlaydate"> Update </button>
                <button id="button-playdate" v-on:click.prevent="deletePlaydate">Delete</button>
                <button id="button-playdate" v-on:click.prevent="cancel">Cancel</button>
                </div>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
import authLib from '@/auth';

export default {
    data() {
        return {
            playdate: {
                playdateId: this.initial.playdateId,
                username: this.initial.username,
                playdateDate: this.initial.playdateDate,
                address: this.initial.address,
                city: this.initial.city,
                region: this.initial.region,
                zipcode: this.initial.zipcode,
                playdateTime: this.initial.playdateTime
            }
        }
    },
    computed: {
        isValidForm() {
            return this.playdate.playdateDate != '' && this.playdate.address != '' && this.playdate.city != '' 
            && this.playdate.region != '' && this.playdate.zipcode != '' && this.playdate.playdateTime != '';
        },
    },
    methods: {
        editPlaydate(){
            console.log(this.playdate);

            fetch(`${process.env.VUE_APP_REMOTE_API}/profilePlaydate`, {
                method: 'PUT',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(this.playdate)
            })
            .then (
                console.log(this.playdate),
                (response) => {if(response.ok) {
                    this.cancel();
                }
              }
            )
            .catch(
                (err) => {console.error(err + 'problem editing playdate!'); }
            )
        },

        cancel(){
            this.playdate = this.initial;
            this.$emit('togglePlaydateEdit');
        },

        cancelForDelete(value) {
            value = this.initial;
            this.$emit('togglePlaydateDelete', value);
            this.$emit('togglePlaydateEdit');
        },

        deletePlaydate() {
            fetch(`${process.env.VUE_APP_REMOTE_API}/profilePlaydate/${this.playdate.playdateId}`, {
                method: 'DELETE',
                headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(this.playdate)
            })
            .then (
                (response) => {if(response.ok) {
                    this.cancelForDelete(this.playdate);
                }
              }
            )
            .catch(
                (err) => {console.error(err + 'problem deleting playdate!'); }
            )
        },
    },
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
        }
    },
    created() {
        console.log(this.playdate);
    }
}
</script>

<style>
    .editPlaydate-card h3 {
        text-align: center;
    }
    .form-input{
        text-align: left;
    }

</style>