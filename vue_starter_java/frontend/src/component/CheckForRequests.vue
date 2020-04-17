<template>
  <div> </div>
</template>
<script>
import authLib from '@/auth';

export default {
    data(){
        return {
            username: "",
            playdateRequests: []
        }
    },
    created(){
        this.username = authLib.getUser().sub;
        fetch(`${process.env.VUE_APP_REMOTE_API}/getPlaydateRequestsByUsername/${this.username}`,
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
            (data) => {this.playdateRequests = data;}
        )
        .then(
            () => {this.$emit('hasRequests', this.hasRequests);
            }
        )
        .catch((err) => {console.error(err + 'no playdates requests to fetch');})
    },
    computed: {
        hasRequests(){
            if(this.playdateRequests.length != 0){
            return true;
            } return false;
        }
    }
}
</script>

<style>

</style>