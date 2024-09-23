<template>
  <div class="tt">
    <el-table class="t"
              :row-style="{height:'20px'}" max-height="250" style="font-size: 15px"
              :data="tableData"
              border
              fit
              highlight-current-row
              height="200"
              @cell-click="chooseItem"
    >
      <el-table-column
          property="Id"
          label="Id"
          width="60">
      </el-table-column>

      <el-table-column
          property="p"
          label="Query"
          width="750">
      </el-table-column>

      <el-table-column
          property="state"
          label="Results"
          width="200">
      </el-table-column>

    </el-table>

    <div class="n">
      <p>Query List</p>
    </div>
    <div class="n1">
      <p>Input query</p>
    </div>
    <div class="n4">
      <p>Query template</p>
    </div>
    <div class="in">
      <textarea v-model="brand" cols="45" rows="5"></textarea>
    </div>
    <div class="n2">
      <p>Verification result</p>
    </div>
    <div class="res">
      <textarea v-model="res" cols="85" rows="10" style="font-size: 23px"></textarea>
    </div>
<!--    <div class="n3">-->
<!--      <p>Localize result</p>-->
<!--    </div>-->
<!--    <div class="dw">-->
<!--      <textarea v-model="dingwei" cols="65" rows="13"></textarea>-->
<!--    </div>-->


    <el-button class="b1b" @click="check()">Verify</el-button>
    <el-button class="b4b" @click="add">Add</el-button>
    <el-button class="b2b" @click="del()">Delete</el-button>
    <el-button class="b3b" @click="localize()" style="display: none">localize</el-button>
  </div>
  <div>
    <el-button class="bbb1" style="text-align: left">
      consistency: A[] not deadlock or E&lt;&gt; not deadlock<br>
<!--      2、状态可达性：E&lt;&gt; 模型名称.模型中状态名称 例：E&lt;&gt; OverTempProtect.ReadTemp<br>-->
<!--      2、安全性：A[] not 模型名称.模型中状态名称 and not 模型名称.模型中状态名称<br>  例：A[] not CommandOperator.ICUOPOff1 and not OverTempProtect.ICUAutoOn-->
    </el-button>
  </div>



</template>

<script>

import axios from "axios";

