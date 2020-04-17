<template>
  <div>
  <div>
<p>Enter your location</p>
<label>
<gmap-autocomplete
@place_changed="setPlace">
</gmap-autocomplete>
<button @click="addMarker">Add</button>
</label>
<br/>

</div>
    <br>
    <gmap-map
      :center="center"
      :zoom="9"
      style="width:100%;  height: 400px;"
    >

<gmap-info-window
    :options="{maxWidth: 300}"
    :position="infoWindow.position"
    @closeclick="infoWindow.open=true"
    :content="infoWindow.content"
    >
     <div v-html="infoWindow.template"></div>
</gmap-info-window>
     
      <gmap-marker
          :key="index"
          v-for="(m, index) in markers"
          :position="m.position"
          :label="m.label"
          @click="setInfoWindow(m)"
      ></gmap-marker>

    </gmap-map>
  </div>
</template>

<script>
import authLib from '@/auth';
export default {
  name: "GoogleMap",
  data() {
    return {
      
      center: { lat: 42.3390, lng: -83.09 },
      markers: [],
      places: [],
      currentPlace: null,
      infoWindow: {
          position: {lat: 42.3390, lng: -83.09},
          open: false,
          template: '',
          content : 'sdfsdfsdfs'
        }
    };
  },

  
created() {
  fetch(`${process.env.VUE_APP_REMOTE_API}/availablePlaydates`,
                {
                    method: 'GET',
                    headers: {
                    Authorization: "Bearer " + authLib.getToken(),
                    Accept: "application/json",
                    "Content-Type": "application/json"
                },
            })

            .then( (response) => {return response.json();} )
            .then( (playdateData) => {
                this.userPlaydates = playdateData;

                for(let index = 0; index < this.userPlaydates.length; index++) {

                  // 3. playdateDescription is the text that goes into the infoBox, right now it's just set to the date, but
                  // you can change that! You are however limited by what's in the PlayDate class on the backend.

                  let marker = { "position" : {"lat": parseFloat(this.userPlaydates[index].latitude), 
                  "lng": parseFloat(this.userPlaydates[index].longitude) }, 
                  "playdateDescription" : [this.userPlaydates[index].address, this.userPlaydates[index].playdateDate, this.userPlaydates[index].playdateTime],
                
                  }; 

                  this.markers.push(marker); 
                  this.center= {lat: parseFloat(this.userPlaydates[0].latitude), lng: parseFloat(this.userPlaydates[0].longitude)}; 
                  this.infoWindow.position ={lat: parseFloat(this.userPlaydates[0].latitude), lng: parseFloat(this.userPlaydates[0].longitude)}; 
                }

                

            })
            .catch((err) => {console.error(err + 'you have no playdates to view.');})
        },


  mounted() {
    this.geolocate();
  },

  methods: {

    setInfoWindow(marker) {

      // 1. infowindow is centered onto the marker's position:
      this.infoWindow.position = {lat: marker.position.lat, lng: marker.position.lng}; 

      // 2. infowindow text is set to the playdateDescription.... for now it's just the date.
      this.infoWindow.template = marker.playdateDescription;
      

      this.infoWindow.open = true;

    },


    setPlace(place) {
      this.currentPlace = place;
    },

    addMarker() {
      if (this.currentPlace) {
        const marker = {
          lat: this.currentPlace.geometry.location.lat(),
          lng: this.currentPlace.geometry.location.lng()
        };
        this.markers.push({ position: marker });
        this.places.push(this.currentPlace);
        this.center = marker;
        this.currentPlace = null;
       
      }
    },

    geolocate: function() {
      navigator.geolocation.getCurrentPosition(position => {
        this.center = {
          lat: position.coords.latitude,
          lng: position.coords.longitude
        };
      });
    }
  }
};
</script>

<style>
  .gm-ui-hover-effect {
    visibility: hidden;
  }
</style>