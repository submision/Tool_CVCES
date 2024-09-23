import {RectResize} from "@logicflow/extension";
import { h } from '@logicflow/core';
// 提供节点
class CustomState6Node extends RectResize.view { 
  getLabelShape() {
    const { model } = this.props;
    const { x, y, width, height } = model;
    const style = model.getNodeStyle();
    return h(
      "svg",
      {
        x: x - width / 2 -8,
        y: y - height / 2 -9,
        width: 60,
        height: 60,
        viewBox: "0 0 120 120"
      },
      h("g",{
        transform: 'translate(25,25)',
      },[
        h("path",{
          stroke: 'rgba(50,50,50,1)',
          strokeWidth: '2',
          fillOpacity: 1,
          fill: '#97b6cf',
          d: 'M0 43C0 -14.333333333333334 82 -14.333333333333334 82 43C82 100.33333333333333 0 100.33333333333333 0 43Z',
        }),
        h("g",{},[
          h("text",{
            fontFamily: '微软雅黑',
            textAnchor: 'middle',
            fontSize: '13px',
            width: '62px',
            fill: '#323232',
            fontWeight: '400',
            align: 'middle',
            lineHeight: '125%',
            fontStyle: '',
            opacity: 1,
            y: '32.375',
            transform: 'rotate(0)',
          }),
        ]),
      ]),
      h("g",{
        transform: 'translate(31,33)',
      },[
        h("path",{
          stroke: 'rgba(50,50,50,1)',
          strokeWidth: '2',
          fillOpacity: 1,
          fill: '#97b6cf',
          d: 'M0 35C0 -11.666666666666666 70 -11.666666666666666 70 35C70 81.66666666666667 0 81.66666666666667 0 35Z'
        }),
        h("g",{},[
          h("text",{
            fontFamily: '微软雅黑',
            textAnchor: 'middle',
            fontSize: '64px',
            width: '50px',
            fill: '#323232',
            fontWeight: '400',
            align: '400',
            lineHeight: '125%',
            fontStyle: '',
            opacity: 1,
            y: '-21',
            transform: 'rotate(0)',
          },[
            h("tspan",{
              dy: '80',
              x:'35',
            },[
              h("tspan",{
                style: 'text-decoration:;',
              },['C']),
            ]),
          ]),
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
class CustomState6Model extends RectResize.model {
  initNodeData(data) {
    super.initNodeData(data);
    /**
     * 实际就是对于图形SVG的属性
     * http://logic-flow.org/guide/basic/node.html#%E8%AE%A4%E8%AF%86logicflow%E7%9A%84%E5%9F%BA%E7%A1%80%E8%8A%82%E7%82%B9
     */
    this.width = 50;
    this.height = 50;

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
  type: "bpmn:customState6",
  view: CustomState6Node,
  model: CustomState6Model
}