export default {
  name: 'ExcelTransferTable',
  components: { },

  data() {
    return {
      tableData: [

      ], // 表格里面的数据
      Id: '',
      brand:"",
      res: '',
      dingwei: '',
      num:'',
      proname: this.$route.query.projectname,
      desversion: this.$route.query.designversion,
    }
  },

  mounted() {
    this.getresult()
  },

  methods: {

    //定位结果功能函数
    localize(){
      if (!this.Id) return this.$message.warning('Please select a query to locate first!')
      this.tableData.forEach(item =>{
        if(item.Id == this.Id){
          if(item.state == "通过"){ //性质通过不能进行定位
            this.$message.warning('Query satisfied and cannot be localized!')
          }
          else if(item.state == "不通过") { //性质不通过调用后端定位函数Localize
            this.msg = this.$message({ // 需要一个参数接收这个$message(msg来自data)
              duration: 0,
              type: 'warning',
              message: '定位中....'
            });
            axios.post('http://localhost:8077/sscm/Localize', item.p).then(response => {
              console.log(response.data);
              this.msg.close();  // 这样才能正确关闭
              this.msg = this.$message({
                duration: 1000,
                type: 'success',
                message: 'Localizing success！'
              });
              let ress = response.data;  //返回结果
              if(ress["RES"] == "yes"){
                this.dingwei = this.dingwei + "\n" + ress["dwres"];
              }
              else {
                this.dingwei = this.dingwei + "\n" + item.p + ress["dwres"];
              }
            }, function (err) {

              console.log(err)
            });
          }
          else{
            this.$message.warning('The query has not been verified to locate the cause of the error!')
          }

        }
      })

    },

    //通过链接访问后端查找相应项目及其版本的验证结果
    getresult() {
      var _this = this;
      console.log(_this.desversion);
      console.log(_this.proname);

      axios.get('http://localhost:8077/sscm/getres',{
        params: {
          pname: _this.proname,
          dname: _this.desversion
        }
      }).then(function (response) {
        console.log(response);
        console.log(response.data);

        let tres = response.data;
        _this.res = "所有模型的状态空间为："+tres["statespace"]+"\n\n";
        _this.dingwei = tres["loc"]+"\n";

        for(var k in tres){
          if(k == "statespace" || k == "loc"){
            continue;
          }
          else{
            let str = "";
            if(tres[k].includes("不通过")){
              str = "不通过";
              _this.res += tres[k]+"\n";
            }
            else if(tres[k].includes("通过")){
              str = "通过";
              _this.res += tres[k]+"\n";
            }
            else{
              str = "出错";
            }
            let obj = {
              Id: _this.tableData.length+1,
              p: k,
              state: str,
            };
            _this.tableData.push(obj);
          }
        }

      }, function (err) {
        console.log(err)
      });


    },

    computeState(){
      axios.post('http://localhost:8077/sscm/Statenum').then(response => {
        console.log(response.data);
        //this.res = this.res +","+ response.data +"\n";
        console.log("num:"+response.data);
      }, function (err) {
        console.log(err)
      });
    },

    check(){
      if (!this.Id) return this.$message.warning('Please choose a property to verify first!')
      this.tableData.forEach(item =>{
        if(item.Id == this.Id){
          this.msg = this.$message({ // 需要一个参数接收这个$message(msg来自data)
            duration: 0,
            type: 'warning',
            message: 'Waiting....'
          });
          axios.post('http://localhost:8077/sscm/Checkp', item.p).then(response => {
            console.log(response.data);
            this.msg.close();  // 这样才能正确关闭
            this.msg = this.$message({
              duration: 1000,
              type: 'success',
              message: 'Verify success！'
            });

            let ress = response.data;

            item.state= ress["yes"] +",  "+ress["no"] +",  "+ ress["mei"];
            this.res = this.res+item.p+"Verification results:\n"+ress["allres"]+"\n";

            // if(ress["RES"]=="meiyou"){
            //   this.res = this.res+"\n"+item.p+"Verification results:\n"+ress["RR"];
            //   this.computeState();
            //   item.state = "error";
            // }
            // else if(ress["RES"] == "yes"){
            //   this.res = this.res+"\n"+item.p+"Verification results:\n"+ress["RR"]+"\n"+"State explored: "+ress["states"]+", Run time: "+ress["time"]+"\n"+ress["dec"];
            //   this.computeState();
            //   item.state = "satisfied";
            // }
            // else if(ress["RES"] == "no") {
            //   if (ress["states"] == "null") {
            //     this.res = this.res + "\n" + item.p + "Verification results:\n" + ress["RR"];
            //   }
            //   else{
            //     this.res = this.res + "\n" + item.p + "Verification results:\n" + ress["RR"] + "\n" + "State explored: " + ress["states"] + ", Run time: " + ress["time"] + "\n" + ress["dec"];
            //   }
            //   this.computeState();
            //   item.state = "not satisfied";
            // }
            // else{
            //   this.res = this.res+"\n"+ress["RES"];
            //   item.state = "not satisfied";
            // }
          }, function (err) {

            console.log(err)
          });

        }
      })
    },

    del() {
      // 判断有没有选中一行数据
      if (!this.Id) return this.$message.warning('Please select a query to remove first')
      this.$confirm('Are you sure you want to delete the query of the current selection?', 'Notice', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        //定义一个新的数组
        const tempArr = []
        //遍历table表的数组，如果遍历到的table中的id不等于选中的那一行id，那么将数
        //据push到新的数组里面，也就是说，新的数组里面没有选中的那一行的值
        this.tableData.forEach(item => {

          if (item.Id < this.Id) {
            tempArr.push(item)
          }
          else if(item.Id > this.Id){
            item.Id--;
            tempArr.push(item)
          }
          else{
            if(item.state!=""){
              let param = {"data": item.p};
              axios.post('http://localhost:8077/sscm/delquery', param).then(function(response) {
                console.log(response.data)
              }, function(err){
                console.log(err)
              });
            }
          }
        })
//将新的数组赋值给table表数组，此时选中那一行删除成功
        this.tableData = tempArr
//将选中的一行id置空。
        this.Id = ''

      })
    },


//row是选中这一行的全部数据，如果table的数组是从后端请求回来的，可以将id替换成每一行唯一的那个标志，比如序号等等。
    chooseItem(row) {
      this.Id = row.Id;
      console.log(this.Id);
    },

    add() {
      console.log(this.brand);
      if (this.brand != "") {  //如果输入的值不为空
        let obj = {
          Id:this.tableData.length+1,
          p: this.brand,
          state:"",
        };
        this.tableData.push(obj);
        let param = {"data": this.brand};
        axios.post('http://localhost:8077/sscm/addquery', param).then(function(response) {
          console.log(response.data)
        }, function(err){
          console.log(err)
        });

        this.brand = "";
      } else {
        let obj = {
          Id:this.tableData.length+1,
          p: "",
          state:"",
        };
        this.tableData.push(obj);
      }

    },
  }
}
</script>

<style scoped>
.tt{
  position: absolute;
  top: 10px;
}

.t{
  top: 40px;
  height: 200px;
}

.in{
  position: absolute;
  top: 280px;
  width: 50px;

}

.res{
  position: absolute;
  top: 400px;
  width: 50px;
}

.dw{
  position: absolute;
  top: 400px;
  width: 50px;
  left: 520px;
}

.n{
  position: absolute;
  top: 0px;
}


.bbb1{
  position: absolute;
  top: 290px;
  left: 370px;
  width: 640px;
  height: 80px;
  font-size: 15px;
  -webkit-border-vertical-spacing: 20px;
  padding-top: 10px;
  padding-left: 30px;
  border-color: black;
  color: black;
}

.n1{
  position: absolute;
  top: 240px;
}

.n2{
  position: absolute;
  top: 360px;

}

.n3{
  position: absolute;
  top: 360px;
  left: 520px;
}

.n4{
  position: absolute;
  top: 240px;
  left: 370px;
}

.b1b{
  position: absolute;
  left: 1050px;
  top: 40px;
  width: 100px;
}

.b2b{
  position: absolute;
  left: 1040px;
  top: 90px;
  width: 100px;
}
.b3b{
  position: absolute;
  left: 1040px;
  top: 140px;
  width: 100px;
}
.b4b{
  position: absolute;
  left: 1040px;
  top: 280px;
  width: 100px;
}

.el-table td {
  border-bottom: none;
}
.tableStyle::before{
  width: 0;
}
.el-table{
  border: 1px solid #ccc;
}

.el-button:hover {
  background: white;
  color: black;
  border-color: black;
}



</style>
