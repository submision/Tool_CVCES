<template>


  <p class="sdp">RDD List</p>
<!--  &lt;!&ndash;  <textarea class="wb2" style="resize:none;" v-model="condep" name="" cols="65" rows="30" placeholder="控制依赖关系......"></textarea>&ndash;&gt;-->

  <el-tree :data="treedata"
           :props="defaultProps" @node-click="handleNodeClick" class="td1"></el-tree>

  <div class="demo-image__placeholder">
    <p class="sd">Requirements Dependency Diagram</p>
    <div class="block">
      <el-image :src="require('../assets/RDD/'+imageUrl)" ></el-image>
    </div>
  </div>

</template>

<script>

import axios from 'axios';

export default {
  name: "rdd",
  data() {
    return {
      imageUrl: 'RDD.png',
      condep : '',
      datadep : '',

      treedata: [
      ],

      defaultProps: {
        label: 'label'
      }

    };
  },

  mounted() {
    this.UploadSD();
  },
  computed: {

  },

  methods: {

    handleNodeClick(data) {
      console.log(data.label);

      this.imageUrl = data.label+".png";
    },

    UploadSD() {
      var _this = this;

      axios.post('http://localhost:8077/sscm/getrdd').then(response => {
        let tres = response.data;

        for(var k in tres){

          console.log(tres[k]);
          let obj = {
            label: tres[k]
          };
          _this.treedata.push(obj);

        }

        this.$message({
          message: "Generating Requirements Dependency Diagram Success!",
          type: 'success'
        })
      });
    },

  },
};
</script>

<style>



.td1{
  position: absolute;
  top: 200px;
  left: 80px;
  height: 500px;
  width: 200px;
  transform: scale(130%);
}


.sdp{
  position: absolute;
  top: 50px;
  left: 60px;
  width: 500px;
  font-size: 25px;
}

.sd{
  font-size: 25px;
}

.demo-image__placeholder{
  position: absolute;
  top: 50px;
  left: 500px;
  width: 600px;
}



</style>
