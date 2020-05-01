import Vue from 'vue';
import App from './App.vue';
import router from './router';
import './plugins/element.js';
import axios from 'axios';
import Amplify, * as AmplifyModules from 'aws-amplify'
import {AmplifyEventBus, AmplifyPlugin} from 'aws-amplify-vue'
import config from './staging/config'

console.log(JSON.stringify(config));
console.log(JSON.stringify(config.amplifyAuth));
Amplify.configure({ Auth: config.amplifyAuth });

Vue.use(AmplifyPlugin, AmplifyModules)

Vue.config.productionTip = false;

const vue = new Vue({
    router,
    render: h => h(App)
}).$mount('#app');

AmplifyEventBus.$on('authState', info => {
    console.log(`Here is the auth event that was just emitted by an Amplify component: ${info}`)
    if (info === 'signedIn') {
        vue.$Amplify.Auth.currentSession().then(session => {
            axios.defaults.headers.common['Authorization'] = `Bearer ${session.idToken.jwtToken}`;
            console.log(axios.defaults.headers.common['Authorization']);
        })
    } else if (info === 'signedOut') {
        axios.defaults.headers.common['Authorization'] = undefined;
    }
});



