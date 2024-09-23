import {PolygonNode, PolygonNodeModel} from "@logicflow/core";


// 提供节点
class CustomPolygonNode extends PolygonNode {

}

// 提供节点的属性
class CustomPolygonModel extends PolygonNodeModel {

  initNodeData(data) {
    super.initNodeData(data);
    this.points = [[40, 0], [80, 40], [40, 80], [0, 40]]
  }

  getNodeStyle() {
    const style = super.getNodeStyle();
    style.stroke = 'black';
    return style;
  }
}

export default {
  type: "CustomPolygon",
  view: CustomPolygonNode,
  model: CustomPolygonModel
}
