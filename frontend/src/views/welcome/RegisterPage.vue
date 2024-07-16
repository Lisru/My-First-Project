<script setup>
import {reactive} from "vue";
import {Lock, User, Message} from "@element-plus/icons-vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";
import {get} from "@/net/index.js";

const form = reactive({
  username: '',
  password: '',
  password_repeat: '',
  email: '',
  code: ''
})

const validateUsername = (rule,value,callback)=>{
  if(value === ''){
    callback(new Error('请输入用户名'))
  }else if(!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)){
    callback(new Error('用户名只能是中英文'))
  }else {
    callback()
  }
}

const validatePassword = (rule,value,callback)=>{
  if(value === ''){
    callback(new Error('请再次输入密码'))
  }else if(value !== form.password){
    callback(new Error('两次密码不一致'))
  }else {
    callback()
  }
}
const rule = {
  username:[
    { validator: validateUsername, trigger:['blur','change']},
  ],
  password:[
    {required:true,message:'请输入密码',trigger:'blur'},
    {min:6,max:20,message: '密码长度必须在6-20字符之间',trigger:['blur','change'] }
  ],
  password_repeat:[
    {validator: validatePassword,trigger:['blur','change']}
  ],
  email:[
    {required:true,message:'请输入邮箱',trigger:'blur'},
    {type:'email',message: '请输入合法的电子邮箱',trigger:['blur','change'] }
  ],
  code:[
    {required:true,message:'请输入',trigger:'blur'}
  ]
}

function askCode(){
  if(/^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/.test(form.email)){
    get(`/api/auth/ask-code?email=${form.email}&type=register`,()=>{
      ElMessage.success('验证码已发送到邮箱')
    })
  }else {
    ElMessage.warning('请输入正确的电子邮件')
  }

}
</script>

<template>
<div style="text-align: center;margin: 0 20px">
  <div style="margin-top: 150px">
    <div style="font-size: 25px;font-weight: bold">注册新用户</div>
    <div style="font-size: 14px;color: grey">欢饮注册我们的学习平台</div>
  </div>
  <div style="margin-top: 50px">
    <el-form :model="form" :rules="rule">
      <el-form-item prop="username">
        <el-input v-model="form.username" maxlength="10" type="text" placeholder="用户名">
          <template #prefix>
            <el-icon><User /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input v-model="form.password" maxlength="10" type="password" placeholder="密码">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password_repeat">
        <el-input v-model="form.password_repeat" maxlength="10" type="password" placeholder="重复密码密码">
          <template #prefix>
            <el-icon><Lock /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="email">
        <el-input v-model="form.email" type="text" placeholder="邮箱">
          <template #prefix>
            <el-icon><Message /></el-icon>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-row :gutter="10" style="width: 100%">
          <el-col :span="17">
            <el-input v-model="form.code" maxlength="6" type="text" placeholder="请输入验证码"/>
          </el-col>
          <el-col :span="5">
            <el-button @click="askCode" type="success">获取验证码</el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
  </div>
  <div style="margin-top: 20px">
    <el-button style="width: 270px" type="warning">立即注册</el-button>
  </div>
  <div style="margin-top: 20px">
    <span style="font-size: 14px;color: grey">已有账号?</span>
    <el-link @click="router.push('/')">立即登录</el-link>
  </div>
</div>
</template>

<style scoped>

</style>