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
  <div style="text-align: center;margin: 0 20px;">
    <div style="margin-top: 150px;">
      <div style="font-size: 25px;">登录</div>
      <div style="font-size: 14px;color: grey;margin-top: 15px">在进入系统之前请先输入用户名和密码进行登录</div>
    </div>
    <div style="margin-top: 20px;">
      <el-input v-model="form.username" type="text" placeholder="请输入用户名/邮箱">
        <template #prefix>
          <el-icon><UserFilled /></el-icon>
        </template>
      </el-input>
      <el-input v-model="form.password" style="margin-top: 10px;" type="text" placeholder="请输入密码">
        <template #prefix>
          <el-icon><WalletFilled /></el-icon>
        </template>
      </el-input>
    </div>
    <div style="margin-top: 10px">
      <el-row>
        <el-col :span="12" style="text-align: left">
          <el-checkbox v-model="form.remember" label="记住我" />
        </el-col>
        <el-col :span="12" style="text-align: right">
          <el-link>忘记密码？</el-link>
        </el-col>
      </el-row>
    </div>
    <div style="margin-top: 20px">
      <el-button @click="login" style="width: 170px;" type="success" plain>立即登录</el-button>
    </div>
    <el-divider>
      <span style="color: grey">没有账号?</span>
    </el-divider>
    <div style="margin-top: 20px">
      <el-button style="width: 170px;" type="warning" plain>立即注册</el-button>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "@vue/runtime-core";
import {ElMessage} from "element-plus";
import {post} from "@/net";
import router from "@/router";

const form = reactive({
  username: '',
  password: '',
  remember: false
})

const login = ()=>{
  if (!form.username || !form.password){
    ElMessage.warning('请填写用户名和密码')
  }else {
    post('/api/auth/login',{
      username: form.username,
      password: form.password,
      remember: form.remember
    },(data)=>{
        ElMessage.success(data);
        router.push('/index');
    })
  }
}
</script>

<style scoped>

</style>