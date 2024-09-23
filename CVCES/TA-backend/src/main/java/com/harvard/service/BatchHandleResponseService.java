package com.harvard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.harvard.config.FileUtil;
import com.harvard.entity.JsonRootBean;
import com.harvard.entity.ListRootBean;
import com.harvard.entity.Tree;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.print.Doc;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/25 上午12:43
 * @Version 1.0
 */
@Service
public class BatchHandleResponseService {
    private static final String modelPath = "C:/软件需求与设计模型的形式化验证工具/SRDVT/backendResources/sscm/model.xml";
//    private static final String modelPath = "src/main/resources/model.xml";
    public static final String TEMPLATE = "Template";
    // 这是导入的tree
    public  List<Tree> all;
    // 这是新建时的tree
    public  List<Tree> newTree;
    // 读导入的xml文件
    public  ListRootBean listRootBeanAll;
    public static int isNUll;

    @Resource
    HandleResponseService handleResponseService;

//    public List<Tree> all;
    public int UUID = 0;

    /*/**
     * 读导入的xml文件
     * @param file
     * @return
     */
    /*public ListRootBean batchParseXMl(MultipartFile file) {
        SAXReader reader = new SAXReader();
        //1、读取XML文件
        Document document = null;
        try {
            document = reader.read(file.getInputStream());
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        ListRootBean listRootBean = new ListRootBean();
        ArrayList<JsonRootBean> listRoot = new ArrayList<>();
        Element declaration = (Element) document.selectSingleNode("/nta/declaration");
        if (declaration != null){
            listRootBean.setDeclaration(declaration.getText());
        }
        List<Element> templateList = document.selectNodes("//template");
        for (Element template : templateList){
            Document newDocument = DocumentHelper.createDocument((Element)template.clone());
            String documentToString = handleResponseService.DocumentToString(newDocument);
            String xml = handleResponseService.parseXml(documentToString);
            JsonRootBean jsonRootBean = JSON.parseObject(xml, new TypeReference<JsonRootBean>() {});
            listRoot.add(jsonRootBean);
        }
        listRootBean.setJsonRootBeans(listRoot);
        listRootBeanAll = listRootBean;
        return listRootBean;
    }*/

    public ListRootBean batchParseXMl(Document document) {
        if (handleResponseService == null) handleResponseService = new HandleResponseService();
        SAXReader reader = new SAXReader();
        ListRootBean listRootBean = new ListRootBean();
        ArrayList<JsonRootBean> listRoot = new ArrayList<>();
        Element declaration = (Element) document.selectSingleNode("/nta/declaration");
        if (declaration != null){
            listRootBean.setDeclaration(declaration.getText());
        }
        List<Element> templateList = document.selectNodes("//template");
        System.out.println(templateList.size());
        for (Element template : templateList){
            System.out.println(template.getName());
            Document newDocument = DocumentHelper.createDocument((Element)template.clone());
            String documentToString = handleResponseService.DocumentToString(newDocument);
            String xml = handleResponseService.parseXml(documentToString);
            JsonRootBean jsonRootBean = JSON.parseObject(xml, new TypeReference<JsonRootBean>() {});
            listRoot.add(jsonRootBean);
        }
        listRootBean.setJsonRootBeans(listRoot);
        return listRootBean;
    }

    public List<Tree> findTree(Document document){
        ListRootBean listRootBean = batchParseXMl(document);
        int UUID = 0;
        Tree tree = new Tree("SystemModel",1,String.valueOf(++UUID),String.valueOf(0),null);
        List<Tree> children2 = new ArrayList<>();
//        children2.add(new Tree("声明",2,String.valueOf(++UUID),tree.getId(),null));
        List<JsonRootBean> jsonRootBeans = listRootBean.getJsonRootBeans();
        for (int i = 0; i < jsonRootBeans.size(); i++) {
            String name = jsonRootBeans.get(i).getName();
            Tree levelTwo = new Tree(name,2,String.valueOf(++UUID),tree.getId(),null);
            children2.add(levelTwo);
//            Tree levelThree = new Tree("声明",3,String.valueOf(++UUID),levelTwo.getId(),null);
//            levelTwo.setChildren(Collections.singletonList(levelThree));
        }
        tree.setChildren(children2);
        return Collections.singletonList(tree);
    }

