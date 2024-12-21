<template>
  <div id="basicLayout">
    <a-layout style="height: 100vh;">
      <a-layout-header class="header">
        <GlobalHeader/>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view></router-view>
      </a-layout-content>
      <a-layout-footer class="footer">
        <a href="47.103.217.83" target="_blank">
          小融的秘密网站
        </a>
      </a-layout-footer>
    </a-layout>

  </div>
</template>

<script setup name="BasicLayout">
import GlobalHeader from '@/components/GlobalHeader.vue'</script>

<style scoped lang="scss">
#basicLayout {
  .content {
    background: linear-gradient(to right, #fefefe, #fff);
    margin: 28px;
  }
  .footer {
    background-color: #efefef;
    width: 90vw;
    margin-left: 5%;
    text-align: center;
  }
}
</style>