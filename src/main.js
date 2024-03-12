// 导入 vue 和 createApp 方法
import App from "./App.vue";
import { createApp } from "vue";
// 导入路由表
import router from "./router";
// 导入 vuex
import store from "./store";
// 导入 element-plus
import ElementPlus from "element-plus";
// 导入 element-plus 样式
import "element-plus/dist/index.css";
// 导入 element-plus 图标
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import axios from "axios";

const app = createApp(App);
// 使用router\vuex\element-plus并挂载
app.use(ElementPlus).use(router).use(store).mount("#app");
// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

axios.defaults.baseURL='http://localhost:8080';