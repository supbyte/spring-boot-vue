<template>
  <div style="margin: 30px 20px">
    <el-steps style="max-width: 600px" finish-status="success" :active="active" align-center>
      <el-step title="第一步" description="验证电子邮件" @click="active=0" />
      <el-step title="第二步" description="重新设定密码" />
    </el-steps>
  </div>
  <transition name="el-zoom-in-center" mode="out-in">
    <div style="text-align: center;margin-left: 25px;height: 100%"  v-if="active===0">
      <div style="margin-top: 100px;">
        <div style="font-size: 25px;">重置密码</div>
        <div style="font-size: 14px;color: grey;margin-top: 15px">请输入需要重置密码的电子邮件地址</div>
      </div>
      <div style="margin-top: 30px">
        <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email"  type="email" placeholder="电子邮件地址">
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

      <div style="margin-top: 40px">
        <el-button @click="startReset" style="width: 270px;" type="danger" plain>开始重置密码</el-button>
      </div>
    </div>
  </transition>

  <transition name="el-zoom-in-center" mode="out-in">
    <div style="text-align: center;margin-left: 25px;height: 100%"  v-if="active===1">
      <div style="margin-top: 100px;">
        <div style="font-size: 25px;">重置密码</div>
        <div style="font-size: 14px;color: grey;margin-top: 15px">请填写您的新密码，注意保存</div>
        <div style="margin-top: 30px">
          <el-form :model="form" :rules="rules" @validate="onValidate" ref="formRef">
            <el-form-item prop="password">
              <el-input v-model="form.password" :maxlength="16" type="password" placeholder="新密码">
                <template #prefix>
                  <el-icon><WalletFilled /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password_repeat">
              <el-input v-model="form.password_repeat" :maxlenth="16" type="password" placeholder="确认新密码">
                <template #prefix>
                  <el-icon><WalletFilled /></el-icon>
                </template>
              </el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div style="margin-top: 40px">
        <el-button @click="doReset" style="width: 270px;" type="danger" plain>立即重置密码</el-button>
      </div>
    </div>

  </transition>


</template>

<script setup>
import router from "@/router";
import {reactive,ref} from "vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";
import { nextTick } from 'vue';

const form = reactive({
  email: '',
  code: '',
  password: ''
})

const active = ref(0);

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
  email: [
    { required: true, message: '请输入邮件地址', trigger: 'blur' },
    {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']},
  ],
  code: [
    { required: true, message: '请输入获取的验证码', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 16, message: '密码的长度必须在6-16个字符之间', trigger: ['blur', 'change'] }
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur', 'change'] },
  ]
}

const formRef = ref()
const isEmailValid = ref(false)
const coldTime = ref(0)

const onValidate = (prop, isValid) => {
  if(prop === 'email')
    isEmailValid.value = isValid
}

const validateEmail = () => {
  coldTime.value = 60
  post('/api/auth/valid-reset-email', {
    email: form.email
  }, (message) => {
    ElMessage.success(message)
    coldTime.value = 60
    setInterval(() => coldTime.value--, 1000)
  })
}

const  startReset = () =>{
  formRef.value.validate((isValid) => {
    if(isValid) {
      post('/api/auth/start-reset',{
        email: form.email,
        code: form.code
      },()=>{
        active.value++
      })
    } else {
      ElMessage.warning('请填写邮件地址和验证码！')
    }
  })

}

const doReset = () => {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('/api/auth/do-reset', {
        password: form.password
      }, () => {
        ElMessage.success("密码重置成功，正在返回登录页面");
        active.value++;
        nextTick(() => {
          router.push({
            path: "/",
            query: {
              date: new Date().getTime()
            }
          });
        });
      })
    } else {
      ElMessage.warning('请填写新的密码！')
    }
  })
}


</script>

<style scoped>

</style>