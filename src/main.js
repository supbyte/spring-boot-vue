import { createPinia } from 'pinia'
import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index'
import store from './stores/index'
//引入elementplus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//引入elementplus icon
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import axios from 'axios'


axios.defaults.baseURL="http://localhost:8080";

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
// 使用use将文件挂载上去

app.use(router).use(store).use(ElementPlus).use(ElementPlusIconsVue).use(createPinia()).mount('#app')

