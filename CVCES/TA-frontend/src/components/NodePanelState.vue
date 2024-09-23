<template>
  <div class="node-panel" id="node-panel">
    <div class="node-item" @mousedown="openSelection()">
      <div class="node-item-icon selection"></div>
      <span class="node-label">选区</span>
    </div>
    <div class="node-item" @mousedown="addState1Node()">
      <div class="node-item-icon state1"></div>
      <span class="node-label">locations</span>
    </div>
    <div class="node-item" @mousedown="addState2Node()">
      <div class="node-item-icon state2"></div>
      <span class="node-label">initial</span>
    </div>
    <div class="node-item" @mousedown="addState3Node()">
      <div class="node-item-icon state3"></div>
      <span class="node-label">urgent</span>
    </div>
    <div class="node-item" @mousedown="addState4Node()">
      <div class="node-item-icon state4"></div>
      <span class="node-label">committed</span>
    </div>
    <div class="node-item" @mousedown="addState5Node()">
      <div class="node-item-icon state5"></div>
      <span class="node-label">initial&urgent</span>
    </div>
    <div class="node-item" @mousedown="addState6Node()">
      <div class="node-item-icon state6"></div>
      <span class="node-label">initial&committed</span>
    </div>
    <div class="node-item" @mousedown="addState7Node()">
      <div class="node-item-icon state7"></div>
      <span class="node-label">branch</span>
    </div>
  </div>
</template>
<script lang="ts">
import LogicFlow from "@logicflow/core";
import "@logicflow/core/dist/style/index.css";
import { DndPanel, SelectionSelect } from '@logicflow/extension';
import '@logicflow/extension/lib/style/index.css';
LogicFlow.use(DndPanel);
LogicFlow.use(SelectionSelect);

//注册节点

import CustomState1 from '../registerNode/CustomState1'
import CustomState2 from '../registerNode/CustomState2'
import CustomState3 from '../registerNode/CustomState3'
import CustomState4 from '../registerNode/CustomState4'
import CustomState5 from '../registerNode/CustomState5'
import CustomState6 from '../registerNode/CustomState6'
import CustomState7 from '../registerNode/CustomState7'
import Sequence from '../registerNode/SequenceModel'
import SequenceB from '../registerNode/SequenceModelB'
import SequenceC from "../registerNode/SequenceModelC";

//UI组件
import {ElMessage} from "element-plus";



export default {
  name: "CustomNodePanelState",
  data() {
    return {};
  },
  props: {
    lf: Object,
  },

  mounted() {
    //框选使用
    let lf = this.$props.lf as LogicFlow;
    lf.on("selection:selected", () => {
      lf.updateEditConfig({
        stopMoveGraph: false,
      });
    });
    this.registerNode();
  },
  methods: {
    registerNode() {
      let that = this;
      let lf = this.$props.lf as LogicFlow;
      lf.register(CustomState1);
      lf.register(CustomState2);
      lf.register(CustomState3);
      lf.register(CustomState4);
      lf.register(CustomState5);
      lf.register(CustomState6);
      lf.register(CustomState7);
      lf.register(Sequence);
      lf.register(SequenceB)
      lf.register(SequenceC)

      lf.setDefaultEdgeType("bpmn:sequenceFlow");
      // 定位到画布视口中心到坐标[1000, 1000]处
      lf.focusOn({
        coordinate: {
          x: 100,
          y: 100,
        }
      });

      //注册非业务事件，业务事件需要放在上一层
      lf.on("connection:not-allowed", (data) => {
        ElMessage({
          type: "error",
          message: data.msg,
        });
      });

      lf.on("custom:button-click", (model) => {
        console.log('custom:button-click')
        lf.setProperties(model.id, {
          body: "LogicFlow",
        });
      });
    }, 
    openSelection() {
      (this.$props.lf as LogicFlow).updateEditConfig({
        stopMoveGraph: true,
      });
    },
    addState1Node() {
      (this.$props.lf as LogicFlow).dnd.startDrag({
        type: "bpmn:customState1",
        text: "",
        properties: {start: "状态图1"}, //自定义属性，同时导出的文件节点属性上会生成
      });
    },
    addState2Node() {
      (this.$props.lf as LogicFlow).dnd.startDrag({
        type: "bpmn:customState2",
        text: "",
        properties: {start: "状态图2"}, //自定义属性，同时导出的文件节点属性上会生成
      });
    },
    addState3Node() {
      (this.$props.lf as LogicFlow).dnd.startDrag({
        type: "bpmn:customState3",
        text: "",
        properties: {start: "状态图3"}, //自定义属性，同时导出的文件节点属性上会生成
      });
    },
    addState4Node() {
      (this.$props.lf as LogicFlow).dnd.startDrag({
        type: "bpmn:customState4",
        text: "",
        properties: {start: "状态图4"}, //自定义属性，同时导出的文件节点属性上会生成
      });
    },
    addState5Node() {
      (this.$props.lf as LogicFlow).dnd.startDrag({
        type: "bpmn:customState5",
        text: "",
        properties: {start: "状态图5"}, //自定义属性，同时导出的文件节点属性上会生成
      });
    },
    addState6Node() {
      (this.$props.lf as LogicFlow).dnd.startDrag({
        type: "bpmn:customState6",
        text: "",
        properties: {start: "状态图6"}, //自定义属性，同时导出的文件节点属性上会生成
      });
    },
    addState7Node() {
      (this.$props.lf as LogicFlow).dnd.startDrag({
        type: "bpmn:customState7",
        text: "",
        properties: {start: "状态图7"}, //自定义属性，同时导出的文件节点属性上会生成
      });
    },
  },
};
</script>
<style>
.node-panel {
  position: absolute;
  top: 120px;
  left: 20%;
  width: 90px;
  padding: 10px;
  background-color: white;
  box-shadow: 0 0 10px 1px rgb(228, 224, 219);
  border-radius: 6px;
  text-align: center;
  z-index: 101;
}

