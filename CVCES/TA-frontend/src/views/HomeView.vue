<template>
  <div class="home">
    <tou></tou>
    <rightbar></rightbar>
  </div>

</template>

<script>
// @ is an alias to /src
import tou from "@/components/tou";
import rightbar from "@/components/rightbar";

export default {
  name: 'HomeView',
  components: {
    tou,
    rightbar
  },

}
</script>

<style scoped>


</style>
