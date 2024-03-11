// import Vue from 'vue'
import Vuex from 'vuex'
// 使用一次插件
// Vue.use(Vuex)

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

// 岁外包录一个Store类的实例
export default new Vuex.Store({
    state,
    mutations,
    actions,
    getters
})