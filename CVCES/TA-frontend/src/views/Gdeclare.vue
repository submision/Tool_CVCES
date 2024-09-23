<template>
  <div className="Sysdeclare">
<!--    <div class="declar">-->
<!--      <h1>系统声明</h1>-->
<!--      <textarea v-model="input_text" name="" cols="75" rows="32" placeholder="系统声明......"></textarea>-->
<!--      <br><br>-->
<!--      <el-button @click="showfile()">生成系统声明</el-button>-->
<!--    </div>-->
<!--    <div class="init">-->
<!--      <h1>设置变量初始值</h1>-->
<!--      <textarea v-model="value_text" name="" cols="75" rows="32" placeholder="变量......"></textarea>-->
<!--      <br><br>-->
<!--      <el-button @click="senddeclare()">提交</el-button>-->
<!--    </div>-->

    <div class="declar">
      <h1>系统声明</h1>
      <textarea class="ttq1" v-model="value_text" name="" cols="110" rows="25" placeholder="系统声明......"></textarea>
      <br><br>
      <el-button class="blb1" @click="senddeclare()">提交</el-button>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import rightbar from "@/components/rightbar";

export default {
  name: "Gdeclare",

  components:{
    rightbar
  },

  data: function () {
    return {
      input_text: '',
      value_text: ''
    }
  },

  mounted() {
    this.showfile();
    //this.senddeclare();
  },

  methods: {
    showfile() {
      var _this = this;
      axios.post('http://localhost:8077/sscm/Gdeclare').then(response => {
        console.log(response.data);
        this.value_text = response.data;
      }, function (err) {
        console.log(err)
      });
    },

    senddeclare(){

      axios.post('http://localhost:8077/sscm/Setdeclare',this.value_text).then(response => {
        console.log(response.data);
      }, function (err) {
        console.log(err)
      });
      this.$message({
        message: '提交成功',
        type: 'success'
      });
    }
  }
}
</script>

<style scoped>
.init{
  position: absolute;

  left: 650px;
}

.declar{
  position: absolute;
  left: 150px;
  top: 30px;
}

.blb1{
  position: absolute;
  left: 950px;
}

.ttq1{
  font-size: 15px;
}

</style>
