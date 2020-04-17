<template>
<div class="forms">
    <form v-on:submit.prevent="handleCityFilter">
        <input type="text" placeholder="City" v-model='searchCity'><button>Search For City</button>
    </form>
    <form v-on:submit.prevent="handleDateFilter">
        <input type="date" placeholder="Search for date" v-model='searchDate'><button>Search For Date</button>
    </form>
</div>     
</template>

<script>
export default {
    name: 'search-bar',
    data() {
        return {
            searchCity: '',
            searchDate: ''
        };
    },
    methods: {
        handleCityFilter(){
            this.$emit('cityFilter', this.searchCity);
        },
        handleDateFilter(){
            this.$emit('dateFilter', this.searchDate);
        }
    }
}
</script>

<style scoped>

form {
    display: flex;
    justify-content: flex-end;
    padding: 0.5em;
}

form * {
    padding: 0.5em;
    margin: 1em;
}

form input {
    flex: 2;
    font-size: 1em;
}

form button {
    width: 20%;
}

</style>