.node-item {
  margin-bottom: 10px;
}

.node-item-icon {
  width: 30px;
  height: 30px;
  margin-left: 30px;
  background-size: cover;
}

.node-label {
  font-size: 12px;
  margin-top: 5px;
  user-select: none;
}

.selection {
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABQAAAAUCAYAAAH6ji2bAAAABGdBTUEAALGPC/xhBQAAAOVJREFUOBGtVMENwzAIjKP++2026ETdpv10iy7WFbqFyyW6GBywLCv5gI+Dw2Bluj1znuSjhb99Gkn6QILDY2imo60p8nsnc9bEo3+QJ+AKHfMdZHnl78wyTnyHZD53Zzx73MRSgYvnqgCUHj6gwdck7Zsp1VOrz0Uz8NbKunzAW+Gu4fYW28bUYutYlzSa7B84Fh7d1kjLwhcSdYAYrdkMQVpsBr5XgDGuXwQfQr0y9zwLda+DUYXLaGKdd2ZTtvbolaO87pdo24hP7ov16N0zArH1ur3iwJpXxm+v7oAJNR4JEP8DoAuSFEkYH7cAAAAASUVORK5CYII=) center center no-repeat;
  cursor: grab;
}


.state1 {
  /* background: center center no-repeat; */
  background-image: url(../assets/state1.jpg);
    /*图片从左往右移动的百分比大小，从上往下百分比大小，重复方式 换成数值同样如此，在这里没有调整大小的方法*!*/
  background-position: 0% 100%;
  /*从左到右，从上到下的缩放比例，单位px则为px缩放*/
  background-size: 100% 100%;
  /*填充不完则不进行重复渲染图片*/
  background-repeat: no-repeat;
  cursor: grab;
}

.state2 {
  /* background: center center no-repeat; */
  background-image: url(../assets/state2.jpg);
    /*图片从左往右移动的百分比大小，从上往下百分比大小，重复方式 换成数值同样如此，在这里没有调整大小的方法*!*/
  background-position: 0% 100%;
  /*从左到右，从上到下的缩放比例，单位px则为px缩放*/
  background-size: 100% 100%;
  /*填充不完则不进行重复渲染图片*/
  background-repeat: no-repeat;
  cursor: grab;
}

.state3 {
  /* background: center center no-repeat; */
  background-image: url(../assets/state3.jpg);
    /*图片从左往右移动的百分比大小，从上往下百分比大小，重复方式 换成数值同样如此，在这里没有调整大小的方法*!*/
  background-position: 0% 100%;
  /*从左到右，从上到下的缩放比例，单位px则为px缩放*/
  background-size: 100% 100%;
  /*填充不完则不进行重复渲染图片*/
  background-repeat: no-repeat;
  cursor: grab;
}

.state4 {
  /* background: center center no-repeat; */
  background-image: url(../assets/state4.jpg);
    /*图片从左往右移动的百分比大小，从上往下百分比大小，重复方式 换成数值同样如此，在这里没有调整大小的方法*!*/
  background-position: 0% 100%;
  /*从左到右，从上到下的缩放比例，单位px则为px缩放*/
  background-size: 100% 100%;
  /*填充不完则不进行重复渲染图片*/
  background-repeat: no-repeat;
  cursor: grab;
}

.state5 {
  /* background: center center no-repeat; */
  background-image: url(../assets/state5.jpg);
    /*图片从左往右移动的百分比大小，从上往下百分比大小，重复方式 换成数值同样如此，在这里没有调整大小的方法*!*/
  background-position: 0% 100%;
  /*从左到右，从上到下的缩放比例，单位px则为px缩放*/
  background-size: 100% 100%;
  /*填充不完则不进行重复渲染图片*/
  background-repeat: no-repeat;
  cursor: grab;
}

.state6 {
  /* background: center center no-repeat; */
  background-image: url(../assets/state6.jpg);
    /*图片从左往右移动的百分比大小，从上往下百分比大小，重复方式 换成数值同样如此，在这里没有调整大小的方法*!*/
  background-position: 0% 100%;
  /*从左到右，从上到下的缩放比例，单位px则为px缩放*/
  background-size: 100% 100%;
  /*填充不完则不进行重复渲染图片*/
  background-repeat: no-repeat;
  cursor: grab;
}

.state7 {
  /* background: center center no-repeat; */
  background-image: url(../assets/state7.jpg);
    /*图片从左往右移动的百分比大小，从上往下百分比大小，重复方式 换成数值同样如此，在这里没有调整大小的方法*!*/
  background-position: 0% 100%;
  /*从左到右，从上到下的缩放比例，单位px则为px缩放*/
  background-size: 100% 100%;
  /*填充不完则不进行重复渲染图片*/
  background-repeat: no-repeat;
  cursor: grab;
}


</style>
