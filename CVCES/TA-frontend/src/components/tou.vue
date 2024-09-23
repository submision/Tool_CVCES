<template>
  <div class="topbar">
<!--    <img alt="logo" src="../assets/logo1.png" class="img1" height="48.06" width="69.78">-->
    <img src="../assets/logo3.png" style="height: 60px">


<!--    <el-button class="b3" @click="confi()">Configure Project Path</el-button>-->

<!--    <el-button class="b3" @click="EEL()">External entity model libraries</el-button>-->

    <el-button class="b3" @click="showEELs()">External entity model libraries</el-button>

    <el-dialog title="External entity model libraries" v-model="isListVisible" width="500px">
      <div>
        <el-table ref="multipleTable" :data="EELList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="Ename" label="EEML Name" show-overflow-tooltip/>
        </el-table>
      </div>
      <template v-slot:footer>
        <div class="dialog-footer" style="text-align:center;padding-top: 20px;">
          <el-button @click="isListVisible = false">Cancel</el-button>
          <el-button type="primary" @click="isListVisible = false">OK</el-button>
        </div>
      </template>
    </el-dialog>


    <!--    <input ref="uploadpro" className="data-upload" type="file" @change="uploadPro($event)" style="display: none"/>-->
    <input ref="uploadDes" type="file" @change="UploadDes($event)" webkitdirectory style="display: none"/>
    <el-button class="b4" @click="openpro()">Open</el-button>

    <input type='file' ref="saveres" @change="triggerFile($event)" style="display: none" webkitdirectory/>
    <el-button class="b5" @click="save()">Save</el-button>



  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "tou",
  components: {

  },


  data() {
    return {

      cont:'External entity model libraries',

      // 控制列表显示的变量
      isListVisible: false,

      multipleSelection: [],

      EELList: [

      ]

    }
  },

  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },

    showEELs() {
      let da = "EEML";

      var _this=this;
      let sr = "";
      _this.EELList = []

      axios.post('http://localhost:8077/sscm/eeml', da).then(function (response) {
        //console.log(response.data)

        _this.cont="";
        let dat = response.data;
        //this.cont=" ";

        for(var ss in dat){
          let str = dat[ss];
          //console.log(str);
          //_this.cont += str+", ";

          _this.EELList.push({Ename: str})

        }

        console.log(_this.cont)

      });


      this.isListVisible = true;
    },


    // 隐藏文件列表的方法
    hideEELs() {
      this.isListVisible = false;
    },

    EEL() {
      let da = "EEML";

      var _this=this;
      let sr = "";

      axios.post('http://localhost:8077/sscm/eeml', da).then(function (response) {
        //console.log(response.data)

        _this.cont="";
        let dat = response.data;
        //this.cont=" ";

        for(var ss in dat){
          let str = dat[ss];
          //console.log(str);
          _this.cont += str+", ";
        }

        console.log(_this.cont)

      });


      this.$alert( _this.cont , 'External entity model libraries', {
        confirmButtonText: 'Yes',
        // callback: action => {
        //   this.$message({
        //     type: 'info',
        //     message: `action: ${ action }`
        //   });
        // }
      });


    },

    goTo(path) {
      this.$router.replace(path);
    },
    jumpp() {
      window.location.href =
          "http://localhost"
    },

    UploadDes(input) {
      var _this = this;
      let file = input.target.files;
      console.log(file);
      var relativePath = file[0].webkitRelativePath;
      var folderName = relativePath.split("/")[0];
      console.log(folderName);
      let str = {"name":folderName};
      axios.post('http://localhost:8077/sscm/uploadpp',str).then(response => {
        str = response.data;

        this.$message({
          message: "Open success！",
          type: 'success'
        })
      });
    },

    confi() {
      var _this = this;
      this.$prompt('Please enter the configuration project path', 'Saved', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'Cancel',
      }).then(({ value }) => {
        let data={
          "path" : value
        }
        axios.post('http://localhost:8077/sscm/config', data).then(function (response){
          console.log(response.data)

          if(response.data==0){
            _this.$message({
              message: 'Fail！',
              type: 'error'
            });
          }
          else{
            _this.$message({
              message: 'Successes！',
              type: 'success'
            });
          }
        });

      }).catch(() => {
        this.$message({
          type: 'info',
          message: 'Cancel!'
        });
      });
    },

    uploadPro1(input) {
      //支持chrome IE10
      if (window.FileReader) {
        var file = input.target.files[0];
        var reader = new FileReader();
        reader.onload=((event)=>{
          //显示文件
          //this.input_text=event.target.result;
          console.log(event.target.result);
          axios.post('http://localhost:8077/sscm/uploadpro',event.target.result).then(function (response){
            console.log(response)
          });
        })
        console.info(file)
        console.info(reader);
        reader.readAsText(file);

        this.$message({
          message: '项目打开成功！',
          type: 'success'
        });
      }
      else {
        alert("FileReader Not supported by your browser!");
      }
    },

    openpro(){
      this.$refs.uploadDes.click();
    },

    triggerFile(event) {
      var _this = this;
      let file = event.target.files[0];
      console.log(file.webkitRelativePath);
      let name = file.webkitRelativePath;

      axios.post('http://localhost:8077/sscm/saveresult',name).then(function (response){
        console.log(response.data)

        if(response.data==0){
          _this.$message({
            message: '保存失败！',
            type: 'error'
          });
        }
        else{
          _this.$message({
            message: '保存成功！',
            type: 'success'
          });
        }
      });

      event.target.value = '';

    },

    save(){
      this.$refs.saveres.click();
    },

  }

}
</script>

<style scoped>
.topbar{
  background-color: #1874CD;
}

.file-list {
  position: absolute;
  background-color: white;
  border: 1px solid #ccc;
  padding: 10px;
  /* 其他样式根据需要添加 */
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin: 5px 0;
}

.box1{
  font-size: 30px;
  background-color: #1874CD;
  /*text-align: center;  /*水平居中*/
  text-indent: 30px;
  line-height: 60px; /*垂直居中 值为heigth的值*/
  color: #fff;
}

.b2{
  position: absolute;
  left: 650px;
  top: 8px;
  border-style: none;
  height: 60px;
  width: 350px;
  color: white;
  font-size: 22px;
  background-color: #1874CD;
}

.b3{
  position: absolute;
  left: 450px;
  top: 8px;
  border-style: none;
  height: 60px;
  width: 350px;
  color: white;
  font-size: 22px;
  background-color: #1874CD;
}

.b4{
  position: absolute;
  left: 250px;
  top: 8px;
  border-style: none;
  height: 60px;
  width: 100px;
  color: white;
  font-size: 22px;
  background-color: #1874CD;
}

.b5{
  position: absolute;
  left: 350px;
  top: 8px;
  border-style: none;
  height: 60px;
  width: 100px;
  color: white;
  font-size: 22px;
  background-color: #1874CD;
}

</style>
