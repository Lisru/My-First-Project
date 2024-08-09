<script setup>
defineProps({
  show: Boolean
})
</script>

<template>
<div>
  <el-drawer :model-value="show"
             :close-on-click-modal="false"
             direction="btt" :size="550">
    <template #header>
      <div>
        <div style="font-weight: bold">发表新的帖子</div>
        <div style="font-size: 13px">欢迎发表帖子</div>
      </div>
    </template>
  </el-drawer>
</div>
</template>

<style scoped>
:deep(.el-drawer){
  width: 800px;
  margin: auto;
  border-radius: 5px;
}
</style>