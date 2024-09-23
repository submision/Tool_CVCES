import {RectResize} from "@logicflow/extension";
// 提供节点
class CustomRectNode extends RectResize.view {
  // getShape() {
  //   const { model, graphModel } = this.props;
  //   // ...
  // }
}

// 提供节点的属性
class CustomRectModel extends RectResize.model {
  initNodeData(data) {
    super.initNodeData(data);
    /**
     * 实际就是对于图形SVG的属性
     * http://logic-flow.org/guide/basic/node.html#%E8%AE%A4%E8%AF%86logicflow%E7%9A%84%E5%9F%BA%E7%A1%80%E8%8A%82%E7%82%B9
     */
    this.width = 100;
    this.height = 60;
    this.radius = 20;
  }

  getNodeStyle() {
    const style = super.getNodeStyle();
    style.stroke = 'black';
    return style;
  }
}

export default {
  type: "CustomRect",
  view: CustomRectNode,
  model: CustomRectModel
}
