<script setup>

</script>

<template>
  <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
    <div style="flex: 1;background-color: black">
      <el-image style="width: 100%;height: 100%" fit="cover"
          src="https://img2.baidu.com/it/u=1659416017,1677546583&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800"/>
    </div>
    <div class="welcome-title">
      <div>希望生活一直都快乐得这么简单</div>
    </div>
    <div class="right-card">
      <router-view/>
    </div>
  </div>
</template>

<style scoped>
.right-card{
  width: 400px;
  z-index: 1;
  background-color: white;
}

.welcome-title{
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>