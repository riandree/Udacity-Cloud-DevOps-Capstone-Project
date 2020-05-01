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
                   <el-col>
                      <el-popover
                        placement="bottom"
                        width="300"
                        v-model="popoverVisible">
                        <el-input placeholder="Todo Headline" v-model="newToDoHeadline" style="margin-bottom:0.5em"></el-input>
                        <el-input placeholder="Short Description" v-model="newToDoDesc" style="margin-bottom:0.5em"></el-input>
                        <div style="text-align: right; margin: 0">
                          <el-button size="mini" type="text" @click="popoverCancel">cancel</el-button>
                          <el-button type="primary" size="mini" @click="popoverCreate">create</el-button>
                        </div>
                        <el-button slot="reference" type="primary" style="float:right">new Task</el-button>
                      </el-popover>                   
                   </el-col>
                 </el-row>
                 <el-collapse 
                     v-for="todo in todos" :key="todo.id" style="margin-bottom:0.1em">
                   <el-collapse-item :title="todo.headline" :name="todo.id">
                      <el-card class="box-card" style="margin-bottom: 0.5em;">
                        <div slot="header">
                           <span>{{ todo.shortDesc }}</span>
                           <el-button @click="deleteToDo(todo)" 
                              type="primary"  style="float: right; padding: 0.5em" icon="el-icon-delete"/>
                        </div>
                        <div v-for="item in todo.items" :key="item.seq" class="text item">
                           <el-checkbox>{{ item.name }}</el-checkbox>
                        </div>
                        <el-input
                            v-model="todo.newItem" 
                            placeholder="Please input" class="input-with-select" style="margin-top:0.4em;">
                          <el-button  
                               @click="createItem(todo)"
                               :disabled="!todo.newItem"
                               slot="append" type="primary" icon="el-icon-plus"/>
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
    import todoService from './service/todoService';

    export default {
        name: 'app',
        components: {
            HelloWorld,
            ...components
        },
        data() {
            return {
                isSignedIn: false,
                popoverVisible : false,
                newToDoHeadline : "",
                newToDoDesc : "",
                todos : []
            }
        },
        methods: {
            determinLoginStatus: function () {
                this.$Amplify.Auth.currentSession().then(session => {
                    this.isSignedIn = true;
                    todoService.getAllOwnToDos().then((data) => { this.todos = data; });
                }).catch(() => {
                    this.isSignedIn = false;
                });
            },
            popoverCancel() {
                this.popoverVisible = false;
                this.newToDoHeadline = "";
                this.newToDoDesc = "";
            },
            popoverCreate() {
                todoService.createWithHeadlineAndDescription(this.newToDoHeadline,this.newToDoDesc).then((data) => {
                    this.popoverCancel();
                    this.todos.push(data);
                });
            }, 
            deleteToDo(todo) {
                todoService.deleteOwnToDoById(todo.id)
                      .then(() => todoService.getAllOwnToDos())
                      .then((data) => this.todos = data);
            },
            createItem(todo) {
                todo.items.push({
                    seq : todo.items.length,
                    name : todo.newItem,
                    checked : false,
                });
                todo.newItem = "";
                todoService.updateToDo(todo).then(() => todoService.getAllOwnToDos().then((data) => { this.todos = data; }));
            } 
        },
        mounted() {
            todoService.getAllOwnToDos().then((data) => { this.todos = data; });
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
