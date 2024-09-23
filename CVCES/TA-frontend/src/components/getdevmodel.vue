<template>
  <div class="p1">
    <p>验证模型列表</p>
  </div>
  <div class="p2">
    <p>模型显示区域</p>
  </div>
  <div>
    <el-container class="adda">
      <el-aside width="250px">
        <el-tree v-bind:data="menus" :props="defaultProps" @node-click="handleNodeClick" :expand-on-click-node="false"
                 :default-expanded-keys="expandedKey" node-key="id" style="height: 570px; width: 300px">
          <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span>{{ node.label }}</span>
<!--            <span>-->

<!--              <a v-if="node.level == 2 && node.name != '声明'" @click="edit(data)"-->
<!--                 style="color:darkseagreen; font-size:16px"> Edit </a>-->
<!--              <a v-if="node.level == 2" style="margin-left: 8px; color:darkseagreen; font-size:16px"-->
<!--                 @click="remove(node, data)"> Delete </a>-->
<!--            </span>-->
          </span>
          </template>
        </el-tree>
      </el-aside>
      <el-main>
        <div id="hide" class="app-content-hide" style="display: none;">
          <div class="edit_container_hide">
            <textarea style="width:100%; height:400px;" v-model="value"></textarea>
          </div>
        </div>
        <div class="app-content" id="show" style="display: block;">
          <div class="flowCanvas" style="width:900px; height: 570px; top: 30px">
            <div ref="container" class="container">
            </div>
            <!-- 节点面板 -->
            <CustomNodePanelState v-if="lf" :lf="lf" style="display:none"></CustomNodePanelState>
          </div>
        </div>
      </el-main>
    </el-container>
    <div>
      <input ref="uploadDes" className="data-upload" type="file" @change="uploadUppaal($event)" style="display: none"/>
      <el-button class="bb" @click="getdesign()" style="display: none">1.获取<br />设计模型</el-button>
      <!--      <input ref="uploadDev" className="data-upload" type="file" @change="uploadUppaalDev($event)" style="display: none"/>-->
      <el-button class="bb1" @click="getdevice()" style="display: none">2.获取<br />设备模型</el-button>
      <el-button class="bb2" @click="showfile()" style="display: none">3.生成<br />系统模型</el-button>
    </div>
  </div>
</template>

<script  lang="ts">
import LogicFlow from "@logicflow/core";
import "@logicflow/core/dist/style/index.css";
import { BpmnAdapter, BpmnElement, BpmnXmlAdapter, Menu, SelectionSelect, Snapshot, } from "@logicflow/extension";
import "@logicflow/extension/lib/style/index.css";
import CustomNodePanelState from "../components/NodePanelState.vue";
import axios from 'axios';


//注册节点
import Sequence from '../registerNode/SequenceModel'
type FileEventTarget = EventTarget & { files: FileList };
function download(filename: string, text: string) {
  var element = document.createElement("a");
  element.setAttribute(
      "href",
      "data:text/plain;charset=utf-8," + encodeURIComponent(text)
  );
  element.setAttribute("download", filename);
  element.style.display = "none";
  document.body.appendChild(element);
  element.click();
  document.body.removeChild(element);
}

