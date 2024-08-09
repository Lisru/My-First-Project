<script setup>

import LightCard from "@/components/LightCard.vue";
import {Calendar, CollectionTag, EditPen, Link} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed, reactive} from "vue";
import {get} from "@/net/index.js"
import {ElMessage} from "element-plus";
import TopicEdit from "@/components/TopicEdit.vue";

const today = computed(()=>{
  const date = new Date()
  return `${date.getFullYear()} 年 ${date.getMonth()+1} 月 ${date.getDate()} 日`
})

const weather = reactive({
  location: {},
  now: {},
  hourly: [],
  success: false
})

navigator.geolocation.getCurrentPosition(position =>{
  const longitude = position.coords.longitude
  const latitude = position.coords.latitude
  get(`/api/forum/weather?longitude=${longitude}&latitude=${latitude}`,data=>{
    Object.assign(weather,data)    //复制数据
    weather.success = true
  })
},error => {
  console.info(error)
  ElMessage.warning('位置信息获取超时')
  get(`/api/forum/weather?longitude=28.23&latitude=112.93`,data=>{
    Object.assign(weather,data)    //复制数据
    weather.success = true
    console.log("data",data)
  })
},{
  timeout: 3000,
  enableHighAccuracy: true
})
</script>

<template>
<div style="display: flex;margin: 20px;gap: 20px">
  <div style="flex: 1">
    <LightCard>
      <div class="create-topic">
        <el-icon><EditPen/></el-icon> 点击发表主题...
      </div>
    </LightCard>
    <LightCard style="margin-top: 10px;height: 30px">

    </LightCard>
    <div style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px">
      <LightCard style="height: 100px" v-for="item in 10">

      </LightCard>
    </div>
  </div>
  <div style="width: 280px">
    <div style="position: sticky;top:20px">
      <LightCard>
        <div style="font-weight: bold">
          <el-icon><CollectionTag/></el-icon>
          论坛公告
        </div>
        <el-divider style="margin: 10px 0"/>
        <div style="font-size: 14px;margin: 10px;color: grey">
          中国共产党已走过百年奋斗历程。我们党立志于中华民族千秋伟业，致力于人类和平与发展崇高事业，责任无比重大，使命无上光荣。全党同志务必不忘初心、牢记使命，务必谦虚谨慎、艰苦奋斗，务必敢于斗争、善于斗争，坚定历史自信，增强历史主动，谱写新时代中国特色社会主义更加绚丽的华章。
        </div>
      </LightCard>
      <LightCard style="margin-top: 10px">
        <div style="font-weight: bold">
          <el-icon><Calendar/></el-icon>
          天气信息
        </div>
        <el-divider/>
        <Weather :data="weather"/>
      </LightCard>
      <LightCard>
        <div class="info-text">
          <div>当前日期</div>
          <div>{{today}}</div>
        </div>
        <div class="info-text">
          <div>当前IP地址</div>
          <div>127.0.0.1</div>
        </div>
      </LightCard>
      <div style="font-size: 14px;margin-top: 10px;color: grey">
        <el-icon><Link/></el-icon>
        友情链接
        <el-divider style="margin: 10px 0"/>
      </div>
      <div style="display: grid;grid-template-columns: repeat(2,1fr);grid-gap: 10px;margin-top: 10px">
        <div class="friend-link">
          <el-image style="height: 100%" src="https://element-plus.org/images/sponsors/mele-banner.png"/>
        </div>
      </div>
    </div>
  </div>
  <topic-edit :show="true"/>
</div>
</template>

<style lang="less " scoped>
.info-text{
  display: flex;
  justify-content: space-between;
  color: grey;
  font-size: 14px;
  margin: 5px 0;
}

.friend-link{
  border-radius: 5px;
  overflow: hidden;
}

.create-topic{
  background-color: #f7f8fa;
  border-radius: 5px;
  height: 40px;
  font-size: 14px;
  line-height: 40px;
  color: grey;
  padding: 0 10px;

  :hover{
    cursor: pointer;
  }
}
</style>