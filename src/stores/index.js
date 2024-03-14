import {reactive} from 'vue'
import { defineStore } from 'pinia'

// 该文件用于创建Vuex中最核心的store
import Vue from 'vue'
import Vuex from 'vuex'


const state = {
    count:0
}
const actions = {
    // 这里可以书写业务逻辑 参数不能修改state的值
    add({commit}){
        console.log(123);
        commit('ADD')
    }
}
const mutations = {
    ADD(state){
        state.count++
    }
}
const getters = {}

const store = new Vuex.Store({
    actions: actions,
    mutations,
    state,
    getters
})
export default store

export const useStore = defineStore('store', () => {
    const auth = reactive({
        user: null
    })
    return { auth }
})
