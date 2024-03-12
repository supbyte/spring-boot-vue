<head>
<!-- 导入样式 -->
<link rel="stylesheet" href="//unpkg.com/element-plus/dist/index.css" />
<!-- 导入 Vue 3 -->
<!--<script src="//unpkg.com/vue@3"></script>-->
<!--&lt;!&ndash; 导入组件库 &ndash;&gt;-->
<!--<script src="//unpkg.com/element-plus"></script>-->

<!--<script src="//unpkg.com/@element-plus/icons-vue"></script>-->
<!--<script src="//cdn.jsdelivr.net/npm/@element-plus/icons-vue"></script>-->
</head>


<template>
  <div style="text-align: center;margin-left: 25px">
    <div style="margin-top: 150px;">
      <div style="font-size: 25px;">注册新用户</div>
      <div style="font-size: 14px;color: grey;margin-top: 15px">欢迎注册我们的学习平台，请在下方填写相关注册信息</div>
    </div>
    <div style="margin-top: 30px">
      <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
        <el-form-item prop="username">
          <el-input v-model="form.username" type="text" placeholder="用户名">
            <template #prefix>
              <el-icon><UserFilled /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码">
            <template #prefix>
              <el-icon><WalletFilled /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password_repeat">
          <el-input v-model="form.password_repeat" type="password" placeholder="确认密码">
            <template #prefix>
              <el-icon><WalletFilled /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="form.email" type="email" placeholder="电子邮件地址">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-row style="width: 100%;">
            <el-col :span="17">
              <el-input v-model="form.code" :maxlength="6" type="text" placeholder="请输入验证码">
                <template #prefix>
                  <el-icon><Promotion /></el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="7">
              <el-button type="success" @click="validateEmail"
                         :disabled="!isEmailValid || coldTime > 0">
                {{coldTime > 0 ? '请稍后 ' + coldTime + ' 秒' : '获取验证码'}}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>

    </div>
    <div style="margin-top:80px">
      <el-button type="warning" style="width: 170px;" @click="register" plain>立即注册</el-button>
    </div>
    <el-divider>
      <span style="color: grey">已有账号?</span>
    </el-divider>
    <el-button @click="router.push('/')" style="width: 170px;" type="success" plain>立即登录</el-button>
  </div>
</template>

<script setup>
import router from "@/router";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net";

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})

const validateUsername = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入用户名'))
  } else if(!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)){
    callback(new Error('用户名不能包含特殊字符，只能是中文/英文'))
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入密码'))
  } else if (value !== form.password) {
    callback(new Error("两次输入的密码不一致"))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { validator: validateUsername, trigger: ['blur', 'change'] },
    { min: 2, max: 8, message: '用户名的长度必须在2-8个字符之间', trigger: ['blur', 'change'] },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 16, message: '密码的长度必须在6-16个字符之间', trigger: ['blur', 'change'] }
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur', 'change'] },
  ],
  email: [
    { required: true, message: '请输入邮件地址', trigger: 'blur' },
    {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
  ],
  code: [
    { required: true, message: '请输入获取的验证码', trigger: 'blur' },
  ]
}

const formRef = ref()
const isEmailValid = ref(false)
const coldTime = ref(0)

const onValidate = (prop, isValid) => {
  if(prop === 'email')
    isEmailValid.value = isValid
}

const register = () => {
  formRef.value.validate((isValid) => {
    if(isValid) {
      post('/api/auth/register', {
        username: form.username,
        password: form.password,
        email: form.email,
        code: form.code
      }, (message) => {
        ElMessage.success(message)
        router.push("/")
      })
    } else {
      ElMessage.warning('请完整填写注册表单内容！')
    }
  })
}

const validateEmail = () => {
  coldTime.value = 60
  post('/api/auth/valid-register-email', {
    email: form.email
  }, (message) => {
    ElMessage.success(message)
    setInterval(() => coldTime.value--, 1000)
  }, (message) => {
    ElMessage.warning(message)
    coldTime.value = 0
  })
}

</script>

<style scoped>

</style>