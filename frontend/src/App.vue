<template>
    <div id="app">
        <el-container>
          <el-header>
              <img alt="logo" style="width: 200px; height: 100px" src="./assets/logo.jpg">
              <amplify-sign-out v-if="isSignedIn" button-text="Log me out"></amplify-sign-out>
          </el-header>
          <el-main>
              <amplify-authenticator></amplify-authenticator>
          </el-main>
        </el-container>       
    </div>
</template>

<script>
    import HelloWorld from './components/HelloWorld.vue';
    import {AmplifyEventBus, components} from 'aws-amplify-vue';
    import axios from 'axios';
    import config from './staging/config';

    export default {
        name: 'app',
        components: {
            HelloWorld,
            ...components
        },
        data() {
            return {
                isSignedIn: false
            }
        },
        methods: {
            determinLoginStatus: function () {
                this.$Amplify.Auth.currentSession().then(session => {
                    this.isSignedIn = true;
                }).catch(() => {
                    this.isSignedIn = false;
                })
            },
            handleClick() {
              console.log("fetching");
              axios({
                method: 'get',
                url: '/api/v1/hello',
                baseURL: config.baseURL
              }). then((result) => {
                console.log(result);
              })
            }
        },
        created() {
            this.determinLoginStatus();
            AmplifyEventBus.$on('authState', () => {
                this.determinLoginStatus();
            });
        }
    };
</script>

<style>
    #app {
        font-family: 'Avenir', Helvetica, Arial, sans-serif;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        color: #2c3e50;
        margin-top: 60px;
    }

    .el-header {
        display: flex;
        justify-content: space-between;
        align-items: self-end; 
        height: 80px;   
    }

    .el-header button {
        min-width: 80px;
    }

    div[data-test="sign-in-section"] {
        min-width: 80%;
    } 
    
</style>
