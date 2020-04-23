import Vue from 'vue';
import App from './App.vue';
import router from './router';
import './plugins/element.js';

import Amplify, * as AmplifyModules from 'aws-amplify'
import { AmplifyPlugin } from 'aws-amplify-vue'
//import awsconfig from './aws-exports'

import { AmplifyEventBus } from 'aws-amplify-vue';

Amplify.configure({
    Auth : {
        region: 'eu-central-1',
        userPoolId: 'eu-central-1_Dw8ZNiETz',
        userPoolWebClientId: '6h4ndn21ij45q3rqtrj2m0r6nv'
    }
});

Vue.use(AmplifyPlugin, AmplifyModules)

Vue.config.productionTip = false;

const vue=new Vue({ 
  router,
  render: h => h(App)
}).$mount('#app');


AmplifyEventBus.$on('authState', info => {
  console.log(`Here is the auth event that was just emitted by an Amplify component: ${info}`)
  vue.$Amplify.Auth.currentSession().then(session => {
    console.log(session);
    // idToken.jwtToken

    // Keys siehe : https://cognito-idp.eu-central-1.amazonaws.com/eu-central-1_Dw8ZNiETz/.well-known/jwks.json
    // siehe auch : https://docs.aws.amazon.com/de_de/cognito/latest/developerguide/amazon-cognito-user-pools-using-tokens-verifying-a-jwt.html
    //              https://aws.amazon.com/premiumsupport/knowledge-center/decode-verify-cognito-json-token/
    //              https://github.com/rrohitramsen/aws_jwt_cognito
    /*{
      "sub": "deda321f-7ccb-4309-978a-8fbfb7882a4f",
      "email_verified": true,
      "iss": "https://cognito-idp.eu-central-1.amazonaws.com/eu-central-1_Dw8ZNiETz",
      "phone_number_verified": false,
      "cognito:username": "andre.rieck",
      "aud": "6h4ndn21ij45q3rqtrj2m0r6nv",
      "event_id": "2fdb0875-bdb6-4cce-ae1f-a67255deacfd",
      "token_use": "id",
      "auth_time": 1586971923,
      "phone_number": "+49040123456",
      "exp": 1586975523,
      "iat": 1586971923,
      "email": "blubb@test.de"
    }*/



  });
});



