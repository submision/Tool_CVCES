<template>
  <div>
    <div class="all">
      <el-row class="tac">
        <el-col>
          <el-menu default-active="1" class="menu1">

            <el-menu-item class="ziti1" >
              <span slot="title">Progress</span>
            </el-menu-item>

            <div v-show="active == 1">
              <el-menu-item class="ziti" index="1">
                <span slot="title" class="zi">Step1: Upload sequence diagrams</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="2">
                <span slot="title" class="zi">Step2: Requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="3">
                <span slot="title" class="zi">&emsp;2.1: Identifying requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="4">
                <span slot="title" class="zi">&emsp;2.2: Generating requirements dependency diagram</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="5">
                <span slot="title" class="zi">Step3: Generating system architectures</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="6">
                <span slot="title" class="zi">Step4: Assembling and verifying</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="7">
                <span slot="title" class="zi">&emsp;4.1: Assembling system NTA </span>
              </el-menu-item>
              <el-menu-item class="ziti" index="8">
                <span slot="title" class="zi">&emsp;4.2: verifying verification subsystems</span>
              </el-menu-item>
            </div>

            <div v-show="active == 2">
              <el-menu-item class="ziti2" index="1">
                <span slot="title" class="zi">Step1: Upload sequence diagrams</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="2">
                <span slot="title" class="zi">Step2: Requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="3">
                <span slot="title" class="zi">&emsp;2.1: Identifying requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="4">
                <span slot="title" class="zi">&emsp;2.2: Generating requirements dependency diagram</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="5">
                <span slot="title" class="zi">Step3: Generating system architectures</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="6">
                <span slot="title" class="zi">Step4: Assembling and verifying</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="7">
                <span slot="title" class="zi">&emsp;4.1: Assembling system NTA </span>
              </el-menu-item>
              <el-menu-item class="ziti" index="8">
                <span slot="title" class="zi">&emsp;4.2: verifying verification subsystems</span>
              </el-menu-item>
            </div>

            <div v-show="active == 3">
              <el-menu-item class="ziti" index="1">
                <span slot="title" class="zi">Step1: Upload sequence diagrams</span>
              </el-menu-item>
              <el-menu-item class="ziti2" index="2">
                <span slot="title" class="zi">Step2: Requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="3">
                <span slot="title" class="zi">&emsp;2.1: Identifying requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="4">
                <span slot="title" class="zi">&emsp;2.2: Generating requirements dependency diagram</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="5">
                <span slot="title" class="zi">Step3: Generating system architectures</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="6">
                <span slot="title" class="zi">Step4: Assembling and verifying</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="7">
                <span slot="title" class="zi">&emsp;4.1: Assembling system NTA </span>
              </el-menu-item>
              <el-menu-item class="ziti" index="8">
                <span slot="title" class="zi">&emsp;4.2: verifying verification subsystems</span>
              </el-menu-item>
            </div>

            <div v-show="active == 4">
              <el-menu-item class="ziti" index="1">
                <span slot="title" class="zi">Step1: Upload sequence diagrams</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="2">
                <span slot="title" class="zi">Step2: Requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti2" index="3">
                <span slot="title" class="zi">&emsp;2.1: Identifying requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="4">
                <span slot="title" class="zi">&emsp;2.2: Generating requirements dependency diagram</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="5">
                <span slot="title" class="zi">Step3: Generating system architectures</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="6">
                <span slot="title" class="zi">Step4: Assembling and verifying</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="7">
                <span slot="title" class="zi">&emsp;4.1: Assembling system NTA </span>
              </el-menu-item>
              <el-menu-item class="ziti" index="8">
                <span slot="title" class="zi">&emsp;4.2: verifying verification subsystems</span>
              </el-menu-item>
            </div>

            <div v-show="active == 5">
              <el-menu-item class="ziti" index="1">
                <span slot="title" class="zi">Step1: Upload sequence diagrams</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="2">
                <span slot="title" class="zi">Step2: Requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="3">
                <span slot="title" class="zi">&emsp;2.1: Identifying requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti2" index="4">
                <span slot="title" class="zi">&emsp;2.2: Generating requirements dependency diagram</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="5">
                <span slot="title" class="zi">Step3: Generating system architectures</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="6">
                <span slot="title" class="zi">Step4: Assembling and verifying</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="7">
                <span slot="title" class="zi">&emsp;4.1: Assembling system NTA </span>
              </el-menu-item>
              <el-menu-item class="ziti" index="8">
                <span slot="title" class="zi">&emsp;4.2: verifying verification subsystems</span>
              </el-menu-item>
            </div>

            <div v-show="active == 6">
              <el-menu-item class="ziti" index="1">
                <span slot="title" class="zi">Step1: Upload sequence diagrams</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="2">
                <span slot="title" class="zi">Step2: Requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="3">
                <span slot="title" class="zi">&emsp;2.1: Identifying requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="4">
                <span slot="title" class="zi">&emsp;2.2: Generating requirements dependency diagram</span>
              </el-menu-item>
              <el-menu-item class="ziti2" index="5">
                <span slot="title" class="zi">Step3: Generating system architectures</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="6">
                <span slot="title" class="zi">Step4: Assembling and verifying</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="7">
                <span slot="title" class="zi">&emsp;4.1: Assembling system NTA </span>
              </el-menu-item>
              <el-menu-item class="ziti" index="8">
                <span slot="title" class="zi">&emsp;4.2: verifying verification subsystems</span>
              </el-menu-item>
            </div>

            <div v-show="active == 7">
              <el-menu-item class="ziti" index="1">
                <span slot="title" class="zi">Step1: Upload sequence diagrams</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="2">
                <span slot="title" class="zi">Step2: Requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="3">
                <span slot="title" class="zi">&emsp;2.1: Identifying requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="4">
                <span slot="title" class="zi">&emsp;2.2: Generating requirements dependency diagram</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="5">
                <span slot="title" class="zi">Step3: Generating system architectures</span>
              </el-menu-item>
              <el-menu-item class="ziti2" index="6">
                <span slot="title" class="zi">Step4: Assembling and verifying</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="7">
                <span slot="title" class="zi">&emsp;4.1: Assembling system NTA </span>
              </el-menu-item>
              <el-menu-item class="ziti" index="8">
                <span slot="title" class="zi">&emsp;4.2: verifying verification subsystems</span>
              </el-menu-item>
            </div>

            <div v-show="active == 8">
              <el-menu-item class="ziti" index="1">
                <span slot="title" class="zi">Step1: Upload sequence diagrams</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="2">
                <span slot="title" class="zi">Step2: Requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="3">
                <span slot="title" class="zi">&emsp;2.1: Identifying requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="4">
                <span slot="title" class="zi">&emsp;2.2: Generating requirements dependency diagram</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="5">
                <span slot="title" class="zi">Step3: Generating system architectures</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="6">
                <span slot="title" class="zi">Step4: Assembling and verifying</span>
              </el-menu-item>
              <el-menu-item class="ziti2" index="7">
                <span slot="title" class="zi">&emsp;4.1: Assembling system NTA </span>
              </el-menu-item>
              <el-menu-item class="ziti" index="8">
                <span slot="title" class="zi">&emsp;4.2: verifying verification subsystems</span>
              </el-menu-item>
            </div>

            <div v-show="active == 9">
              <el-menu-item class="ziti" index="1">
                <span slot="title" class="zi">Step1: Upload sequence diagrams</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="2">
                <span slot="title" class="zi">Step2: Requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="3">
                <span slot="title" class="zi">&emsp;2.1: Identifying requirements dependencies</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="4">
                <span slot="title" class="zi">&emsp;2.2: Generating requirements dependency diagram</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="5">
                <span slot="title" class="zi">Step3: Generating system architectures</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="6">
                <span slot="title" class="zi">Step4: Assembling and verifying</span>
              </el-menu-item>
              <el-menu-item class="ziti" index="7">
                <span slot="title" class="zi">&emsp;4.1: Assembling system NTA </span>
              </el-menu-item>
              <el-menu-item class="ziti2" index="8">
                <span slot="title" class="zi">&emsp;4.2: verifying verification subsystems</span>
              </el-menu-item>
            </div>

          </el-menu>
        </el-col>
      </el-row>
    </div>

    <el-button class="eb1" @click="pre">Pre</el-button>
    <el-button class="eb2" @click="next">Next</el-button>

    <el-container class="view">
      <router-view></router-view>
    </el-container>
  </div>
