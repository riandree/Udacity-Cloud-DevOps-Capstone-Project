<template>
  <div id="app">
    <div>
      <p>
        If Element is successfully added to this project, you'll see an
        <code v-text="'<el-button>'"></code>
        below
      </p>
      <el-button>el-button</el-button>
    </div>
    <HelloWorld msg="Welcome to Your Vue.js App"/>
    <amplify-authenticator></amplify-authenticator>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import { components } from 'aws-amplify-vue'

export default {
  name: 'app',
  components: {
    HelloWorld,
    ...components
  },
    data () {
    return {
      authConfig: {
          usernameAttributes: 'user email',
          signUpConfig: {
            header: 'Sign Up',
            hideAllDefaults: true,
            defaultCountryCode: '1',
            signUpFields: [
              {
                label: 'email',
                key: 'email',
                required: true,
                displayOrder: 1,
                type: 'string',
              },
              {
                label: 'Password',
                key: 'password',
                required: true,
                displayOrder: 2,
                type: 'password'
              }
            ]
          }
        }
      }
    }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