export default {
  name: "display2",
  data() {
    return {
      count: 0,
      listRootBean: { declaration: "", jsonRootBean: [] },
      jsonRootBean: [],
      title: "",
      dialogType: "",
      category: { name: "", pid: 1, id: null, level: "" },
      dialogVisible: false,
      expandedKey: [2, 3],
      menus: [],
      lf: null,
      drawer: false,
      currentNode: null,
      content: {},
      editorOption: {},
      dialogTableVisible: false,
      dialogFormVisible: false,
      form: {
        select: '',
        guard: '',
        sync: '',
        update: '',
      },
      value: "",
      formLabelWidth: '120px',
      defaultProps: {
        children: 'children',
        label: 'name'
      },
    };
  },
  components: { CustomNodePanelState },
  mounted() {
    LogicFlow.use(BpmnElement);
    LogicFlow.use(BpmnXmlAdapter);
    BpmnAdapter.shapeConfigMap.set('bpmn:customState1', {
      width: 40,
      height: 40,
    });
    BpmnAdapter.shapeConfigMap.set('bpmn:customState2', {
      width: 40,
      height: 40,
    });
    BpmnAdapter.shapeConfigMap.set('bpmn:customState3', {
      width: 40,
      height: 40,
    });
    BpmnAdapter.shapeConfigMap.set('bpmn:customState4', {
      width: 40,
      height: 40,
    });
    BpmnAdapter.shapeConfigMap.set('bpmn:customState5', {
      width: 50,
      height: 50,
    });
    BpmnAdapter.shapeConfigMap.set('bpmn:customState6', {
      width: 50,
      height: 50,
    });
    BpmnAdapter.shapeConfigMap.set('bpmn:customState7', {
      width: 20,
      height: 20,
    });
    LogicFlow.use(Snapshot);
    LogicFlow.use(Menu);
    LogicFlow.use(SelectionSelect);
    let i = 0;
    //初始化
    this.lf = new LogicFlow({
      container: this.$refs.container,
      grid: false,
      keyboard: {
        enabled: true
      },
      idGenerator(type) {
        return 'id' + (++i);
      }
    });
    // 设置主题
    this.lf.setTheme({
      polyline: {
        stroke: '#000000',
        hoverStroke: '#000000',
        selectedStroke: '#000000',
        outlineColor: '#88f',
        strokeWidth: 2,
      },
      edgeText: {
        background: {
          fill: 'transparent',
        }
      }

    });

    this.registerNode();
    this.init();
    // this.getMenu();

    //绑定事件
    const { eventCenter } = this.lf.graphModel;
    //单击节点
    this.bindEvent(eventCenter);
    //单击边
    // this.bindEventEdge(eventCenter);
    //双击边
    this.bindEventDEdge(eventCenter);
    //锚点连线拖动连线成功
    this.anchorEvent(eventCenter);
    const edgeModel = '';
    this.lf.render();
    this.getdevice();

  },
  computed: {

  },



  methods: {

    uploadUppaal(ev: Event) {
      var _this = this;
      const file = (ev.target as FileEventTarget).files[0];
      console.log(file.webkitRelativePath);
      const reader = new FileReader()
      reader.onload = (event: ProgressEvent<FileReader>) => {
        if (event.target) {
          const xml = event.target.result as string;
          let param = {"data":xml};
          axios.post('http://localhost:8077/sscm/uploaddesignxml',param).then(function(response) {
            _this.lf.renderRawData(response.data)
            _this.getDesMenu();
          }, function(err){
            console.log(err)
          });
        }
      }

      reader.readAsText(file); // you could also read images and other binaries
    },

    getdesign(){
      this.$refs.uploadDes.click();
    },

    uploadUppaalDev(ev: Event) {
      var _this = this;
      const file = (ev.target as FileEventTarget).files[0];
      const reader = new FileReader()
      reader.onload = (event: ProgressEvent<FileReader>) => {
        if (event.target) {
          const xml = event.target.result as string;
          let param = {"data":xml};
          axios.post('http://localhost:8077/sscm/uploaddevicexml',param).then(function(response) {
            _this.lf.renderRawData(response.data)
            _this.getDevMenu();
          }, function(err){
            console.log(err)
          });
        }
      }
      reader.readAsText(file); // you could also read images and other binaries
    },

    getdevice(){
      //this.$refs.uploadDev.click();
      var _this = this;
      axios.post('http://localhost:8077/sscm/uploaddevice',"device").then(function(response) {
        _this.lf.renderRawData(response.data)
        _this.getDevMenu();
        console.log(response.data)
      }, function(err){
        console.log(err)
      });

    },

    showfile() {
      var _this = this;
      axios.post('http://localhost:8077/sscm/loadGsys').then(function (response) {
        console.log(response.data);
        _this.lf.renderRawData(response.data)
        _this.getMenu();
      }, function (err) {
        console.log(err)
      });
    },

    registerNode() {
      let that = this;
      let lf = this.$props.lf as LogicFlow;
      this.lf.register(Sequence);
    },
    getMenu() {
      var _this = this;
      let name = "design";
      axios.post('http://localhost:8077/sscm/catalogsys')
          .then(function (response) {
            _this.menus = JSON.parse(response.data.data);
            console.log(response);
          }, function (err) {
            console.log(err)
          });
    },

    getDesMenu() {
      var _this = this;
      let name = "design";
      axios.post('http://localhost:8077/sscm/catalogdesign')
          .then(function (response) {
            _this.menus = JSON.parse(response.data.data);
            console.log(response);
          }, function (err) {
            console.log(err)
          });
    },

    getDevMenu() {
      var _this = this;
      let name = "design";
      axios.post('http://localhost:8077/sscm/catalog')
          .then(function (response) {
            _this.menus = JSON.parse(response.data.data);
            console.log(response);
          }, function (err) {
            console.log(err)
          });
    },

    handleNodeClick(data: any) {
      var _this = this;
      _this.category.name = data.name;
      axios.post('http://localhost:8077/sscm/subTemp',data.name).then(function (response){
        _this.lf.renderRawData(response.data);
        console.log(data.name)
      });
    },



    exportXml() {
      // 下载菜单
      console.log("下载菜单");
      var _this = this;
      axios.get('http://localhost:8077/sscm/export')
          .then(function (response) {
            download("uppaal.xml", response.data.data);
          }, function (err) {
            console.log(err)
          });
    },
    handleSave(val: any){
      var _this = this;
      // 点击时name不会变
      console.log("接收到的name是:"+_this.category.name)
      let param = {"data":val,"name":_this.category.name};
      axios.post('http://localhost:8077/sscm/save',param).then(function(response) {
        console.log(response.data);
      }, function(err){
        console.log(err)
      });
    },
    uploadXml(ev: Event) {
      var _this = this;
      const file = (ev.target as FileEventTarget).files[0];
      let ax = axios.create();
      let param = new FormData();
      param.append("file", file);
      console.info(file);
      let config = {
        headers: {
          "Content-Type": "multipart/form-data",
        }
      };
      ax.post("http://localhost:8077/sscm/upload", param, config)
          .then(result => {
            console.log(result.data.data);
            _this.menus = JSON.parse(result.data.data);
          })
          .catch(err => {
            console.log(err);
          });
    },
    init() {
      var _this = this;
      axios.get('http://localhost:8077/sscm/init')
          .then(function (response) {
            _this.menus = JSON.parse(response.data.data);
          }, function (err) {
            console.log(err)
          });
    },
    edit(data: any) {
      console.log("输出数据为:", data);
      this.dialogType = "edit";
      this.title = "修改模版"
      this.dialogVisible = true;
      this.category.name = data.name;
      this.category.id = data.id;
    },
    submitData() {
      if (this.dialogType == "add") {
        this.addCategory();
      }
      if (this.dialogType == "edit") {
        this.editCategory();
      }
    },
    editCategory() {
      var _this = this;
      axios.post('http://localhost:8077/sscm/edit', _this.category).then(function (response) {
        console.log(response.data);
        _this.$message({
          type: 'success',
          message: '修改成功!'
        });
        //关闭对话框
        _this.dialogVisible = false;
        //刷新出新的菜单
        _this.getMenu();
        //设置需要默认展开的菜单
        // _this.expandedKey = [3];
      }, function (err) {
        console.log(err)
        _this.$message({
          type: 'error',
          message: '修改失败!'
        });
      });
    },
    addCategory() {
      var _this = this;
      console.log("提交的内容是:", _this.category);
      axios.post('http://localhost:8077/sscm/append', _this.category).then(function (response) {
        console.log(response.data);
        _this.$message({
          type: 'success',
          message: '新建成功!'
        });
        //关闭对话框
        _this.dialogVisible = false;
        //刷新出新的菜单
        _this.getMenu();
        //设置需要默认展开的菜单
        // _this.expandedKey = [3];
      }, function (err) {
        console.log(err)
        _this.$message({
          type: 'error',
          message: '新建失败!'
        });
      });
    },
    append(data: any) {
      console.log("Append", data);
      this.dialogType = "add";
      this.title = "添加模版"
      this.dialogVisible = true;
      this.category.name = "";
      this.category.level = 2;
      this.category.pid = 1;
    },

    remove(node: any, data: any) {
      var _this = this;
      let param = {"name":data.name};
      console.log("Delete", node, data);
      _this.$confirm(`是否删除[${data.name}]模板`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post('http://localhost:8077/sscm/delete', param).then(function (response) {
          console.log(response.data);
          _this.$message({
            type: 'success',
            message: '删除成功!'
          });
          //刷新出新的菜单
          _this.getMenu();
          //设置需要默认展开的菜单
          // _this.expandedKey = [3];
        }, function (err) {
          console.log(err)
          _this.$message({
            type: 'error',
            message: '删除失败!'
          });
        });

      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });

    },

    //清除对话框中的数据
    closeAddDialog() {
      this.dialogFormVisible = false;
      this.form.select = '';
      this.form.guard = '';
      this.form.sync = '';
      this.form.update = '';
    },
    submitAddDialog() {
      const text = this.form.select + '\n' + this.form.guard + '\n' + this.form.sync + '\n' + this.form.update + '\n';
      this.edgeModel.updateText(text);
      console.log(this.form.select);
      console.log(this.form.guard);
      this.edgeModel.setProperties({
        // 自定义properties
        select: this.form.select,
        guard: this.form.guard,
        synchronisation: this.form.sync,
        assignment: this.form.update,
      });
      this.form.select = '';
      this.form.guard = '';
      this.form.sync = '';
      this.form.update = '';
      this.dialogFormVisible = false;
    },
    /*downloadImage() {
      html2canvas(this.$refs.container).then(canvas => {
        // 转成图片，生成图片地址
        this.imgUrl = canvas.toDataURL("image/png"); //可将 canvas 转为 base64 格式
        let filename = `${new Date().getTime()}.png`;
        let file_url = this.dataURLtoFile(this.imgUrl, filename, "image/png"); //将文件转换成file的格式，就可以使用file_url传递给服务端了
        let formData = new FormData()
        formData.append('file', file_url)
        axios.post('http://localhost:8181/oss/upload', formData);
      });
    },*/
    handleRender(val: any) {
      //渲染图原始数据，和render的区别是在使用adapter后，如何还想渲染logicflow格式的数据，可以用此方法。
      this.lf.renderRawData(val)
    },
    //将base64格式转换成file的格式
    dataURLtoFile(base64: string, filename: string, contentType: any) {
      let arr = base64.split(',')  //去掉base64格式图片的头部
      let bstr = atob(arr[1])   //atob()方法将数据解码
      let leng = bstr.length
      let u8arr = new Uint8Array(leng)
      while (leng--) {
        u8arr[leng] = bstr.charCodeAt(leng) //返回指定位置的字符的 Unicode 编码
      }
      return new File([u8arr], filename, { type: contentType })
    },
    bindEvent(eventCenter: { on: (arg0: string, arg1: (args: any) => void) => void; }) {
      eventCenter.on("node:click", (args: any) => {
        console.log("节点单击", args);
        this.drawer = true;
        this.currentNode = args;
      });
    },
    bindEventEdge(eventCenter: { on: (arg0: string, arg1: (args: any) => void) => void; }) {
      eventCenter.on("edge:click", (args: { data: { id: any; }; }) => {
        console.log("边单击", args);
        const id = args.data.id;
        const edgeModel = this.lf.getEdgeModelById(id);
        const properties = edgeModel.getProperties();
        if (!properties.isActived) {
          edgeModel.setProperties({
            // 自定义properties
            isActived: true,
          });
        } else {
          edgeModel.setProperties({
            // 自定义properties
            isActived: false,
          });
        }
      });
    },
    bindEventDEdge(eventCenter: { on: (arg0: string, arg1: (args: any) => void) => void; }) {
      eventCenter.on("edge:dbclick", (args: { data: { id: any; }; }) => {
        console.log("边双击", args);
        const id = args.data.id;
        const edgeModel = this.lf.getEdgeModelById(id);
        const edgeData = edgeModel.getData();
        console.log(edgeData);
        if (edgeData.text !== undefined) {
          const value = edgeData.text.value;
          const valueList = value.split("\n");
          this.form.select = valueList[0] == undefined ? "" : valueList[0];
          this.form.guard = valueList[1] == undefined ? "" : valueList[1];
          this.form.sync = valueList[2] == undefined ? "" : valueList[2];
          this.form.update = valueList[3] == undefined ? "" : valueList[3];
        }
        const properties = edgeModel.getProperties();
        this.dialogFormVisible = true;
        this.edgeModel = edgeModel;
      });
    },
    anchorEvent(eventCenter: { on: (arg0: string, arg1: (args: any) => void) => void; }) {
      eventCenter.on("anchor:drop", (args: { edgeModel: { id: any; sourceNode: { type: string; }; }; }) => {
        const id = args.edgeModel.id;
        const edgeModel = this.lf.getEdgeModelById(id);
        if (args.edgeModel.sourceNode.type == 'bpmn:customState7') {
          edgeModel.setProperties({
            // 自定义properties
            isActived: true,
          });
        };


      })
    },
  },
};
</script>

