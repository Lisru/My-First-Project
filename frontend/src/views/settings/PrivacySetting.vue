<script setup>

import Card from "@/components/Card.vue";
import {Lock, Setting, Switch} from "@element-plus/icons-vue";
import {reactive,ref} from "vue";
import {ElMessage} from "element-plus";
import {post,get} from "@/net/index.js"

const form = reactive({
  password:'',
  new_password:'',
  new_password_repeat:''
})

const validatePassword = (rule,value,callback)=>{
  if(value === ''){
    callback(new Error('请再次输入密码'))
  }else if(value !== form.new_password){
    callback(new Error('两次密码不一致'))
  }else {
    callback()
  }
}

const rules = {
  password:[
    {required:true,message:'请输入原来的密码',trigger:'blur'}
  ],
  new_password:[
    {required:true,message:'请输入新密码',trigger:'blur'},
    {min:6,max:16,message: '密码长度必须在6-16个字符之间',trigger:['blur']}
  ],
  new_password_repeat:[
    {required:true,message:'请在此输入新密码',trigger:'blur'},
    {validator: validatePassword,trigger: ['blur','change']}
  ]
}

const formRef = ref()
const valid = ref(false)
const onValidate = (prop,isValid) => valid.value = isValid

function resetPassword(){
  formRef.value.validate(valid=>{
    if(valid){
      post('/api/user/change-password',form,()=>{
        ElMessage.success("修改密码成功")
        formRef.value.resetFields()
      })
    }
  })
}

const privacy = reactive({
  phone:false,
  wx:false,
  qq:false,
  email:false,
  gender:false,
})

const saving = ref(true)

get('/api/user/privacy',data=>{
  privacy.phone = data.phone
  privacy.wx = data.wx
  privacy.qq = data.qq
  privacy.email = data.email
  privacy.gender = data.gender
  saving.value = false
})

function savePrivacy(type,status){
  saving.value = true
  post('/api/user/save-privacy',{
    type: type,
    status: status
  },()=>{
    ElMessage.success('隐私设置修改成功')
    saving.value = false
  })
}
</script>

<template>
<div style="margin: 20px">
  <div style="margin-top: 20px">
    <card :icon="Setting" title="隐私设置" desc="在这里设置那些内容可以被其他人看到" v-loading="saving">
      <div class="checkbox-list">
        <el-checkbox @change="savePrivacy('phone',privacy.phone)" v-model="privacy.phone">公开手机号</el-checkbox>
        <el-checkbox @change="savePrivacy('email',privacy.email)" v-model="privacy.email">公开电子邮件</el-checkbox>
        <el-checkbox @change="savePrivacy('wx',privacy.wx)" v-model="privacy.wx">公开微信号</el-checkbox>
        <el-checkbox @change="savePrivacy('qq',privacy.qq)" v-model="privacy.qq">公开QQ号</el-checkbox>
        <el-checkbox @change="savePrivacy('gender',privacy.gender)" v-model="privacy.gender">公开性别</el-checkbox>
      </div>
    </card>
    <card style="margin: 20px 0" :icon="Setting" title="修改密码" desc="修改密码请在这里操作">
      <el-form ref="formRef" @validate="onValidate" :rules="rules" :model="form" label-width="150" style="margin: 20px">
        <el-form-item prop="password" label="当前密码">
          <el-input type="password" v-model="form.password" :prefix-icon="Lock" placeholder="当前密码" maxlength="16"/>
        </el-form-item>
        <el-form-item prop="new_password" label="新密码">
          <el-input type="password" v-model="form.new_password" :prefix-icon="Lock" placeholder="新密码" maxlength="16"/>
        </el-form-item>
        <el-form-item prop="new_password_repeat" label="重新输入新密码">
          <el-input type="password" v-model="form.new_password_repeat" :prefix-icon="Lock" placeholder="重新输入新密码" maxlength="16"/>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button @click="resetPassword" :icon="Switch" type="success">立即重置密码</el-button>
      </div>
    </card>
  </div>
</div>
</template>

<style scoped>
.checkbox-list{
  margin: 10px 0 0 10px;
  display: flex;
  flex-direction: column;
}
</style>