    public List<Tree> findSysTree(Document document, int count, String req){
        ListRootBean listRootBean = batchParseXMl(document);
        int UUID = 0;
        Tree tree = new Tree(req+ " SubSystem",1,String.valueOf(++UUID),String.valueOf(0),null);
        List<Tree> children2 = new ArrayList<>();
//        children2.add(new Tree("声明",2,String.valueOf(++UUID),tree.getId(),null));
        List<JsonRootBean> jsonRootBeans = listRootBean.getJsonRootBeans();
        for (int i = 0; i < jsonRootBeans.size(); i++) {
            String name = jsonRootBeans.get(i).getName();
            Tree levelTwo = new Tree(name,2,String.valueOf(++UUID),tree.getId(),null);
            children2.add(levelTwo);
//            Tree levelThree = new Tree("声明",3,String.valueOf(++UUID),levelTwo.getId(),null);
//            levelTwo.setChildren(Collections.singletonList(levelThree));
        }
        tree.setChildren(children2);
        return Collections.singletonList(tree);
    }


    /*public List<Tree> findTree(MultipartFile file){
        // bug出自这里，listRootBeanAll原来是static，只能赋值一次
        ListRootBean listRootBean = batchParseXMl(file);
        int UUID = 0;
        Tree tree = new Tree("项目",1,"id"+(++UUID),String.valueOf(0),null);
        List<Tree> children2 = new ArrayList<>();
        children2.add(new Tree("声明",2,"id"+(++UUID),tree.getId(),null));
        List<JsonRootBean> jsonRootBeans = listRootBean.getJsonRootBeans();
        for (int i = 0; i < jsonRootBeans.size(); i++) {
            String name = jsonRootBeans.get(i).getName();
            Tree levelTwo = new Tree(name,2,"id"+(++UUID),tree.getId(),null);
            children2.add(levelTwo);
            Tree levelThree = new Tree("声明",3,"id"+(++UUID),levelTwo.getId(),null);
            levelTwo.setChildren(Collections.singletonList(levelThree));
        }
        tree.setChildren(children2);
        all = Collections.singletonList(tree);
        return Collections.singletonList(tree);
    }*/

    public List<Tree> batchPXml(String specification) throws DocumentException {
        String content = specification;
        content =  content.substring(content.indexOf("<nta>"));
        Document  document = DocumentHelper.parseText(content);
        List<Tree> tree = findTree(document);

        return tree;
    }

    public List<Tree> SysbatchPXml(String specification, int count, String req) throws DocumentException {
        String content = specification;
        content =  content.substring(content.indexOf("<nta>"));
        Document  document = DocumentHelper.parseText(content);
        List<Tree> tree = findSysTree(document, count, req);

        return tree;
    }

    public String drawNode(String content, String name) throws IOException {
        String sp = content;
        String n = name;

        int x = sp.indexOf(n);
        int y = sp.indexOf("</template>",x);
        int x1 = sp.substring(0,x).lastIndexOf("<template>");

        String sss = sp.substring(x1,y+11);

        return sss;
    }

    /**
     * 展示图
     * @param tree
     * @return
     */
    public String batchParseXMl2(Tree tree) {
        Integer level = tree.getLevel();
        String name = tree.getName();
        String context = "";
        // 先调用batchParseXMl时，读取导入文件时listRootBeanAll不为空
        // 不导入文件时，listRootBeanAll为空则读入模版
        List<JsonRootBean> jsonRootBeans = listRootBeanAll.getJsonRootBeans();
        if (level.equals(1)){
            // 不做任何处理
        }else if (level.equals(2) && name.equals("声明")==true){
            context = listRootBeanAll.getDeclaration();
        }else if (level.equals(2) && name.equals("声明")==false){
            for (int i = 0; i < jsonRootBeans.size(); i++) {
                if (jsonRootBeans.get(i).getName().equals(name)){
                    context = JSON.toJSONString(jsonRootBeans.get(i));
                }
            }

        }else if (level.equals(3) && name.equals("声明")==true ){
            String findName = null;
            if (isNUll == 1){
                findName = tree.findNameByPid(newTree.get(0), tree);
            }else{
                findName = tree.findNameByPid(all.get(0), tree);
            }
            System.out.println("findName是"+findName);
            for (int i = 0; i < jsonRootBeans.size(); i++) {
                if (jsonRootBeans.get(i).getName().equals(findName)){
                    context = JSON.toJSONString(jsonRootBeans.get(i).getDeclaration());
                }
            }
        }else {
            return "";
        }
        return context;
    }


    //页面初始化时，会在前端mounted()中调用此方法
    public List<Tree> init() {
        Tree tree = new Tree("Project",1,"id"+(++UUID),String.valueOf(0),null);
        List<Tree> children2 = new ArrayList<>();
        children2.add(new Tree("declaration",2,"id"+(++UUID),tree.getId(),null));
        Tree levelTwo = new Tree(TEMPLATE,2,"id"+(++UUID),tree.getId(),null);
        children2.add(levelTwo);
        Tree levelThree = new Tree("declaration",3,"id"+(++UUID),levelTwo.getId(),null);
        levelTwo.setChildren(Collections.singletonList(levelThree));
        tree.setChildren(children2);
        newTree = Collections.singletonList(tree);
        all = newTree;
        File file = new File(modelPath);
        MultipartFile multipartFile = FileUtil.fileToMultipartFile(file);
        //batchParseXMl(multipartFile);
        isNUll=1;
        return Collections.singletonList(tree);
    }

