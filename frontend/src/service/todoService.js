import axios from 'axios';
import Vue from 'vue';
import config from '../staging/config';

function doWithAxios(method, subresource = "", data) {
   return Vue.prototype.$Amplify.Auth.currentSession().then(session => 
       axios({
         method,
         url: '/api/v1/todo'+(subresource === "" ? "" : `/${subresource}`),
         data,
         headers : {
            Authorization : `Bearer ${session.idToken.jwtToken}`
         }, 
         baseURL: config.baseURL
       }).then((response) => response.data)
   );
};

function getAllOwnToDos() {
    return doWithAxios('get');
};

function deleteOwnToDoById(id) {
    return doWithAxios('delete', id);
}

function createWithHeadlineAndDescription(headline,shortDesc) {
    return doWithAxios('post', '', { headline, shortDesc});
}

function updateToDo(todo) {
    return doWithAxios('put', todo.id, todo);
}

export default {
    getAllOwnToDos, deleteOwnToDoById, createWithHeadlineAndDescription, updateToDo
};