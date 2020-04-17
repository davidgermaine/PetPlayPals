import Vue from 'vue'
import App from './App.vue'
import router from './router'
import * as VueGoogleMaps from "vue2-google-maps";

Vue.config.productionTip = false

Vue.use(VueGoogleMaps, {
  load: {
    key: "AIzaSyBRuotMq6IT3ftGkHhpzxjN1e718udNPBc",
    libraries: "places" // necessary for places input
  }
});

new Vue({
  router,
  el: "#app",
  components: { App },
  template: "<App/>",
  render: h => h(App)
}).$mount('#app')