<style>

.bb{
  position: absolute;
  top: 40px;
  left: 1140px;
  width: 100px;
}

.bb1{
  position: absolute;
  top: 120px;
  left: 1130px;
  width: 100px;
}

.bb2{
  position: absolute;
  top: 200px;
  left: 1130px;
  width: 100px;
}

.app-content-hide {
  display: flex;
  justify-content: space-between;
}

.edit_container_hide {
  width: 100%;
  height: 400px;
  flex-wrap: wrap;
}


body>.el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.app-content {
  display: flex;
  justify-content: space-between;
}

.flowCanvas {
  width: 60%;
  height: 600px;
  margin: 0;
  display: flex;
  float: right;
}

.flowCanvas .data-panel-oss {
  position: absolute;
  bottom: 10px;
  left: 50%;
  height: 20px;
  width: auto;
  padding: 10px;
  background-color: white;
  box-shadow: 0 0 10px 1px rgb(228, 224, 219);
  border-radius: 6px;
  text-align: center;
  z-index: 101;
}

.flowCanvas .container {
  display: flex;
  flex-grow: 1;
  /*铺满剩余空间*/
  border: 3px solid #ababab;
  overflow: hidden;

}

.adda{
  position: absolute;
  top: 30px;
  left: 5px;
}

.p1{
  position: absolute;
  top: -10px;
  left: 20px;
  width: 200px;
}

.p2{
  position: absolute;
  top: -10px;
  left: 270px;
  width: 200px;
}
</style>
