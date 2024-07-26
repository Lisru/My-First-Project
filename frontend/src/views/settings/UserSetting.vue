<script setup>

import Card from "@/components/Card.vue";
import {Message, Refresh, Select, User} from "@element-plus/icons-vue";
import {userStore} from "@/store/index.js";
import {computed, reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post,get} from "@/net/index.js"


const store = userStore()
const registerTime = computed(()=>new Date(store.user.registerTime).toLocaleString())
const desc = ref()

const baseFormRef = ref()
const emailFormRef = ref()

const loading = reactive({
  form : true,
  base:false
})

const baseForm = reactive({
  username: '',
  gender:1,
  phone:'',
  qq:'',
  wx:'',
  desc:''
})

const emailForm = reactive({
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

const rules = {
  username:[
    { validator: validateUsername, trigger:['blur','change']},
  ],
  email:[
    {required:true,message:'请输入邮箱',trigger:'blur'},
    {type:'email',message: '请输入合法的电子邮箱',trigger:['blur','change'] }
  ],
  code:[
    {required:true,message:'请输入',trigger:'blur'}
  ]
}
function saveDetails(){
  baseFormRef.value.validate(isValid =>{
    if(isValid){
      loading.base = true
      post('/api/user/save-details',baseForm,()=>{
        ElMessage.success("用户信息保存成功")
        store.user.username = baseForm.username
        desc.value = baseForm.desc
        loading.base = false
      },(message)=>{
        ElMessage.warning(message)
        loading.base = false
      })
    }
  })
}

get('/api/user/details',data=>{
  baseForm.username = store.user.username
  baseForm.gender = data.gender
  baseForm.phone = data.phone
  baseForm.qq = data.qq
  baseForm.wx = data.wx
  baseForm.desc = desc.value = data.desc
  emailForm.email = store.user.email
  loading.form = false
})

const coldTime = ref(0)
const isEmailValid = ref(true)
const onValidate = (prop,isValid)=>{
  if(prop === 'email')
    isEmailValid.value = isValid
}

function sendEmailCode(){
  coldTime.value = 60
  get(`/api/auth/ask-code?email=${emailForm.email}&type=modify`,()=>{
    ElMessage.success(`验证码已成功发送致邮箱：${emailForm.email}，请注意查收`)
    const handle = setInterval(()=>{
      coldTime.value--
      if(coldTime.value === 0){
        clearInterval(handle)
      }
    },1000)
  },(message)=>{
    ElMessage.warning(message)
    coldTime.value = 0
  })
}

function modifyEmail(){
  emailFormRef.value.validate((isValid)=>{
    post('/api/user/modify-email',emailForm,()=>{
      ElMessage.success('邮件修改成功')
      store.user.email = emailForm.email
      emailForm.code = ''
    })
  })
}
</script>

<template>
<div style="display: flex">
  <div class="settings-left">
    <card :icon="User" title="账号信息设置" desc="这里编辑您的个人信息" v-loading="loading.form">
      <el-form ref="baseFormRef" :rules="rules" :model="baseForm" label-position="top" style="margin: 0 10px 10px 10px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="baseForm.username" maxlength="10"/>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-radio-group  v-model="baseForm.gender">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="baseForm.phone" maxlength="11"/>
        </el-form-item>
        <el-form-item label="QQ号" prop="qq">
          <el-input v-model="baseForm.qq" maxlength="13"/>
        </el-form-item>
        <el-form-item label="微信号" prop="wx">
          <el-input v-model="baseForm.wx" maxlength="20"/>
        </el-form-item>
        <el-form-item label="个人简介" prop="desc">
          <el-input type="textarea" rows="6" v-model="baseForm.desc" maxlength="200"/>
        </el-form-item>
        <div>
          <el-button @click="saveDetails" :icon="Select" type="success" :loading="loading.base">保存用户信息</el-button>
        </div>
      </el-form>
    </card>
    <card style="margin-top: 10px" :icon="Message" title="电子邮件设置" desc="您可以在这里改电子邮件地址">
      <el-form ref="emailFormRef" :model="emailForm" label-position="top" style="margin: 0 10px 10px 10px">
        <el-form-item label="电子邮件" prop="email">
          <el-input v-model="emailForm.email"/>
        </el-form-item>
        <el-form-item prop="code">
          <el-row style="width: 100%" :gutter="10">
            <el-col :span="18">
              <el-input v-model="emailForm.code" placeholder="请获取验证码"/>
            </el-col>
            <el-col :span="6">
              <el-button @click="sendEmailCode"
                         :disabled="!onValidate || coldTime > 0"
                         type="success"
                         style="width: 100%" plain>
                {{coldTime > 0 ? `请稍等${coldTime}秒`:'获取验证码'}}
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>
        <div>
          <el-button @click="modifyEmail" :icon="Refresh" type="success">更新电子邮件</el-button>
        </div>
      </el-form>
    </card>
  </div>
  <div class="setting-right">
    <div style="position: sticky;top: 20px">
      <card>
        <div style="text-align: center;padding: 5px 15px 0 15px">
          <el-avatar :size="70" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
          <div style="font-weight: bold">你好，{{store.user.username}}</div>
        </div>
        <el-divider/>
        <div style="font-size: 14px;color: grey">
          {{ desc || "这个人很懒，没有填写任何信息~" }}
        </div>
      </card>
      <card style="margin-top: 10px;font-size: 14px">
        <div>账号注册时间：{{registerTime}}</div>
        <div style="color: grey"> 欢迎加入我们的学习论坛</div>
      </card>
    </div>
  </div>
</div>
</template>

<style scoped>
.settings-left{
  flex: 1;
  margin: 20px;
}

.setting-right{
  width: 300px;
  margin: 20px;
}
</style>