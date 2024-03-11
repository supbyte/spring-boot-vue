import {createRouter, createWebHistory} from "vue-router";

//对应你要跳转的组件
const router = createRouter({
    history: createWebHistory(),
    routes:[
        {
            path:'/',
            name:'welcome',
            component:()=>import('@/views/WelcomeView.vue'),
            children:[
                {
                    path:'',
                    name:'welcome-login',
                    component:()=>import('@/components/welcome/LoginPage.vue')
                }
            ]
        },{
            path:'/index',
            name:'index',
            component:()=>import('@/views/IndexView.vue')
        }
    ]
})

export default router