</template>

<script>

export default {
  name: "rightbar",

  data() {
    return {
      active:1,
    }
  },

  methods: {
    goTo(path) {
      this.$router.replace(path);
    },
    jumpp() {
      window.location.href =
          "http://localhost:8090"
    },

    next() {
      if (this.active++ > 8) this.active = 1;
      console.log(this.active)
      if(this.active == 1){
        this.goTo('/');
      }
      // else if(this.active == 2){
      //   this.goTo('/Des');
      // }
      else if(this.active == 2){
        this.goTo('/GSD');
      }
      else if(this.active == 3){
        this.goTo('/');
      }
      else if(this.active == 4){
        this.goTo('/Dep');
      }
      else if(this.active == 5){
        this.goTo('/GRDD');
      }
      else if(this.active == 6){
        this.goTo('/GCD');
      }
      else if(this.active == 7){
        this.goTo('/');
      }
      else if(this.active == 8){
        this.goTo('/Sys');
      }
      else if(this.active == 9){
        this.goTo('/CK');
      }
      else{
        this.goTo('/');
      }
    },

    pre() {
      if (this.active-- < 2) this.active = 1;
      console.log(this.active)
      if(this.active == 1){
        this.goTo('/');
      }
      // else if(this.active == 2){
      //   this.goTo('/Des');
      // }
      else if(this.active == 2){
        this.goTo('/GSD');
      }
      else if(this.active == 3){
        this.goTo('/');
      }
      else if(this.active == 4){
        this.goTo('/Dep');
      }
      else if(this.active == 5){
        this.goTo('/GRDD');
      }
      else if(this.active == 6){
        this.goTo('/GCD');
      }
      else if(this.active == 7){
        this.goTo('/');
      }
      else if(this.active == 8){
        this.goTo('/Sys');
      }
      else if(this.active == 9){
        this.goTo('/CK');
      }
      else{
        this.goTo('/');
      }
    },

  }

}
</script>

<style scoped>

.eb1{
  position: absolute;
  top: 75%;
  right: 225px;
}

.eb2{
  position: absolute;
  top: 75%;
  right: 80px;
}

.all{
  position: absolute;
  top:12.2%;
  right: 0;
  /* left:93%; */
  width:22%;
  height:87.8%;
  background: #1874CD;
  float:right;
  overflow-y:auto;
  /* overflow-x:auto; */
  white-space: nowrap;
  color:white;
}


.menu1{
  background-color: #1874CD;
  font-size: 7px;
}

.view{
  position:absolute;
  left: 50px;
}

.ziti{
  background-color: #1874CD;
  color: #fff;
  font-size: 13px;
}

.ziti1{
  color: #fff;
  background: #36648B;
  text-align: center;
  font-size: 13px;
}

.ziti2{
  color: #fff;
  font-size: 13px;
  background-color:grey;
}

.zi{
  color: white;
}

.title{
  position:relative;
  width:100%;
  background: #36648B;
  margin-bottom:1px;
  cursor: pointer;
}
.content{
  position: relative;
  /* width:100%; */
  background:#1874CD;
  cursor: default;
  overflow-x:auto;
  overflow-y:auto;
}

</style>

