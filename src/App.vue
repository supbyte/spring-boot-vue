
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
<router-view/>
</template>

<script setup>
import {get} from "@/net"
import {ElMessage} from "element-plus";
import axios from "axios";
import { useStore } from '@/stores/index';
import router from "@/router";

const store = useStore()

if(store.auth.user == null) {
  get('/api/user/me', (message) => {
    store.auth.user = message
    router.push('/index')
  }, () => {
    store.auth.user=null
  })
}



</script>

<style scoped>

</style>
