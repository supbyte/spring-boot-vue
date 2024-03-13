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
  <div style="width: 98vw;height: 97vh;overflow: hidden;display: flex">
    <div style="flex: 1">
      <el-image style="width: 100%;height: 100%"  src="https://free4kwallpapers.com/uploads/originals/2019/05/18/awesome-himalayas-wallpaper.jpg"/>
    </div>
    <div class="welcome-title">
      <div style="font-size: 30px;font-weight: bold;margin-bottom: 10px">欢迎来到我们的学习平台</div>
      <div>在这里你可以学习如何使用Java，如何搭建网站，并且与Java之父密切交流</div>
    </div>
    <div  style="width: 400px;background-color: white;z-index: 1">
<!--      路由跳转但不自动刷新页面跟缓存keep-alive有关，将<router-view/>放入<keep-alive>中 -->
      <keep-alive exclude="searchResult">
        <router-view  v-slot="{ Component }" :key="$route.fullPath">
          <transition name="el-zoom-in-center" mode="out-in" appear="appear">
            <component :is="Component" style="height: 100%"/>
          </transition>
        </router-view>
      </keep-alive>

    </div>
  </div>
</template>

<script setup>

</script>

<style scoped>
body{
  margin: 0;
}
.welcome-title{
  position: absolute;
  bottom: 30px;
  left: 30px;
  color: white;
  text-shadow: 0 0 10px black;
}
</style>