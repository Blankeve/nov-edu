<template>
  <div class="main">
          <!--标题-->
    <div class="logo">
      <img src="~/assets/img/logo.png" alt="logo">
    </div>
    <h2>Copyright©</h2>
    网站所使用到的图片文字素材均来自网上，仅作学习之用，如有侵权请联系zhu_ian93962@163.com
    <br>
    <br>
    <a href="http://159.75.234.20/" target="_blank">点我打开 前台门户页面</a>
    <br />
    <a href="http://159.75.234.20:3000/" target="_blank"
      >点我打开 后台管理系统</a
    >
  </div>
</template>
<script>
export default {
  layout: "demo",
};
</script>
<style >
h1{
font-size:25px
}
.main{
 text-align: center; /*让div内部文字居中*/
}
a {
  color: blue;
}
</style>