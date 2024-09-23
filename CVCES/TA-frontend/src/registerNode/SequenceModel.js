import { PolylineEdge, PolylineEdgeModel } from "@logicflow/core";

class SequenceModel extends PolylineEdgeModel {

  initEdgeData(data){
    super.initEdgeData(data);
    this.text.draggable = true; // 允许文本被拖动
    this.text.editable = true; // 允许文本被编辑
  } 

  setAttributes() {
    this.offset = 20;
  }
  getEdgeStyle() {
    const style = super.getEdgeStyle();
    const { properties } = this;
    if (properties.isActived) {
      style.strokeDasharray = "4 4";
    }
    //边框为黑色
    style.stroke = '#000000';
    style.strokeWidth = 2;
    // console.log("输出自定义边的style",style);
    return style;
  }
  getTextStyle() {
    const style = super.getTextStyle();
    // style.color = "#3451F1";
    // style.fontSize = 30;
    // style.background.fill = "#F2F131";
    return style;
  }
  getOutlineStyle() {
    const style = super.getOutlineStyle();
    // style.stroke = "red";
    // style.hover.stroke = "red";
    return style;
  }
}

export default {
  type: "bpmn:sequenceFlow",
  view: PolylineEdge,
  model: SequenceModel
};