<template>
  <div>
    <div class="box1">设计模型的组合验证</div>
    <el-button class="b3" @click="jumpp()">返回主界面</el-button>
    <el-button class="b1" @click="jumpsscm()">构建设计模型</el-button>
    <img src="../assets/TA.png" class="img1" height="300" width="389">
    <el-button class="b2" @click="goTo('/home')">设计模型的验证</el-button>
    <img src="../assets/property.png" class="img2" height="300" width="493">


  </div>
</template>

<script>

export default {
  name: "Main",
  components: {

  },

  data() {
    return {

    }
  },

  methods: {
    goTo(path) {
      this.$router.replace(path);
    },
    jumpp() {
      window.location.href =
          "http://localhost"
    },
    jumpsscm() {
      window.location.href =
          "http://localhost:8090"
    },
  }

}
</script>

<style scoped>

.img1{
  position: absolute;
  left: 180px;
  top: 320px;
}

.img2{
  position: absolute;
  right: 130px;
  top: 320px;
}

.b1{
  position: absolute;
  height: 85px;
  width: 493px;
  left: 130px;
  top: 180px;
  font-size: 30px;
  color: black;
  border-color: black;
}

.b2{
  position: absolute;
  height: 85px;
  width: 493px;
  right: 130px;
  top: 180px;
  font-size: 30px;
  color: black;
  border-color: black;
}

.b3{
  position: absolute;
  left: 30px;
  top: 30px;
  border-style: none;
  height: 60px;
  width: 200px;
  color: white;
  font-size: 22px;
  background-color: #1874CD;
}

.box1{
  font-size: 30px;
  background-color: #1874CD;
  text-align: center;  /*水平居中*/
  line-height: 100px; /*垂直居中 值为heigth的值*/
  color: #fff;
}

</style>