import {RectResize} from "@logicflow/extension";
import { h } from '@logicflow/core';
// 提供节点
class CustomState7Node extends RectResize.view { 
  getLabelShape() {
    const { model } = this.props;
    const { x, y, width, height } = model;
    const style = model.getNodeStyle();
    return h(
      "svg",
      {
        x: x - width / 2 + 4,
        y: y - height / 2 + 4,
        width: 50,
        height: 50,
        viewBox: "0 0 80 80"
      },
      h("path", {
        stroke:'rgba(50,50,50,1)',
        strokeWidth: 2,
        fill: '#97b6cf',
        fillOpacity: 1,
        d:
        "M0 10C0 -3.3333333333333335 20 -3.3333333333333335 20 10C20 23.333333333333332 0 23.333333333333332 0 10Z" 
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
      this.getLabelShape()
    ]);
  }
}

// 提供节点的属性
class CustomState7Model extends RectResize.model {
  initNodeData(data) {
    super.initNodeData(data);
    /**
     * 实际就是对于图形SVG的属性
     * http://logic-flow.org/guide/basic/node.html#%E8%AE%A4%E8%AF%86logicflow%E7%9A%84%E5%9F%BA%E7%A1%80%E8%8A%82%E7%82%B9
     */
    this.width = 20;
    this.height = 20;

    this.text.draggable = true; // 允许文本被拖动
    this.text.editable = true; // 允许文本被编辑
    
  }

  getTextStyle() {
    const style = super.getTextStyle();
    style.fontSize = 16;
    style.color = 'purple';
    return style;
  }

  getNodeStyle() {
    const style = super.getNodeStyle();
    style.stroke = 'black';
    // style.strokeDasharray = '3 3';
    return style;
  }
}


export default {
  type: "bpmn:customState7",
  view: CustomState7Node,
  model: CustomState7Model
}
