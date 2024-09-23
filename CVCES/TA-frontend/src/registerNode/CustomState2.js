import {RectResize} from "@logicflow/extension";
import { h } from '@logicflow/core';
// 提供节点
class CustomState2Node extends RectResize.view { 
  getLabelShape() {
    const { model } = this.props;
    const { x, y, width, height } = model;
    const style = model.getNodeStyle();
    return h(
      "svg",
      {
        x: x - width / 2 -10,
        y: y - height / 2 -10,
        width: 50,
        height: 50,
        viewBox: "0 0 100 100"
      },
      h("g",{
        transform: 'translate(25,25)',
      },[
        h("path",{        
        stroke:'rgba(50,50,50,1)',
        strokeWidth: 2,
        fill: '#97b6cf',
        fillOpacity: 1,
        d:
          "M0 35C0 -11.666666666666666 70 -11.666666666666666 70 35C70 81.66666666666667 0 81.66666666666667 0 35Z"
      }),
      h("g",{},[
        h("text",{
          fontFamily:'微软雅黑',
          textAnchor: 'middle',
          fontSize: '13px',
          width:'50px',
          fill:'#323232',
          fontWeight: '400',
          align: 'middle',
          lineHeight: '125%',
          fontStyle: '',
          opacity: 1,
          y: '24.375',
          transform: 'rotate(0)',
        }),
      ]),
      ]),
      h("g",{
        transform: 'translate(35,36.5)',
      },[
        h("path",{
        stroke:'rgba(50,50,50,1)',
        strokeWidth: 2,
        fill: '#97b6cf',
        fillOpacity: 1,
        d:
          "M0 23.5C0 -7.833333333333333 50 -7.833333333333333 50 23.5C50 54.833333333333336 0 54.833333333333336 0 23.5Z"

        }),
        h("g",{},[
          h("text",{
            fontFamily:'微软雅黑',
            textAnchor: 'middle',
            fontSize: '13px',
            width:'30px',
            fill:'#323232',
            fontWeight: '400',
            align: 'middle',
            lineHeight: '125%',
            fontStyle: '',
            opacity: 1,
            y: '12.875',
            transform: 'rotate(0)',
          }),
        ]),
      ]),
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
class CustomState2Model extends RectResize.model {
  initNodeData(data) {
    super.initNodeData(data);
    /**
     * 实际就是对于图形SVG的属性
     * http://logic-flow.org/guide/basic/node.html#%E8%AE%A4%E8%AF%86logicflow%E7%9A%84%E5%9F%BA%E7%A1%80%E8%8A%82%E7%82%B9
     */
    this.width = 40;
    this.height = 40;
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
  type: "bpmn:customState2",
  view: CustomState2Node,
  model: CustomState2Model
}
