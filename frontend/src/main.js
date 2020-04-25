import Vue from 'vue';
import App from './App.vue';
import router from './router';
import './plugins/element.js';
import axios from 'axios';

import Amplify, * as AmplifyModules from 'aws-amplify'
import {AmplifyEventBus, AmplifyPlugin} from 'aws-amplify-vue'

Amplify.configure({
    Auth: {
        region: 'eu-central-1',
        userPoolId: 'eu-central-1_wr3AourNH',
        userPoolWebClientId: '6gi7km51i3jou67q1j5pdenavv'
    }
});

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
            console.log(session.idToken.jwtToken);
            axios.defaults.headers.common['Authorization'] = `Bearer ${session.idToken.jwtToken}`;
        })
    } else if (info === 'signedOut') {
        axios.defaults.headers.common['Authorization'] = undefined;
    }
});