    /**
     * 点击append
     * @param tree
     * @return
     */
    public List<Tree> append(Tree tree) {
        tree.setId("id"+(++UUID));
        tree.setChildren(Collections.singletonList(new Tree("声明", 3, "id" + (++UUID), tree.getId(), null)));
        // 把数据存进去之后，返回List<Tree>
        List<JsonRootBean> jsonRootBeans = listRootBeanAll.getJsonRootBeans();
        JsonRootBean jsonRootBean = new JsonRootBean();
        jsonRootBean.setName(tree.getName());
        jsonRootBeans.add(jsonRootBean);
        all = newTree;
        List<Tree> children = all.get(0).getChildren();
        children.add(tree);
        System.out.println(all);
        return all;
    }

    public List<Tree> batchParseXml() {
        return all;
    }

    public void save(String param, String name) {
        JSONObject jsonObject = JSON.parseObject(param);
        //0、将json转成类
        JsonRootBean jsonRootBean = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<JsonRootBean>(){});
        jsonRootBean.setName(name);
        List<JsonRootBean> jsonRootBeans = listRootBeanAll.getJsonRootBeans();
        for (JsonRootBean jsonRoot:jsonRootBeans) {
            if(jsonRoot.getName() != null && name.equals(jsonRoot.getName())){
                // 这里写的修改代码
                jsonRootBeans.remove(jsonRoot);
                break;
            }
            if (jsonRoot.getDeclaration() == null){
                jsonRoot.setDeclaration("");
            }
            if (jsonRoot.getNodes() == null){
                jsonRoot.setNodes(new ArrayList<>());
            }
            if (jsonRoot.getEdges() == null){
                jsonRoot.setEdges(new ArrayList<>());
            }
        }
        // 这里写的保存代码
        jsonRootBeans.add(jsonRootBean);

    }

    public String exportXml() {
        System.out.println("处理导出操作");
        StringBuffer sb = new StringBuffer();
        List<JsonRootBean> jsonRootBeans = listRootBeanAll.getJsonRootBeans();
        for (JsonRootBean jsonRoot:jsonRootBeans) {
            String parseJson = handleResponseService.parseJson(JSON.toJSONString(jsonRoot));
            String finalJson = parseJson.substring(parseJson.indexOf("<template>"),parseJson.indexOf("</template>")+11);
            sb.append(finalJson+"\n");
        }
        String finalContext = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<nta> \n" +
                "  <declaration>// Place global declarations here.</declaration>  \n" +
                sb.toString() +
                "\n" +
                "  <system>// Place template instantiations here. Process = Template(); // List one or more processes to be composed into a system. system Process;</system>  \n" +
                "  <queries> \n" +
                "    <query> \n" +
                "      <formula/>  \n" +
                "      <comment/> \n" +
                "    </query> \n" +
                "  </queries> \n" +
                "</nta>";
        return finalContext;
    }

    public void deleteByName(String name) {
    // 分为两步：1、在树中删除 2、在模版中删除
        List<Tree> children = all.get(0).getChildren();
        List<Tree> treeList = children.stream().filter(tree -> name.equals(tree.getName())).collect(Collectors.toList());
        if (treeList != null && treeList.get(0) != null){
            children.remove(treeList.get(0));
        }
        List<JsonRootBean> jsonRootBeans = listRootBeanAll.getJsonRootBeans();
        List<JsonRootBean> rootBeans = jsonRootBeans.stream().filter(listRoot -> name.equals(listRoot.getName())).collect(Collectors.toList());
        if (rootBeans != null && rootBeans.get(0) != null){
            jsonRootBeans.remove(rootBeans.get(0));
        }
    }

    public void editByName(Tree tree) {
        String reName = tree.getName();
        String id = tree.getId();
        // 分为两步：1、在树中修改 2、在模版中修改
        List<Tree> children = all.get(0).getChildren();
        List<Tree> treeList = children.stream().filter(T -> id.equals(T.getId())).collect(Collectors.toList());
        String name = "";
        if (treeList != null && treeList.get(0) != null){
            name = treeList.get(0).getName();
            treeList.get(0).setName(reName);
        }
        List<JsonRootBean> jsonRootBeans = listRootBeanAll.getJsonRootBeans();
        String finalName = name;
        List<JsonRootBean> rootBeans = jsonRootBeans.stream().filter(listRoot -> finalName.equals(listRoot.getName())).collect(Collectors.toList());
        if (rootBeans != null && jsonRootBeans.get(0) != null){
            rootBeans.get(0).setName(reName);
        }
    }
}
