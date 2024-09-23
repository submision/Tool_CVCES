import {RectResize} from "@logicflow/extension";
import { h } from '@logicflow/core';
// import { CustomIncompleteNode, CustomIncompleteModel } from "@logicflow/core";

// 提供节点
class CustomIncompleteNode extends RectResize.view {
  getLabelShape() {
    const { model } = this.props;
    const { x, y, width, height } = model;
    const style = model.getNodeStyle();
    return h(
      "svg",
      {
        x: x - width / 2 + 5,
        y: y - height / 2 + 5,
        width: 25,
        height: 60,
        viewBox: "0 0 1274 1024"
      },
      h("path", {
        // fill: style.stroke,
        d:
          // "M655.807326 287.35973m-223.989415 0a218.879 218.879 0 1 0 447.978829 0 218.879 218.879 0 1 0-447.978829 0ZM1039.955839 895.482975c-0.490184-212.177424-172.287821-384.030443-384.148513-384.030443-211.862739 0-383.660376 171.85302-384.15056 384.030443L1039.955839 895.482975z"
          "m474.91,67l74.19,0l0,890l-74.19,0l0,-890z"
      })
    );
  }
  /**
   * 完全自定义节点外观方法
   */
  getShape() {
    const { model, graphModel } = this.props;
    const { x, y, width, height, radius } = model;
    const style = model.getNodeStyle();
    return h("g", {}, [
      h("rect", {
        ...style,
        x: x - width / 2,
        y: y - height / 2,
        rx: radius,
        ry: radius,
        width,
        height
      }),
      this.getLabelShape()
    ]);
  }
}

// 提供节点的属性
class CustomIncompleteModel extends RectResize.model {
  initNodeData(data) {
    super.initNodeData(data);
    /**
     * 实际就是对于图形SVG的属性
     * http://logic-flow.org/guide/basic/node.html#%E8%AE%A4%E8%AF%86logicflow%E7%9A%84%E5%9F%BA%E7%A1%80%E8%8A%82%E7%82%B9
     */
    this.width = 180;
    this.height = 60;
    // this.radius = 20;
  }

  getNodeStyle() {
    const style = super.getNodeStyle();
    style.stroke = 'black';
    return style;
  }
}

export default {
  type: "CustomIncomplete",
  view: CustomIncompleteNode,
  model: CustomIncompleteModel
}
