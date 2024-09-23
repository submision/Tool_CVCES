package com.harvard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.harvard.entity.*;
import com.harvard.entity.Text;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/21 上午11:29
 * @Version 1.0
 */
@Service
public class HandleResponseService {
    private static final String modelPath = "C:/软件需求与设计模型的形式化验证工具/SRDVT/backendResources/sscm/model.xml";
    //    private static final String modelPath = "src/main/resources/model.xml";
    public String parseJson(String context) {
        JSONObject jsonObject = JSON.parseObject(context);
        //0、将json转成类
        JsonRootBean stu = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<JsonRootBean>(){});
        SAXReader reader = new SAXReader();
        //1、读取XML文件
        Document document = null;
        try {
            document = reader.read(modelPath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //2、获取元素
        Element element = (Element) document.selectSingleNode("//template");
        if(stu.getName() != null){
            Element name = (Element) document.selectSingleNode("//template/name");
            name.setText(stu.getName());
        }
        //画完图形下载时，没有name和declaration
        int sizeNode = stu.getNodes().size();
        int sizeEdge = stu.getEdges().size();
        System.out.println(sizeNode);
        System.out.println(sizeEdge);
        Element location = null;
        Element init = null;
        for (int i = 0; i < sizeNode; i++) {
            String type = stu.getNodes().get(i).getType();
            if (type.equals("bpmn:customState1")){
                location = element.addElement("location");
                location.addAttribute("id",stu.getNodes().get(i).getId());
                location.addAttribute("x", String.valueOf(stu.getNodes().get(i).getX()));
                location.addAttribute("y", String.valueOf(stu.getNodes().get(i).getY()));
            }else if (type.equals("bpmn:customState2")){
                location = element.addElement("location");
                location.addAttribute("id",stu.getNodes().get(i).getId());
                location.addAttribute("x", String.valueOf(stu.getNodes().get(i).getX()));
                location.addAttribute("y", String.valueOf(stu.getNodes().get(i).getY()));
                init = element.addElement("init");
                init.addAttribute("ref",stu.getNodes().get(i).getId());
            }else if (type.equals("bpmn:customState3")){
                location = element.addElement("location");
                location.addAttribute("id",stu.getNodes().get(i).getId());
                location.addAttribute("x", String.valueOf(stu.getNodes().get(i).getX()));
                location.addAttribute("y", String.valueOf(stu.getNodes().get(i).getY()));
                location.addElement("urgent");
            }else if (type.equals("bpmn:customState4")){
                location = element.addElement("location");
                location.addAttribute("id",stu.getNodes().get(i).getId());
                location.addAttribute("x", String.valueOf(stu.getNodes().get(i).getX()));
                location.addAttribute("y", String.valueOf(stu.getNodes().get(i).getY()));
                location.addElement("committed");
            }else if (type.equals("bpmn:customState5")){
                location = element.addElement("location");
                location.addAttribute("id",stu.getNodes().get(i).getId());
                location.addAttribute("x", String.valueOf(stu.getNodes().get(i).getX()));
                location.addAttribute("y", String.valueOf(stu.getNodes().get(i).getY()));
                location.addElement("urgent");
                init = element.addElement("init");
                init.addAttribute("ref",stu.getNodes().get(i).getId());
            }else if (type.equals("bpmn:customState6")){
                location = element.addElement("location");
                location.addAttribute("id",stu.getNodes().get(i).getId());
                location.addAttribute("x", String.valueOf(stu.getNodes().get(i).getX()));
                location.addAttribute("y", String.valueOf(stu.getNodes().get(i).getY()));
                location.addElement("committed");
                init = element.addElement("init");
                init.addAttribute("ref",stu.getNodes().get(i).getId());
            }else if (type.equals("bpmn:customState7")){
                location = element.addElement("branchpoint");
                location.addAttribute("id",stu.getNodes().get(i).getId());
                location.addAttribute("x", String.valueOf(stu.getNodes().get(i).getX()));
                location.addAttribute("y", String.valueOf(stu.getNodes().get(i).getY()));
            } else {
                location = element.addElement("location");
                location.addAttribute("id",stu.getNodes().get(i).getId());
                location.addAttribute("x", String.valueOf(stu.getNodes().get(i).getX()));
                location.addAttribute("y", String.valueOf(stu.getNodes().get(i).getY()));
            }
            if (stu.getNodes(). get(i).getText() != null){
                Element name = location.addElement("name");
                name.addAttribute("x", String.valueOf(stu.getNodes(). get(i).getText().getX()-60));
                name.addAttribute("y", String.valueOf(stu.getNodes(). get(i).getText().getY()-10));
                name.addText(stu.getNodes(). get(i).getText().getValue());
            }

        }
        if (init != null) {
            String ref = init.attributeValue("ref");
            element.remove(init);
            Element init1 = element.addElement("init");
            init1.addAttribute("ref",ref);
        }
        for (int i = 0; i < sizeEdge; i++) {
            Element transition = element.addElement("transition");
            Element source = transition.addElement("source");
            source.addAttribute("ref",stu.getEdges().get(i).getSourceNodeId());
            Element target = transition.addElement("target");
            target.addAttribute("ref",stu.getEdges().get(i).getTargetNodeId());
            Element label = transition.addElement("label");
            Map<String, String> map = selectKind(stu.getEdges().get(i).getProperties());
            for (String key : map.keySet()) {
                label.addAttribute("kind",key);
                label.addAttribute("x", String.valueOf(stu.getEdges().get(i).getText().getX()-60));
                label.addAttribute("y", String.valueOf(stu.getEdges().get(i).getText().getY()-20));
                label.setText(map.get(key));
            }

        }

        String string = DocumentToString(document);
        System.out.println(string);
        return string;
    }

    public  String parseXml(String context){
        int start = context.indexOf("<!");
        int end = context.indexOf(">",start);
        if (start > 0 && end > 0 && end > start){
            context = context.substring(0, start) + context.substring(end+1, context.length());
        }
        //1、读取XML文件
        Document document = null;
        try {
            document = DocumentHelper.parseText(context);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        JsonRootBean jsonRootBean = new JsonRootBean();
        Element nameRootBean = (Element) document.selectSingleNode("//template/name[1]");
        if (nameRootBean != null){
            jsonRootBean.setName(nameRootBean.getText());
        }
        Element declaration = (Element) document.selectSingleNode("//template/declaration[1]");
        if (declaration != null){
            jsonRootBean.setDeclaration(declaration.getText());
        }
        List<Nodes> listNodes = new ArrayList<>();
        //2、获取元素
        List<Element> nodes = document.selectNodes("//location");
        if (nodes != null){
            for (Element node : nodes){
                Nodes nodeStr = new Nodes();
                nodeStr.setId(node.attributeValue("id"));
                nodeStr.setX(Integer.parseInt(node.attributeValue("x")));
                nodeStr.setY(Integer.parseInt(node.attributeValue("y")));
                nodeStr.setType(selectType(node,document));
                Element name = (Element) node.selectSingleNode("name");
                if (name != null){
                    Text text = new Text();
                    text.setX(Integer.parseInt(name.attributeValue("x"))+60);
                    text.setY(Integer.parseInt(name.attributeValue("y"))+20);
                    text.setValue(name.getText());
                    nodeStr.setText(text);
                }
                listNodes.add(nodeStr);
            }
        }

        List<Element> branchPoint = document.selectNodes("//branchpoint");
        if (branchPoint != null){
            for (Element node : branchPoint){
                Nodes nodeStr = new Nodes();
                String id = node.attributeValue("id");
                nodeStr.setId(id);
                nodeStr.setX(Integer.parseInt(node.attributeValue("x")));
                nodeStr.setY(Integer.parseInt(node.attributeValue("y")));
                nodeStr.setType("bpmn:customState7");
                listNodes.add(nodeStr);
            }
        }

        List<Edges> listEdges = new ArrayList<>();
        List<Element> transition = document.selectNodes("//transition");
        if (transition != null){
            for (Element edge : transition){
                Edges edges = new Edges();
                edges.setId(edge.attributeValue("id"));
                edges.setType("bpmn:sequenceFlow");
                Element source = (Element) edge.selectSingleNode("source");
                String ref = source.attributeValue("ref");
                edges.setSourceNodeId(ref);
                Element target = (Element) edge.selectSingleNode("target");
                edges.setTargetNodeId(target.attributeValue("ref"));
                Properties properties = new Properties();
                List<Element> pointList = document.selectNodes("//branchpoint");
                if (pointList != null){
                    for (Element point:pointList){
                        String id = point.attributeValue("id");
                        if (id.equals(ref)) {
                            System.out.println("设置成功");
                            properties.setActived(true);
                            edges.setProperties(properties);
                        }
                    }
                }
                List<Element> labels = edge.selectNodes("label");
                if (labels != null){
                    for (Element label : labels){
                        String kind = label.attributeValue("kind");
                        if (kind != null){
                            if (kind.equals("select")){
                                properties.setSelect(label.getText());
                            }else if (kind.equals("guard")){
                                properties.setGuard(label.getText());
                            }else if(kind.equals("assignment")){
                                properties.setAssignment(label.getText());
                            }else if (kind.equals("synchronisation")){
                                properties.setSynchronisation(label.getText());
                            }
                        }
                        if (properties != null) {
                            edges.setProperties(properties);
                        }
                        Text text = new Text();
                        if (label.attributeValue("x") != null){
                            text.setX(Integer.parseInt(label.attributeValue("x"))+60);
                            text.setY(Integer.parseInt(label.attributeValue("y"))+20);
                            text.setValue(label.getText());
                            edges.setText(text);
                        }
                    }
                }
                listEdges.add(edges);
            }
        }
        jsonRootBean.setNodes(listNodes);
        jsonRootBean.setEdges(listEdges);
        String jsonRoot = JSON.toJSONString(jsonRootBean);
        return jsonRoot;
    }

    /**
     * selectKind
     * @param properties
     * @return
     */
    private static Map<String, String> selectKind(Properties properties){
        if (properties == null) {
            return new HashMap<String, String>();
        }
        Map<String, String> map = new HashMap<String, String>();
        if (properties.getSelect() != null) {
            map.put("select",properties.getSelect());
        }
        if (properties.getGuard() != null) {
            map.put("guard",properties.getGuard());
        }
        if (properties.getSynchronisation() != null) {
            map.put("synchronisation",properties.getSynchronisation());
        }
        if (properties.getAssignment() != null) {
            map.put("assignment",properties.getAssignment());
        }

        return map;

    }

    /**
     * selectType
     * @param node
     * @return
     */
    private static String selectType(Element node,Document document){
        Element init = (Element) document.selectSingleNode("//init");
        String ref = null;
        if (init != null) {
           ref  = init.attributeValue("ref");
        }
        // 找到所有子节点
        List<Element> listElement = node.elements();
        Boolean urgent = false;
        Boolean committed = false;
        for (Element element:listElement){
            urgent = element.getName().equals("urgent");
            committed = element.getName().equals("committed");
        }
        if(node.attributeValue("id").equals(ref) && urgent){
            return "bpmn:customState5";
        }
        if(node.attributeValue("id").equals(ref) && committed){
            return "bpmn:customState6";
        }
        if(node.attributeValue("id").equals(ref)){
            return "bpmn:customState2";
        }
        if (urgent){
            return "bpmn:customState3";
        }
        if (committed){
            return "bpmn:customState4";
        }
        return "bpmn:customState1";
    }


    /**
     * dom4j将xml格式化转换为String
     * @param document
     * @return
     */
    public static String DocumentToString(Document document) {
        //先初始化返回值，防止使用流转换出错时不能返回xml结构
        String strResult = document == null ? "" : document.asXML();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            XMLWriter writer = new XMLWriter(outputStream, format);
            //去除转换后根节点上的多余空行
            format.setNewLineAfterDeclaration(false);
            writer.write(document);
            strResult = new String(outputStream.toByteArray(), "utf-8");
            writer.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
    }


}
