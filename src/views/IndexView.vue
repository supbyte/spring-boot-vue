<head>
<!-- 导入样式 -->
<link rel="stylesheet" href="//unpkg.com/element-plus/dist/index.css" />
<!-- 导入 Vue 3 -->
<script src="//unpkg.com/vue@3"></script>
<!-- 导入组件库 -->
<script src="//unpkg.com/element-plus"></script>

<script src="//unpkg.com/@element-plus/icons-vue"></script>
<script src="//cdn.jsdelivr.net/npm/@element-plus/icons-vue"></script>
</head>

<template>
  <div>
    欢迎<span v-if="store.auth.user!=null">{{store.auth.user.username}}</span>进入到学习平台
  </div>
  <el-button @click="logout" type="danger">退出登录</el-button>
</template>

<script setup>
  import {get} from "@/net";
  import {ElMessage} from "element-plus";
  import router from "@/router";
  import {useStore} from "@/stores";

  const store = useStore()

  const logout = ()=>{
    get('api/auth/logout',(data)=>{
      ElMessage.success(data);
      store.auth.user = null;
      router.push('/');
    })
  }



</script>

<style scoped>

</style>