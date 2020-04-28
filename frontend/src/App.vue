<template>
    <div id="app">
        <el-container>
          <el-header>
              <img alt="logo" style="width: 200px; height: 100px" src="./assets/logo.jpg">
              <amplify-sign-out v-if="isSignedIn" button-text="Log me out"></amplify-sign-out>
          </el-header>
          <el-main>
              <amplify-authenticator></amplify-authenticator>
              <div v-if="isSignedIn">
                 <el-row type="flex" class="row-bg" style="margin-bottom:1em">
                   <el-col span="24">
                      <el-popover
                        placement="bottom"
                        width="300"
                        v-model="visible">
                        <el-input placeholder="Todo Headline" v-model="input" style="margin-bottom:0.5em"></el-input>
                        <el-input placeholder="Short Description" v-model="input" style="margin-bottom:0.5em"></el-input>
                        <div style="text-align: right; margin: 0">
                          <el-button size="mini" type="text" @click="visible = false">cancel</el-button>
                          <el-button type="primary" size="mini" @click="visible = false">create</el-button>
                        </div>
                        <el-button slot="reference" type="primary" style="float:right">new Task</el-button>
                      </el-popover>                   
                   </el-col>
                 </el-row>
                 <el-collapse v-for="todo in todos" :key="todo.id" v-model="activeNames" @change="handleChange" style="margin-bottom:0.1em">
                   <el-collapse-item :title="todo.headline" :name="todo.id">
                      <el-card class="box-card" style="margin-bottom: 0.5em;">
                        <div slot="header">
                           <span>{{ todo.shortDesc }}</span>
                           <el-button type="primary"  style="float: right; padding: 0.5em" icon="el-icon-delete"/>
                        </div>
                        <div v-for="item in todo.items" :key="item.seq" class="text item">
                           <el-checkbox>{{ item.name }}</el-checkbox>
                        </div>
                        <el-input placeholder="Please input" class="input-with-select" style="margin-top:0.4em;">
                          <el-button slot="append" type="primary" icon="el-icon-plus"/>
                        </el-input>
                      </el-card>
                   </el-collapse-item>
                 </el-collapse>
              </div>
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
                isSignedIn: false,
                todos : [
                    {
                        id : 0,
                        headline : "Einkaufen",
                        shortDesc : "go to walmart and buy groceries",
                        items : [
                            {   
                                seq : 0,
                                checked : true,
                                name : "tomatoes"
                            },
                            {
                                seq : 1,
                                checked : false,
                                name : "apples"
                            },
                            {
                                seq : 2,
                                checked : true,
                                name : "cucumbers"
                            }
                        ]
                    },{
                        id : 1,
                        headline : "Urlaub planen",
                        shortDesc : "prepare to fly away",
                        items : [
                            {
                                seq : 0,
                                checked : true,
                                name : "determine destination"
                            },
                            {
                                seq : 1,
                                checked : false,
                                name : "find hotel"
                            },
                            {
                                seq : 2,
                                checked : false,
                                name : "book flight"
                            },
                            {
                                seq : 3,
                                checked : false,
                                name : "book hotel"
                            }
                        ]
                    },{
                        id : 2,
                        headline : "Birthday preparation",
                        shortDesc : "plan my 47th birthday",
                        items : [
                            {
                                seq : 0,
                                checked : false,
                                name : "prepare invitation list"
                            },
                            {
                                seq : 1,
                                checked : false,
                                name : "find venue"
                            },
                            {
                                seq : 2,
                                checked : false,
                                name : "book venue"
                            }
                        ]
                    }
                ]
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
    
    div[data-test="sign-out-section"] > div:nth-child(1) {
        margin-bottom: 2.5em;
    } 

    div[data-test="sign-out-section"] button {
        border-radius: 4px;
    } 
</style>
