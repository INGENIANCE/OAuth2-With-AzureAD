import Vue from 'vue'
import App from './App.vue'
import api from './services/api';

import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'

// Import Bootstrap an BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

// Make BootstrapVue available throughout your project
Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin)

Vue.config.productionTip = false;

Vue.prototype.$http = api;
api.defaults.timeout = 10000;

api.interceptors.response.use(
  response => {
    if ([200, 201].indexOf(response.status) !== -1) {
      return Promise.resolve(response);
    } else {
      return Promise.reject(response);
    }
  },
  error => {
    if ([401, 403].indexOf(error.response.status) !== -1) {
      // Go to login page if 401 Unauthorized or 403 Forbidden response returned from api
      window.location = '/login';
    }

    return Promise.reject(error.response);
  }
);

new Vue({
  render: h => h(App),
}).$mount('#app')
