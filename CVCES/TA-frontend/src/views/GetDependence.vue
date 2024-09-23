<template>


  <p class="p2">Device-related dependency</p>
  <textarea class="wb2" style="resize:none;" v-model="condep" name="" cols="65" rows="30" placeholder="Device-related dependency......"></textarea>
  <p class="p3">Data-related dependency</p>
  <textarea class="wb1" style="resize:none;" v-model="datadep" name="" cols="65" rows="30" placeholder="Data-related dependency......"></textarea>



</template>

<script>

import axios from 'axios';

export default {
  name: "condependence",
  data() {
    return {
      condep : '',
      datadep : '',

    };
  },

  mounted() {
    this.UploadDenpend();
  },
  computed: {

  },

  methods: {

    UploadDenpend() {

      axios.post('http://localhost:8077/sscm/uploaddep').then(response => {
        let res = response.data;
        this.condep = res["cd"];
        this.datadep = res["dd"];
        this.$message({
          message: "Identifying requirements dependencies successfully!",
          type: 'success'
        })
      });
    },

  },
};
</script>

<style>
.wb2{
  position: absolute;
  top: 180px;
  left: 80px;
}

.wb1{
  position: absolute;
  top:180px;
  left: 630px;
}

.p2{
  position: absolute;
  top: 100px;
  left: 80px;
  width: 500px;
  font-size: 25px;
}

.p3{
  position: absolute;
  top: 100px;
  left: 630px;
  width: 500px;
  font-size: 25px;
}




</style>
