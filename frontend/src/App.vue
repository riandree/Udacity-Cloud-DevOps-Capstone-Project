<template>
    <div id="app">
        <div>
            <p>
                If Element is successfully added to this project, you'll see an
                <code v-text="'<el-button>'"></code>
                below
            </p>
            <el-button @click="handleClick">el-button</el-button>
        </div>
        <HelloWorld msg="Welcome to Your Vue.js App"/>
        <amplify-authenticator></amplify-authenticator>
        <amplify-sign-out v-if="isSignedIn" button-text="Log me out"></amplify-sign-out>
    </div>
</template>

<script>
    import HelloWorld from './components/HelloWorld.vue';
    import {AmplifyEventBus, components} from 'aws-amplify-vue';
    import axios from 'axios';

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
                baseURL: 'http://localhost:8080'
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
        text-align: center;
        color: #2c3e50;
        margin-top: 60px;
    }
</style>
