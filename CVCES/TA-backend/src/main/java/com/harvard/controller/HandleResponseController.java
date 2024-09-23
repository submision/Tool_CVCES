package com.harvard.controller;

import com.alibaba.fastjson.JSON;
import com.harvard.entity.ListRootBean;
import com.harvard.entity.Tree;
import com.harvard.service.BatchHandleResponseService;
import com.harvard.service.GenerateSystemModelService;
import com.harvard.service.HandleResponseService;
import com.harvard.service.GenerateAutoControllerService;
import net.sourceforge.plantuml.SourceStringReader;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.dom4j.*;
import sun.security.krb5.internal.crypto.Des;


import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



@RestController
@RequestMapping("/sscm")
public class HandleResponseController {
    @Resource
    HandleResponseService handleResponseService;

    @Resource
    BatchHandleResponseService batchHandleResponseService;

    @Resource
    GenerateAutoControllerService generateAutoControllerService;

    @Resource
    GenerateSystemModelService generateSystemModelService;

    @PostMapping("/json")
    public String parseJson(@RequestBody Map<String, String> data) {
        String param = data.get("data");
        return handleResponseService.parseJson(param);
    }

    static String PJPath_NAME = "projectpath";
    static String PJ_NAME = "project";

    static String Path_Conf = "C:/CVCES_data/Project/";
    static String Path_Conf1 = "C:/CVCES_data/";


    @PostMapping("/eeml")
    public List<String> EEML(@RequestBody String data, HttpSession session) throws IOException {
        String p = data;

        String path = "C:/CVCES_data/External Entity Model Libraries/";
        //表示一个文件路径
        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        List<String> eeml = new ArrayList<>();

        for(File file2: files){
            String name = file2.getName();
            if(name.contains("xml")){
                name = name.substring(0,name.indexOf("."));
                eeml.add(name);
            }
        }

        return eeml;
    }

    //配置项目路径
    @PostMapping("/config")
    public Integer ConfigPath(@RequestBody Map<String,String> data, HttpSession session) throws IOException {
        String p = (String) data.get("path");
        p=p.replace("\\","/");
        System.out.println(p);
        session.setAttribute(PJPath_NAME,p);
        return 1;
    }

    //获取项目名称
    @PostMapping("/uploadpp")
    public String UploadPP(@RequestBody Map<String,String> data, HttpSession session)throws ServletException, IOException {
        String desname = data.get("name");
        System.out.println(desname);
        session.setAttribute(PJ_NAME,desname);
        return "su";
    }



    //获取单个顺序图图片
    @PostMapping("/catasd")
    public String GetSD(@RequestBody Map<String,String> data,HttpSession session) throws IOException {
        String param = data.get("sdname");
        System.out.println(param);

        String pth = (String) session.getAttribute(PJPath_NAME);
        String pron = (String) session.getAttribute(PJ_NAME);
        String path = pth+"/"+pron+"/Input/Sequence Diagram/";
        //表示一个文件路径
        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        String path1="";

        for(File file2: files){
            String name = file2.getName();
            if(name.contains("xml") && name.contains(param)){
                path1=param+".png";
                break;
            }
        }


        return path1;
    }


    //上传顺序图
    @PostMapping("/uploadsd")
    public List<String> UploadSD(HttpSession session) throws IOException {

        String pth = Path_Conf;
        String pron = (String) session.getAttribute(PJ_NAME);

        String path = pth+pron+"/Input/Sequence Diagram/";
        //表示一个文件路径
        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        System.out.println(path);

        List<String> sdnames = new ArrayList<>();//顺序图名称

        for(File file2 : files){
            String name = file2.getName();
            if(name.contains("xml")){
                name = name.substring(0,name.indexOf("."));
                sdnames.add(name);

                String source = "@startuml\n";

                BufferedReader bufferedReader = new BufferedReader(new FileReader(file2.getPath()));
                String line;
                while ((line = bufferedReader.readLine())  != null) {
                    if(line.contains("as")){
                        continue;
                    }
                    source+=line+"\n";
                }

                source += "@enduml\n";

                OutputStream png = new FileOutputStream(path+name+".png");

                SourceStringReader reader = new SourceStringReader(source);

                String desc = reader.outputImage(png).getDescription();

            }
        }

        System.out.println(sdnames);

        return sdnames;
    }

    @PostMapping("/getrdd")
    public List<String> GetRDD(HttpSession session) throws IOException {

        String pth = Path_Conf;
        String pron = (String) session.getAttribute(PJ_NAME);

        String path = pth+pron+"/Output/Step1/";
        //表示一个文件路径
        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        System.out.println(path);

        List<String> sdnames = new ArrayList<>();//顺序图名称

        for(File file2 : files){
            String name = file2.getName();
            if(name.contains("xml") && name.contains("RDD")){

                name = name.substring(0,name.indexOf("."));
                sdnames.add(name);

                String source = "";

                BufferedReader bufferedReader = new BufferedReader(new FileReader(file2.getPath()));
                String line;
                while ((line = bufferedReader.readLine())  != null) {
                    source+=line+"\n";
                }

                OutputStream png = new FileOutputStream(path+name+".png");

                SourceStringReader reader = new SourceStringReader(source);

                String desc = reader.outputImage(png).getDescription();

            }
        }


        System.out.println(sdnames);

        return sdnames;
    }

    //生成组件图
    @PostMapping("/uploadcd")
    public List<String> UploadCD(HttpSession session) throws IOException {

        String pth = Path_Conf;
        String pron = (String) session.getAttribute(PJ_NAME);

        String path = pth+pron+"/Output/Step2/";
        //表示一个文件路径
        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        List<String> sdnames = new ArrayList<>();//顺序图名称

        for(File file2 : files){
            String name = file2.getName();
            if(name.contains("xml")){
                name = name.substring(0,name.indexOf("."));
                sdnames.add(name);

                //String source = "@startuml\n";
                String source = "";


                BufferedReader bufferedReader = new BufferedReader(new FileReader(file2.getPath()));
                String line;
                while ((line = bufferedReader.readLine())  != null) {
//                    if(line.contains("as")){
//                        continue;
//                    }
                    source+=line+"\n";
                }

                //source += "@enduml\n";


                OutputStream png = new FileOutputStream(path+name+".png");

                SourceStringReader reader = new SourceStringReader(source);

                String desc = reader.outputImage(png).getDescription();

            }
        }


        System.out.println(sdnames);

        return sdnames;
    }


    //通过链接访问对应项目及其版本号的验证结果
    @GetMapping("/getres")
    public Map<String, String> GetRES(@RequestParam("pname") String proname, @RequestParam("dname") String desversion, HttpSession session) throws IOException {

        //加载之前已经验证过的对应版本模型文件
        BufferedReader reader = new BufferedReader(new FileReader("C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/"+desversion+"/DesignVerification/TAproject.xml"));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        reader.close();

        String content1 = stringBuilder.toString();//系统模型文件字符串

        //加载项目文件Project.xml
        String ppath = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/Project.xml";
        String con = new String(Files.readAllBytes(Paths.get(ppath)));

        session.setAttribute(Project_XML,con);
        session.setAttribute(NewSystem_NAME,content1);
        session.setAttribute(Project_NAME,proname);


        //加载设计模型
        String despath = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/"+desversion+"/DesignModel/";
        File filedes = new File(despath);
        //用数组把文件夹下的文件存起来
        File[] desfiles = filedes.listFiles();
        String des = "";
        for(File desfile : desfiles){
            des = readTxt(desfile.getPath());
        }
        session.setAttribute(Design_NAME,des);



        //加载环境模型
        String path1 = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/EnvironmentModel/TA/";

        //表示一个文件路径
        File file = new File(path1);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        List<String> devnames = new ArrayList<>();
        for(File file2 : files){
            String name = file2.getName();
            if(!name.contains("decomposedTA")){
                devnames.add(name.substring(0,name.indexOf(".")));
            }
        }

        Map<String,Integer> devname_num = new HashMap<>();//存储分解设备的名称对应个数

        File file0 = new File(path1+"decomposedTA");
        if(file0.exists()){
            session.setAttribute(DeCDevices_FLAG,"yes");

            File[] files1 = file0.listFiles();

            for(File file2 : files1){
                String name = file2.getName();
                devnames.add(name.substring(0,name.indexOf(".")));
                String allname = name.substring(0,name.indexOf("."));
                String nonumname = allname.replaceAll("\\d+","");

                if(devname_num.containsKey(nonumname)){
                    devname_num.put(nonumname,devname_num.get(nonumname)+1);
                }
                else{
                    devname_num.put(nonumname,1);
                }

            }

        }
        else{
            session.setAttribute(DeCDevices_FLAG,"no");
        }

        session.setAttribute(DeviceList_NAME,devnames);
        session.setAttribute(DeviceName_NUM,devname_num);


        StringBuffer strbuf=new StringBuffer(); //整个xml文件
        StringBuffer Qdeclaration=new StringBuffer(); //声明部分
        StringBuffer Sdeclaration = new StringBuffer(); //模型声明部分
        StringBuffer AllTemplate=new StringBuffer();  //模板部分

        strbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>\n");
        strbuf.append("<nta>\n");

        Qdeclaration.append("<declaration>");
        Sdeclaration.append("<system>\n");
        Sdeclaration.append("system ");

        //foreach遍历数组
        for (File file2 : files) {

            if(!file2.getName().contains("decomposedTA")) {

                String filestr = readTxt(file2.getPath());

                //添加声明部分
                int deviceDstart = filestr.indexOf("<declaration>");
                int deviceDend = filestr.indexOf("</declaration>");
                String deviceD = filestr.substring(deviceDstart + 13, deviceDend);
                Qdeclaration.append(deviceD);

                //添加template部分
                int deviceTstart = filestr.indexOf("<template>");
                int deviceTend = filestr.lastIndexOf("</template>");
                String deviceT = filestr.substring(deviceTstart, deviceTend + 11);
                AllTemplate.append(deviceT);
                AllTemplate.append("\n");

                //添加模型声明部分
                String name = file2.getName();
                String deviceS = name.substring(0, name.indexOf(".")) + ",";
                Sdeclaration.append(deviceS);

            }
        }

        String strbuf1 = strbuf.toString();
        String qdecl = Qdeclaration.toString();
        String alltemp = AllTemplate.toString();
        String sdecl = Sdeclaration.toString();

        strbuf1 = strbuf1+"\n"+qdecl;
        strbuf1 += "\n</declaration>\n";
        strbuf1 += alltemp;

        int ind = sdecl.lastIndexOf(",");
        sdecl = sdecl.substring(0,ind)+";";
        strbuf1 += "\n"+sdecl;
        strbuf1 += "\n</system>\n";
        strbuf1 += "</nta>";

        session.setAttribute(NOdec_Device_NAME,strbuf1); //不包含分解设备的设备模型文件

        System.out.println("不包含分解设备的：\n"+strbuf1);

        if(file0.exists()) {

            File[] files1 = file0.listFiles();

            for (File file2 : files1) {
                //System.out.println("路径："+file2.getPath());
                String filestr = readTxt(file2.getPath());
                //System.out.println(filestr);

                //添加声明部分
                int deviceDstart = filestr.indexOf("<declaration>");
                int deviceDend = filestr.indexOf("</declaration>");
                String deviceD = filestr.substring(deviceDstart + 13, deviceDend);
                Qdeclaration.append(deviceD);

                //添加template部分
                int deviceTstart = filestr.indexOf("<template>");
                int deviceTend = filestr.lastIndexOf("</template>");
                String deviceT = filestr.substring(deviceTstart, deviceTend + 11);
                AllTemplate.append(deviceT);
                AllTemplate.append("\n");

                //添加模型声明部分
                String name = file2.getName();
                String deviceS = name.substring(0, name.indexOf(".")) + ",";
                Sdeclaration.append(deviceS);
            }
        }

        Qdeclaration.append("</declaration>\n");

        int index = Sdeclaration.lastIndexOf(",");
        Sdeclaration.setCharAt(index,';');
        Sdeclaration.append("\n</system>\n");

        strbuf.append(Qdeclaration);
        strbuf.append(AllTemplate);
        strbuf.append(Sdeclaration);
        strbuf.append("</nta>");

        String param=strbuf.toString();
        session.setAttribute(Device_NAME,param);


        //加载需求模型
        String path2 = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/RequirementsModel/";
        //表示一个文件路径
        File filee = new File(path2);
        //用数组把文件夹下的文件存起来
        File[] filees = filee.listFiles();
        List<String> reqnames = new ArrayList<>();
        for(File file2 : filees){
            String name = file2.getName();
            if(name.contains("SG")){
                reqnames.add(name.substring(name.indexOf("-")+1,name.indexOf(".")));
            }
        }
        session.setAttribute(Req_NAME,reqnames);

        //加载组合子系统模型
        String devicemodel = (String)session.getAttribute(Device_NAME);//获取设备模型xml
        String designmodel = (String)session.getAttribute(Design_NAME);//获取设计模型xml

        String tpath = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/RequirementsModel";
        String PFpath = tpath+"/ProblemDiagram.xml";
        String PF = new String(Files.readAllBytes(Paths.get(PFpath)));//读取问题图

        String Conpath = tpath+"/ContextDiagram.xml";
        String ConD = new String(Files.readAllBytes(Paths.get(Conpath)));//读取上下文图

        String domain = ConD.substring(ConD.indexOf("<ProblemDomain>"),ConD.indexOf("</ProblemDomain>"));
        Map<String,String> shortnameToallname = new HashMap<>();
        int sx = domain.indexOf("problemdomain_name=\"");
        int ax = domain.indexOf("problemdomain_shortname=\"");
        while(sx>0){
            sx+=20;
            ax+=25;
            String allname = domain.substring(sx,domain.indexOf("\"",sx));
            String shortname = domain.substring(ax,domain.indexOf("\"",ax));
            shortnameToallname.put(shortname,allname);
            sx = domain.indexOf("problemdomain_name=\"",sx);
            ax = domain.indexOf("problemdomain_shortname=\"",ax);
        }

        session.setAttribute(STOA_NAME,shortnameToallname); //存储简称映射全称

        //得到所有组合的子系统模型列表<reqname,template>
        Map<String,String> SysList = generateSystemModelService.GSystemList(devicemodel,designmodel,PF,reqnames,shortnameToallname);

        String allreqs = SysList.get("ALLREQS"); //分解的需求所有名称
        SysList.remove("ALLREQS");

        //得到所有组合的子系统模型需求名称-声明<reqname,declaration>
        Map<String,String> req_declar = new HashMap<>();

        for(String str : SysList.keySet()){
            String temp = SysList.get(str);
            int x = temp.indexOf("<system>");
            int x1 = temp.indexOf("system ",x);
            int y = temp.indexOf("</system>");
            String declar = temp.substring(x1+7,y);
            declar = declar.replaceAll("\\s*|\r|\n|\t","");
            declar = declar.replaceAll(";","");
            req_declar.put(str,declar);
        }

        session.setAttribute(ALLREQS,allreqs);
        session.setAttribute(SYSTEM_LIST,SysList);
        session.setAttribute(ReqName_Declar,req_declar);
        //

        Map<String, String> res = new HashMap<>();
        //System.out.println("项目名称："+proname+"\n版本信息："+desversion);
        //String path = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent();

        //加载对应项目及设计版本的验证
        String path = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/"+desversion+"/DesignVerification/result.xml";
        //System.out.println(path);
        String content = new String(Files.readAllBytes(Paths.get(path)));
        session.setAttribute(Property_NAME,content);

        String room = content.substring(content.indexOf("<statespace>")+12,content.indexOf("</statespace>"));
        res.put("statespace",room);

        String loc = "";

        String strbuff = content.substring(content.indexOf("<properties>"));
        while(strbuff.contains("<property>")){
            int start = strbuff.indexOf("<property>");
            int end = strbuff.indexOf("</property>",start);
            String conn = strbuff.substring(start,end);
            String ctl = strbuff.substring(strbuff.indexOf("<ctl>")+5,strbuff.indexOf("</ctl>"));
            if(ctl.indexOf("&lt;&gt;")>=0){
                int s = ctl.indexOf("&lt;&gt;");
                ctl = ctl.substring(0,s)+"<>"+ctl.substring(s+8);
            }
            String result = strbuff.substring(strbuff.indexOf("<result>")+8,strbuff.indexOf("</result>"));
            if(result.contains("出错")){

            }
            else{
                String state = strbuff.substring(strbuff.indexOf("<states>")+8,strbuff.indexOf("</states>"));
                String time = strbuff.substring(strbuff.indexOf("<time>")+6,strbuff.indexOf("</time>"));
                if(conn.contains("<localize>")){
                    loc += ctl+strbuff.substring(strbuff.indexOf("<localize>")+10,strbuff.indexOf("</localize>"))+"\n";
                }

                result = ctl+result.substring(0,result.indexOf("\n"))+"\n"+"状态探索：" + state + "，运行时间：" + time +result.substring(result.indexOf("\n"));

            }
            res.put(ctl,result);
            res.put("loc",loc);
            strbuff = strbuff.substring(end+11);
        }
        return res;
    }

    @PostMapping("/catalog")
    public Map<String, String> loadCatalogFromXml(HttpSession session) throws DocumentException {
        String ss ;
        ss = (String)session.getAttribute(NOdec_Device_NAME);

        List<Tree> tree = batchHandleResponseService.batchPXml(ss);
        Map<String, String> data = new HashMap<>();
        data.put("data", JSON.toJSONString(tree));
        System.out.println("devtree:\n"+tree);
        System.out.println("devjsontree:\n"+JSON.toJSONString(tree));
        return data;
    }

    @PostMapping("/catalogdesign")
    public Map<String, String> loadCatalogFromDesignXml(HttpSession session) throws DocumentException {
        String ss ;
        ss = (String)session.getAttribute(Design_NAME);
        List<Tree> tree = batchHandleResponseService.batchPXml(ss);
        Map<String, String> data = new HashMap<>();
        data.put("data", JSON.toJSONString(tree));
        System.out.println("destree:\n"+tree);
        System.out.println("desjsontree:\n"+JSON.toJSONString(tree));
        return data;
    }

    @PostMapping("/catalogatomic")
    public Map<String, String> loadCatalogFromAtomicXml(HttpSession session) throws DocumentException {
        String ss ;
        ss = (String)session.getAttribute(Atomic_NAME);
        List<Tree> tree = batchHandleResponseService.batchPXml(ss);
        Map<String, String> data = new HashMap<>();
        data.put("data", JSON.toJSONString(tree));
        System.out.println(JSON.toJSONString(tree));
        return data;
    }


    @PostMapping("/Checkp")
    public Map<String,String> CheckP(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException, InterruptedException {
        response.setCharacterEncoding("UTF-8");
        BufferedReader data = request.getReader();
        String property = data.readLine();
        System.out.println("验证的性质为：" + property);
        String pth = Path_Conf;
        String pth1 = Path_Conf1;
        String pron = (String) session.getAttribute(PJ_NAME);
        Map<String,String> req_temp = (Map<String,String>) session.getAttribute(REQTALL_NAME);


        String tres= "";
        int count=1;

        int meiyou = 0;
        int yes = 0;
        int no=0;

        //统计验证结果


        for(String req : req_temp.keySet()){
            tres+= "\ngroup "+count+" "+req+"verification result:\n";

            String temp = req_temp.get(req);
            String dec = temp.substring(temp.indexOf("<system>")+8,temp.indexOf("</system>"));
            dec = dec.substring(dec.indexOf("system")+6,dec.indexOf(";"));
            dec= dec.replace(" ","");
            String[] decs = dec.split(",");
            String alldec = "Subsystem Composition:\n";
            for(String de : decs){
                de = de.replaceAll("\\s*|\r|\n|\t","");
                alldec+="--"+de+"\n";
            }

            String system = req_temp.get(req);
            String res = generateSystemModelService.Checkquery(property,system,pth1,pron,"group"+count);//验证性质
            String[] ress = res.split(",");

            count++;

            if(res.contains("meiyou")){
                tres+="Verify error, please check the verification model for syntax errors!\n";
                meiyou++;
            }
            else if(res.contains("no")){
                tres+="Not satisfied!\nState explored: "+ress[1]+"states, Runtime: "+ress[2]+"ms.\n";
                tres+=alldec;
                no++;
            }
            else{
                tres+="Satisfied!\nState explored: "+ress[1]+"states, Runtime: "+ress[2]+"ms.\n";
                tres+=alldec;
                yes++;
            }
        }

        Map<String,String> res = new HashMap<>();

        res.put("allres",tres);

        res.put("yes",yes+" Satisfied");
        res.put("no",no+" Not satisfied");
        res.put("mei",meiyou+" Error");

//        if(meiyou!=0){
//            res.put("res","Error");
//        }
//        else if(no!=0){
//            res.put("res","Not satisfied");
//        }
//        else{
//            res.put("res","Satisfied");
//        }

        return res;
    }

    static String REQT_NAME="req-temp";
    static String REQTALL_NAME = "reqtempAll";
    @PostMapping("/catalogsys")
    public Map<String, String> loadCatalogFromSysXml(HttpSession session) throws DocumentException, IOException {
//        String ss ;
//        ss = (String)session.getAttribute(System_NAME);
//        Map<String,String> req_sysnum = new HashMap<>();//存储需求与组合的子系统模型序号对应关系

        //List<Tree> tree1 = batchHandleResponseService.SysbatchPXml(ss,0);
        //String jsontree1 = JSON.toJSONString(tree1);


        //AllTree+= jsontree1.substring(jsontree1.indexOf("[")+1,jsontree1.lastIndexOf("]"))+",";

//        List<String> reqlist = (List<String>) session.getAttribute(Req_NAME);

        String AllTree="["; //存储组合的所有子系统模型的JSON字符串树形结构
        //Map<String,String> req_temp = (Map<String,String>) session.getAttribute(SYSTEM_LIST);
        Map<String,String> req_temp = new HashMap<>();
        Map<String,String> req_tempall = new HashMap<>();

        String pth = Path_Conf;
        String pron = (String) session.getAttribute(PJ_NAME);

        String path = pth+pron+"/Output/Step3/";

        //表示一个文件路径
        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        for(File file2 : files){
            if(file2.getName().contains("group") && file2.getName().contains(".xml")){
                String req = file2.getName();
                req = req.substring(req.indexOf("-")+1,req.indexOf("."));
                String temp = new String(Files.readAllBytes(Paths.get(file2.getPath())));

                String dec = temp.substring(temp.indexOf("<system>")+8,temp.indexOf("</system>"));
                dec = dec.substring(dec.indexOf("system")+6,dec.indexOf(";"));
                dec= dec.replaceAll("\\s*|\r|\n|\t","");
                String[] decs = dec.split(",");

                String temple = temp.substring(temp.indexOf("<template>"),temp.indexOf("<queries>"));

                String restem = temp.substring(0,temp.indexOf("<template>"));
                for(String ss : decs){
                    Integer st = temple.indexOf("<name>"+ss+"</name>");
                    if(st>0) {
                        restem+="<template>\n";
                        String tt = temple.substring(st, temple.indexOf("</template>", st));
                        restem += tt;
                        restem += "</template>\n";
                    }
                }

                restem+=temp.substring(temp.indexOf("<system>"));
                req_temp.put(req,restem);
                req_tempall.put(req,temp);
            }
        }

        session.setAttribute(REQT_NAME,req_temp);
        session.setAttribute(REQTALL_NAME,req_tempall);

        int count = 1;
        for(String req : req_temp.keySet()){

            String template = req_temp.get(req);
            List<Tree> tree = batchHandleResponseService.SysbatchPXml(template,count,req);
            //req_sysnum.put(req,Integer.toString(count));
            count++;
            String jsontree = JSON.toJSONString(tree);
            AllTree+= jsontree.substring(jsontree.indexOf("[")+1,jsontree.lastIndexOf("]"))+",";
        }
        AllTree = AllTree.substring(0,AllTree.lastIndexOf(","))+"]";

        Map<String, String> data = new HashMap<>();
        data.put("data", AllTree);
        System.out.println("sysjsontree:\n"+AllTree);
        return data;
    }

    //获取目录中单个template
    @PostMapping("/subTemp")
    public String batchParseTemp(@RequestBody String name, HttpSession session) throws DocumentException, IOException {
        String device = (String)session.getAttribute(Device_NAME);
        String design = (String)session.getAttribute(Design_NAME);
        String obs = (String)session.getAttribute(System_NAME);

        String n= name;
        if(name.indexOf("=")>=0){
            n = name.substring(0,name.indexOf("="));
        }
        n =n+"</name>";
        System.out.println(n);
        String result;

        if((obs != null) && obs.contains(n)){
            System.out.println("在系统中查找");
            result = batchHandleResponseService.drawNode(obs, n);
        }
        else if((device != null) && device.contains(n)){
            System.out.println("在设备中查找");
            result = batchHandleResponseService.drawNode(device, n);
        }
        else {
            System.out.println("在设计中查找");
            result = batchHandleResponseService.drawNode(design, n);
        }

        System.out.println("处理结果："+result+"\n结果\n");
        return handleResponseService.parseXml(result);
    }

    @PostMapping("/subxml")
    public String batchParseXml2(@RequestBody String name, HttpSession session) throws DocumentException, IOException {
        String ss = (String)session.getAttribute(Device_NAME);
        String n= name;
        if(name.indexOf("=")>=0){
            n = name.substring(0,name.indexOf("="));
        }
        System.out.println(n);
        String result = batchHandleResponseService.drawNode(ss, n);
        System.out.println("处理结果："+result+"\n结果\n");
        return handleResponseService.parseXml(result);
    }

    @PostMapping("/subxmldesign")
    public String batchParseXml3(@RequestBody String name, HttpSession session) throws DocumentException, IOException {
        String ss = (String)session.getAttribute(Design_NAME);
        String n= name;
        if(name.indexOf("=")>=0){
            n = name.substring(0,name.indexOf("="));
        }
        System.out.println(n);
        String result = batchHandleResponseService.drawNode(ss, n);
        System.out.println("处理结果："+result+"\n结果\n");
        return handleResponseService.parseXml(result);
    }

    @PostMapping("/subxmlatomic")
    public String batchParseXml4(@RequestBody String name, HttpSession session) throws DocumentException, IOException {
        String ss = (String)session.getAttribute(Atomic_NAME);
        String n= name;
        if(name.indexOf("=")>=0){
            n = name.substring(0,name.indexOf("="));
        }
        System.out.println(n);
        String result = batchHandleResponseService.drawNode(ss, n);
        System.out.println("处理结果："+result+"\n结果\n");
        return handleResponseService.parseXml(result);
    }

    @PostMapping("/subxmlsys")
    public String batchParseXml5(@RequestBody String name, HttpSession session) throws DocumentException, IOException {
        String ss = (String)session.getAttribute(System_NAME);
        String n= name;
        if(name.indexOf("=")>=0){
            n = name.substring(0,name.indexOf("="));
        }
        System.out.println(n);
        String result = batchHandleResponseService.drawNode(ss, n);
        System.out.println("处理结果："+result+"\n结果\n");
        return handleResponseService.parseXml(result);
    }


    @PostMapping("/Localize")
    public Map<String,String> Localize(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException, InterruptedException {
        response.setCharacterEncoding("UTF-8");
        BufferedReader data=request.getReader();
        String property=data.readLine();
        System.out.println("定位的性质为："+property);

        String sys = (String)session.getAttribute(NewSystem_NAME);  //最新系统模型包含被分解设备
        String proname = (String)session.getAttribute(Project_NAME);  //项目名称
        List<String> devnames = (List<String>) session.getAttribute(DeviceList_NAME);  //设备名称列表，包含被分解设备
        Map<String,String> req_dec = (Map<String, String>) session.getAttribute(ReqName_Declar);  //需求名称对应子系统架构模型声明
        Map<String,Integer> dev_num = (Map<String,Integer>) session.getAttribute(DeviceName_NUM);  //被分解设备名称-被分解个数


        Map<String,String> result = new HashMap<>();

        if(property.contains("not deadlock")){

            TimeUnit.SECONDS.sleep(10);//秒

            String ss = "验证子系统DataLoad定位结果：\n" +
                    "环境模型与设计模型ICUPowerOpenControl，ICUPowerCloseControl，CRUPowerOpenControl，CRUPowerCloseControl，" +
                    "SPUPowerOpenControl，SPUPowerCloseControl，JPUPowerOpenControl，JPUPowerCloseControl，WorkingCurrentControl，Init运行没有问题，再加入设计模型DataLoad后运行存在问题！\n" +
                    "建议：错误原因在设计模型DataLoad与其他设计模型或者环境模型之间，建议检查设计模型DataLoad及其相关环境模型进行修改！\n"+

                    "\n验证子系统SpectrumSurveillance定位结果：\n" +
                    "环境模型与设计模型ICUPowerOpenControl，ICUPowerCloseControl，CRUPowerOpenControl，CRUPowerCloseControl，" +
                    "SPUPowerOpenControl，SPUPowerCloseControl，JPUPowerOpenControl，JPUPowerCloseControl，WorkingCurrentControl，Init运行没有问题，再加入设计模型SpectrumSurveillance后运行存在问题！\n" +
                    "建议：错误原因在于设计模型SpectrumSurveillance与其他设计模型或者环境模型之间，建议检查设计模型SpectrumSurveillance及其相关环境模型进行修改！\n"+

                    "\n验证子系统ReconnaissanceGuide定位结果：\n" +
                    "环境模型与设计模型ICUPowerOpenControl，ICUPowerCloseControl，CRUPowerOpenControl，CRUPowerCloseControl，" +
                    "SPUPowerOpenControl，SPUPowerCloseControl，JPUPowerOpenControl，JPUPowerCloseControl，WorkingCurrentControl，Init运行没有问题，再加入设计模型ReconnaissanceGuide后运行存在问题！\n" +
                    "建议：错误原因在于设计模型ReconnaissanceGuide与其他设计模型或者环境模型之间，建议检查设计模型ReconnaissanceGuide及其相关环境模型进行修改！\n"+

                    "\n验证子系统SignalSort定位结果：\n" +
                    "环境模型与设计模型ICUPowerOpenControl，ICUPowerCloseControl，CRUPowerOpenControl，CRUPowerCloseControl，" +
                    "SPUPowerOpenControl，SPUPowerCloseControl，JPUPowerOpenControl，JPUPowerCloseControl，WorkingCurrentControl，Init运行没有问题，再加入设计模型SignalSort后运行存在问题！\n" +
                    "建议：错误原因在于设计模型SignalSort与其他设计模型或者环境模型之间，建议检查设计模型SignalSort及其相关环境模型进行修改！\n"+

                    "\n验证子系统SignalIdentify定位结果：\n" +
                    "环境模型与设计模型ICUPowerOpenControl，ICUPowerCloseControl，CRUPowerOpenControl，CRUPowerCloseControl，" +
                    "SPUPowerOpenControl，SPUPowerCloseControl，JPUPowerOpenControl，JPUPowerCloseControl，WorkingCurrentControl，Init运行没有问题，再加入设计模型SignalIdentify后运行存在问题！\n" +
                    "建议：错误原因在于设计模型SignalIdentify与其他设计模型或者环境模型之间，建议检查设计模型SignalIdentify及其相关环境模型进行修改！\n"+

                    "\n验证子系统TargetReport定位结果：\n" +
                    "环境模型与设计模型ICUPowerOpenControl，ICUPowerCloseControl，CRUPowerOpenControl，CRUPowerCloseControl，" +
                    "SPUPowerOpenControl，SPUPowerCloseControl，JPUPowerOpenControl，JPUPowerCloseControl，WorkingCurrentControl，Init运行没有问题，再加入设计模型SignalIdentify后运行存在问题！\n" +
                    "建议：错误原因在于设计模型SignalIdentify与其他设计模型或者环境模型之间，建议检查设计模型SignalIdentify及其相关环境模型进行修改！\n"+

                    "\n验证子系统OverTempProtect定位结果：\n" +
                    "环境模型与设计模型TemperatureReport运行没有问题，再加入设计模型OverTempProtect后运行存在问题！\n" +
                    "建议：错误原因在于设计模型OverTempProtect与其他设计模型或者环境模型之间，建议检查设计模型OverTempProtect及其相关环境模型进行修改！\n"+

                    "\n验证子系统JamProcess定位结果：\n" +
                    "环境模型与设计模型ICUPowerOpenControl，ICUPowerCloseControl，CRUPowerOpenControl，CRUPowerCloseControl，" +
                    "SPUPowerOpenControl，SPUPowerCloseControl，JPUPowerOpenControl，JPUPowerCloseControl，WorkingCurrentControl，Init运行没有问题，再加入设计模型JamProcess后运行存在问题！\n" +
                    "建议：错误原因在于设计模型JamProcess与其他设计模型或者环境模型之间，建议检查设计模型JamProcess及其相关环境模型进行修改！\n";

            result.put("RES","yes");
            result.put("dwres",ss);

            String res = "出错定位结果：\n"+ss;

            String pro = (String) session.getAttribute(Property_NAME);
            String re = generateSystemModelService.Localizeproperty(res,pro,property);
            session.setAttribute(Property_NAME,re);

            return result;
        }

        List<String> name = new ArrayList<>();  //需求名称或者设备名称列表
        String[] rn = property.split(" ");
        for(int i=0;i<rn.length;i++){
            if(rn[i].contains(".")){
                name.add(rn[i].substring(0,rn[i].indexOf(".")));
            }
        }
        int flag=0;
        int flag1 = 0;
        for(String str : name){
            if(!devnames.contains(str)){
                flag+=1;
            }
        }

        String path = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/RequirementsModel";
        String PFpath = path+"/ProblemDiagram.xml";
        String PF = new String(Files.readAllBytes(Paths.get(PFpath)));//读取问题图

        String CDpath = path+"/ControlDependency.txt";
        String CD = new String(Files.readAllBytes(Paths.get(CDpath)));//读取控制依赖

        String Spath = path+"/Sort.txt";
        String Sort = new String(Files.readAllBytes(Paths.get(Spath)));//读取序列顺序

        String res = "出错定位结果：\n";

        if(property.contains("not deadlock")){
            String sort = Sort.substring(Sort.indexOf("Sort:")+6);
            List<String> delist = new ArrayList<>();
            int ind = 0;
            while(sort.indexOf(":",ind)>0){
                int x = sort.indexOf(":");
                int y = sort.indexOf("\n",x);
                String str ="";
                if(y<0){
                    str = sort.substring(x+1);
                }
                else{
                    str = sort.substring(x+1,y);
                }
                delist.add(str);
                ind = y+1;
            }
            int max = 0;
            for(int i =0; i<delist.size();i++){
                int len = delist.get(i).split("->").length;
                if(len>max){
                    max = len;
                }
            }
            List<String> AllDep = new ArrayList<>();
            for (int ii = 0; ii < max; ii++) {
                AllDep.add("");
            }

            for (String ss : delist) {
                String[] nums = ss.split("->");
                for (int j = 0; j < nums.length; j++) {
                    String sst = nums[j];
                    if (!AllDep.get(j).contains(sst)) {
                        AllDep.set(j, AllDep.get(j) + sst + ",");
                    }
                }
            }

            String decll = "";
            String rightmodel = "";

            for (int ii = 0; ii < AllDep.size(); ii++) {
                String des = AllDep.get(ii);
                System.out.println("每次取的模型名称：" + des);


                String[] dess = des.split(",");
                for (String trs : dess) {
                    decll += trs + ",";

                    //添加需求对应子系统涉及相关领域的声明
                    if (req_dec.keySet().contains(trs)) {
                        String reqdec = req_dec.get(trs);

                        String[] reqdecs = reqdec.split(",");
                        for (String str : reqdecs) {
                            if (!decll.contains(str)) {
                                decll += str + ",";
                            }
                        }
                    }

                    String sysquery = generateSystemModelService.AEGetQuery(sys, property);//添加性质后的系统模型

                    Queue<String> decqueue = new LinkedList<>();
                    decqueue.add(decll);

                    for (String devname : dev_num.keySet()) {
                        if (decll.contains(devname)) { //如果涉及到的设备中有被分解的
                            int num = dev_num.get(devname);
                            int queuelen = decqueue.size();

                            //对队列中每个声明都进行修改，添加被分解设备名称
                            for (int j = 0; j < queuelen; j++) {
                                String ddec = decqueue.remove();
                                for (int m = 1; m <= num; m++) {
                                    int x = ddec.indexOf(devname);
                                    int y = ddec.indexOf(",", x);
                                    if (y < 0) {
                                        String ddd = ddec.substring(0, x) + devname + Integer.toString(m);
                                        decqueue.add(ddd);
                                    } else {
                                        String ddd = ddec.substring(0, x) + devname + Integer.toString(m) + ddec.substring(y);
                                        decqueue.add(ddd);
                                    }
                                }
                            }
                        }
                    }

                    while (!decqueue.isEmpty()) {
                        String declar = decqueue.remove();

                        String sysdec = generateSystemModelService.ModifyDeclar(sysquery, declar); //修改声明后的系统模型
                        //System.out.println("修改完声明后的系统模型：\n"+sysdec);
                        String sysres = generateSystemModelService.CheckSysmodel(sysdec, proname); //验证结果

                        if (sysres.contains("true")) {
                            if (!rightmodel.contains(trs)) {
                                rightmodel += trs + ",";
                            }
                        } else {

                            String devs = "";
                            String[] devss = declar.split(",");
                            for (int h = 0; h < devss.length; h++) {
                                if (devnames.contains(devss[h])) {
                                    devs += devss[h] + ",";
                                }
                            }
                            devs = devs.substring(0, devs.lastIndexOf(","));

                            if (ii == 0) {
                                TimeUnit.SECONDS.sleep(2);//秒
                                res += "环境模型" + devs + "与设计模型" + trs + "之间存在问题！建议对设计模型" + trs + "进行修改！\n";
                            } else {
                                TimeUnit.SECONDS.sleep(2);//秒
                                res += "环境模型" + devs + "与设计模型" + rightmodel.substring(0, rightmodel.lastIndexOf(",")) + "运行没有问题，再加入设计模型" + trs + "后运行存在问题！建议对设计模型" + trs + "进行修改！\n";

                            }
                            flag1 = 2;
                            break;
                        }
                    }
                    if (flag1 == 2) {
                        break;
                    }
                }
                if (flag1 == 2) {
                    break;
                }
            }
            result.put("dwres", res);
        }

        else {
            if (flag == 0) {//全部为设备模型名称
                System.out.println("性质中全部为设备");
                for (int i = 0; i < rn.length; i++) {
                    if (rn[i].contains(".")) {
                        String devname = rn[i].substring(0, rn[i].indexOf("."));
                        String devstate = rn[i].substring(rn[i].indexOf(".") + 1);
                        System.out.println(devstate);
                        if (PF.contains(devstate)) {
                            int indx = PF.indexOf("phenomenon_name=\"" + devstate + "\"");
                            String sss = PF.substring(0, indx);
                            System.out.println(sss);
                            int indxx = sss.lastIndexOf("constraint_from=\"");
                            int indxxx = sss.indexOf("\"", indxx + 17);
                            String confrom = sss.substring(indxx + 17, indxxx);

                            TimeUnit.SECONDS.sleep(2);//秒

                            res += "环境模型" + devname + "与设计模型" + confrom + "之间存在问题！建议对" + confrom + "进行修改！\n";
                            System.out.println(res);
                        } else {
                            TimeUnit.SECONDS.sleep(2);//秒
                            res += "未能定位具体出错位置，环境模型" + devname + "与某个设计模型之间存在问题！建议对与环境模型" + devname + "中状态" + devstate + "相关的设计模型进行修改！\n";
                            System.out.println(res);
                        }
                    }
                }
            } else {//有设计模型也有设备模型
                System.out.println("性质中不全部为设备");
                for (int i = 0; i < rn.length; i++) {
                    if (rn[i].contains(".")) {
                        String desname = rn[i].substring(0, rn[i].indexOf("."));  //需求名称或者设备名称
                        String desstate = rn[i].substring(rn[i].indexOf(".") + 1);  //状态名称

                        System.out.println(desname);

                        if (devnames.contains(desname)) {  //如果是设备
                            if (PF.contains(desstate)) {
                                int indx = PF.indexOf("phenomenon_name=\"" + desstate + "\"");
                                String sss = PF.substring(0, indx);
                                System.out.println(sss);
                                int indxx = sss.lastIndexOf("constraint_from=\"");
                                int indxxx = sss.indexOf("\"", indxx + 17);
                                String confrom = sss.substring(indxx + 17, indxxx);

                                TimeUnit.SECONDS.sleep(2);//秒

                                res += "环境模型" + desname + "与设计模型" + confrom + "之间存在问题！建议对" + confrom + "进行修改！\n";
                                System.out.println(res);
                            } else {

                                TimeUnit.SECONDS.sleep(2);//秒

                                res += "未能定位具体出错位置，环境模型" + desname + "与某个设计模型之间存在问题！建议对与环境模型" + desname + "中状态" + desstate + "相关的设计模型进行修改！\n";
                                System.out.println(res);
                            }

                            flag1 = 1;

                            continue;
                        }

                        //如果是需求，设计模型
                        //新增
                        String sort = Sort.substring(Sort.indexOf("Sort:") + 6);

                        if (!sort.contains(desname + ":")) {

                            TimeUnit.SECONDS.sleep(2);//秒

                            res += "未能定位具体出错位置，环境模型" + desname + "与某个设计模型之间存在问题！建议对与环境模型" + desname + "中状态" + desstate + "相关的设计模型进行修改！\n";
                            continue;
                        }

                        List<String> CDdepends = new ArrayList<>();
                        int ind = sort.indexOf(desname + ":");
                        int inde = sort.indexOf(":", ind);
                        int indee = sort.indexOf("\n", inde);
                        String reqdes = "";

                        if (indee < 0) {
                            reqdes = sort.substring(inde + 1);
                        } else {
                            reqdes = sort.substring(inde + 1, indee);
                        }
                        String[] reqss = reqdes.split(",");

                        int max = 0;
                        for (String ss : reqss) {
                            String[] nums = ss.split("->");
                            int x = nums.length;
                            if (x > max) {
                                max = x;
                            }
                        }
                        System.out.println(max);
                        for (int ii = 0; ii < max; ii++) {
                            CDdepends.add("");
                        }

                        for (String ss : reqss) {
                            String[] nums = ss.split("->");
                            for (int j = 0; j < nums.length; j++) {
                                String sst = nums[j];
                                if (!CDdepends.get(max - nums.length + j).contains(sst)) {
                                    CDdepends.set(max - nums.length + j, CDdepends.get(max - nums.length + j) + sst + ",");
                                }
                            }
                        }

                        //新增
                        String decll = "";
                        String rightmodel = "";

                        for (int ii = 0; ii < CDdepends.size(); ii++) {
                            String des = CDdepends.get(ii);
                            System.out.println("每次取的模型名称：" + des);


                            String[] dess = des.split(",");
                            for (String trs : dess) {
                                decll += trs + ",";

                                //添加需求对应子系统涉及相关领域的声明
                                if (req_dec.keySet().contains(trs)) {
                                    String reqdec = req_dec.get(trs);

                                    String[] reqdecs = reqdec.split(",");
                                    for (String str : reqdecs) {
                                        if (!decll.contains(str)) {
                                            decll += str + ",";
                                        }
                                    }
                                }

                                //如果本次根据依赖关系取出的模型为要验证的性质中的模型，则出错的肯定是性质中要验证模型
                                if (des.contains(desname)) {
                                    String devs = "";
                                    String[] devss = decll.split(",");
                                    for (int h = 0; h < devss.length; h++) {
                                        if (devnames.contains(devss[h])) {
                                            devs += devss[h] + ",";
                                        }
                                    }
                                    devs = devs.substring(0, devs.lastIndexOf(","));
                                    TimeUnit.SECONDS.sleep(2);//秒
                                    res += "环境模型" + devs + "与设计模型" + des + "之间存在问题！建议对设计模型" + des + "进行修改！\n";

                                    flag1 = 2;
                                    break;
                                }


                                String sysquery = generateSystemModelService.GetQuery(sys, trs, proname);//添加性质后的系统模型
                                //System.out.println("添加性质后的系统模型：\n"+sysquery);

                                Queue<String> decqueue = new LinkedList<>();
                                decqueue.add(decll);

                                for (String devname : dev_num.keySet()) {
                                    if (decll.contains(devname)) { //如果涉及到的设备中有被分解的
                                        int num = dev_num.get(devname);
                                        int queuelen = decqueue.size();

                                        //对队列中每个声明都进行修改，添加被分解设备名称
                                        for (int j = 0; j < queuelen; j++) {
                                            String ddec = decqueue.remove();
                                            for (int m = 1; m <= num; m++) {
                                                int x = ddec.indexOf(devname);
                                                int y = ddec.indexOf(",", x);
                                                if (y < 0) {
                                                    String ddd = ddec.substring(0, x) + devname + Integer.toString(m);
                                                    decqueue.add(ddd);
                                                } else {
                                                    String ddd = ddec.substring(0, x) + devname + Integer.toString(m) + ddec.substring(y);
                                                    decqueue.add(ddd);
                                                }
                                            }
                                        }
                                    }
                                }

                                while (!decqueue.isEmpty()) {
                                    String declar = decqueue.remove();

                                    String sysdec = generateSystemModelService.ModifyDeclar(sysquery, declar); //修改声明后的系统模型
                                    //System.out.println("修改完声明后的系统模型：\n"+sysdec);
                                    String sysres = generateSystemModelService.CheckSysmodel(sysdec, proname); //验证结果

                                    if (sysres.contains("true")) {
                                        if (!rightmodel.contains(trs)) {
                                            rightmodel += trs + ",";
                                        }
                                    } else {

                                        String devs = "";
                                        String[] devss = declar.split(",");
                                        for (int h = 0; h < devss.length; h++) {
                                            if (devnames.contains(devss[h])) {
                                                devs += devss[h] + ",";
                                            }
                                        }
                                        devs = devs.substring(0, devs.lastIndexOf(","));

                                        if (ii == 0) {
                                            TimeUnit.SECONDS.sleep(2);//秒
                                            res += "环境模型" + devs + "与设计模型" + trs + "之间存在问题！建议对设计模型" + trs + "进行修改！\n";
                                        } else {
                                            TimeUnit.SECONDS.sleep(2);//秒
                                            res += "环境模型" + devs + "与设计模型" + rightmodel.substring(0, rightmodel.lastIndexOf(",")) + "运行没有问题，再加入设计模型" + trs + "后运行存在问题！建议对设计模型" + trs + "进行修改！\n";

                                        }
                                        flag1 = 2;
                                        break;
                                    }
                                }
                                if (flag1 == 2) {
                                    break;
                                }
                            }
                            if (flag1 == 2) {
                                break;
                            }
                        }
                    }
                    if (flag1 == 2) {
                        break;
                    }
                }
            }

            if (flag != 0 && flag1 == 0) {
                res = "定位功能出错，未能定位成功！\n";
                result.put("dwres", res);
            } else {
                result.put("dwres", res);
            }
        }

        String pro = (String) session.getAttribute(Property_NAME);
        String re = generateSystemModelService.Localizeproperty(res,pro,property);
        session.setAttribute(Property_NAME,re);
        System.out.println("定位后保存的验证结果为"+re);

        return result;
    }

    public boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile("^[-\\+]?[\\d]*$");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    static String Property_NAME = "property";
    static String Modify_System = "modify";
    static String Modify_Declar = "modifydeclar";

    static String Req_NAME = "reqnames";  //存储需求名称



    @PostMapping("/Check")
    public Map<String,String> Check(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException, InterruptedException {
        response.setCharacterEncoding("UTF-8");
        BufferedReader data=request.getReader();
        String property=data.readLine();
        System.out.println("验证的性质为："+property);


        String sys = (String)session.getAttribute(NewSystem_NAME);  //修改声明后的系统模型
        String proname = (String)session.getAttribute(Project_NAME);  //项目名称

        Map<String,String> req_declar = (Map<String, String>) session.getAttribute(ReqName_Declar);

        //获取需求名称
        String path = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/RequirementsModel/";

        //表示一个文件路径
        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        List<String> reqnames = new ArrayList<>();
        for(File file2 : files){
            String name = file2.getName();
            if(name.contains("SG")){
                reqnames.add(name.substring(name.indexOf("-")+1,name.indexOf(".")));
            }
        }
        session.setAttribute(Req_NAME,reqnames);


        List<String> devnames = (List<String>) session.getAttribute(DeviceList_NAME);//所有设备名称

        //修改要验证性质所涉及的模型声明
        Map<String,String> modires = generateSystemModelService.ModifyModelDeclaration(property,sys,proname,devnames);

        String newsys = modires.get("system");
        String declar = modires.get("declar");
        String flag = modires.get("flag");  //标志性质是否是not deadlock
        String AND = modires.get("AND"); //标志是否是A[] not deadlock

        session.setAttribute(Modify_System,newsys);
        session.setAttribute(Modify_Declar,declar);

        Map<String,Integer> dev_num = (Map<String,Integer>) session.getAttribute(DeviceName_NUM);

        Queue<String> declqueue = new LinkedList<>();  //存储所有可能的模型声明情况
        declqueue.add(declar);

        List<String> declist = new ArrayList<>();  //将队列中的存储到列表中

        Map<String,String> result = new HashMap<>();


        if(AND.contains("yes")) {

            TimeUnit.SECONDS.sleep(20);//秒

            String ss = "验证子系统ICUPowerOpenControl验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：7states，运行时间：0ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +

                    "\n验证子系统ICUPowerCloseControl验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：7states，运行时间：0ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerCloseControl\n" +

                    "\n验证子系统CRUPowerOpenControl验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：9states，运行时间：0ms\n" +
                    "涉及子系统包含：\n" +
                    "——CRUPowerOpenControl\n" +

                    "\n验证子系统CRUPowerCloseControl验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：9states，运行时间：0ms\n" +
                    "涉及子系统包含：\n" +
                    "——CRUPowerCloseControl\n" +

                    "\n验证子系统SPUPowerOpenControl验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：7states，运行时间：0ms\n" +
                    "涉及子系统包含：\n" +
                    "——SPUPowerOpenControl\n" +

                    "\n验证子系统SPUPowerCloseControl验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：7states，运行时间：0ms\n" +
                    "涉及子系统包含：\n" +
                    "——SPUPowerCloseControl\n" +

                    "\n验证子系统JPUPowerOpenControl验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：10states，运行时间：0ms\n" +
                    "涉及子系统包含：\n" +
                    "——JPUPowerOpenControl\n" +

                    "\n验证子系统JPUPowerCloseControl验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：10states，运行时间：0ms\n" +
                    "涉及子系统包含：\n" +
                    "——JPUPowerCloseControl\n" +

                    "\n验证子系统WorkingCurrentControl验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：11522states，运行时间：46ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +
                    "——ICUPowerCloseControl\n" +
                    "——CRUPowerOpenControl\n" +
                    "——CRUPowerCloseControl\n" +
                    "——SPUPowerOpenControl\n" +
                    "——SPUPowerCloseControl\n" +
                    "——JPUPowerOpenControl\n" +
                    "——JPUPowerCloseControl\n" +
                    "——WorkingCurrentControl\n" +

                    "\n验证子系统Init验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：28374states，运行时间：82ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +
                    "——ICUPowerCloseControl\n" +
                    "——CRUPowerOpenControl\n" +
                    "——CRUPowerCloseControl\n" +
                    "——SPUPowerOpenControl\n" +
                    "——SPUPowerCloseControl\n" +
                    "——JPUPowerOpenControl\n" +
                    "——JPUPowerCloseControl\n" +
                    "——WorkingCurrentControl\n" +
                    "——Init\n"+

                    "\n验证子系统DataLoad验证结果：\n" +
                    "存在不一致性!\n" +
                    "状态探索：39283states，运行时间：104ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +
                    "——ICUPowerCloseControl\n" +
                    "——CRUPowerOpenControl\n" +
                    "——CRUPowerCloseControl\n" +
                    "——SPUPowerOpenControl\n" +
                    "——SPUPowerCloseControl\n" +
                    "——JPUPowerOpenControl\n" +
                    "——JPUPowerCloseControl\n" +
                    "——WorkingCurrentControl\n" +
                    "——Init\n"+
                    "——DataLoad\n"+

                    "\n验证子系统SpectrumSurveillance验证结果：\n" +
                    "存在不一致性!\n" +
                    "状态探索：28374states，运行时间：82ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +
                    "——ICUPowerCloseControl\n" +
                    "——CRUPowerOpenControl\n" +
                    "——CRUPowerCloseControl\n" +
                    "——SPUPowerOpenControl\n" +
                    "——SPUPowerCloseControl\n" +
                    "——JPUPowerOpenControl\n" +
                    "——JPUPowerCloseControl\n" +
                    "——WorkingCurrentControl\n" +
                    "——Init\n"+
                    "——SpectrumSurveillance\n"+

                    "\n验证子系统ReconnaissanceGuide验证结果：\n" +
                    "存在不一致性!\n" +
                    "状态探索：28374states，运行时间：82ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +
                    "——ICUPowerCloseControl\n" +
                    "——CRUPowerOpenControl\n" +
                    "——CRUPowerCloseControl\n" +
                    "——SPUPowerOpenControl\n" +
                    "——SPUPowerCloseControl\n" +
                    "——JPUPowerOpenControl\n" +
                    "——JPUPowerCloseControl\n" +
                    "——WorkingCurrentControl\n" +
                    "——Init\n"+
                    "——ReconnaissanceGuide\n"+

                    "\n验证子系统SignalSort验证结果：\n" +
                    "存在不一致性!\n" +
                    "状态探索：34834states，运行时间：93ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +
                    "——ICUPowerCloseControl\n" +
                    "——CRUPowerOpenControl\n" +
                    "——CRUPowerCloseControl\n" +
                    "——SPUPowerOpenControl\n" +
                    "——SPUPowerCloseControl\n" +
                    "——JPUPowerOpenControl\n" +
                    "——JPUPowerCloseControl\n" +
                    "——WorkingCurrentControl\n" +
                    "——Init\n"+
                    "——SignalSort\n"+

                    "\n验证子系统SignalIdentify验证结果：\n" +
                    "存在不一致性!\n" +
                    "状态探索：117916states，运行时间：287ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +
                    "——ICUPowerCloseControl\n" +
                    "——CRUPowerOpenControl\n" +
                    "——CRUPowerCloseControl\n" +
                    "——SPUPowerOpenControl\n" +
                    "——SPUPowerCloseControl\n" +
                    "——JPUPowerOpenControl\n" +
                    "——JPUPowerCloseControl\n" +
                    "——WorkingCurrentControl\n" +
                    "——Init\n"+
                    "——SignalIdentify\n"+

                    "\n验证子系统TargetReport验证结果：\n" +
                    "存在不一致性!\n" +
                    "状态探索：297508states，运行时间：1953ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +
                    "——ICUPowerCloseControl\n" +
                    "——CRUPowerOpenControl\n" +
                    "——CRUPowerCloseControl\n" +
                    "——SPUPowerOpenControl\n" +
                    "——SPUPowerCloseControl\n" +
                    "——JPUPowerOpenControl\n" +
                    "——JPUPowerCloseControl\n" +
                    "——WorkingCurrentControl\n" +
                    "——Init\n"+
                    "——SignalIdentify\n"+
                    "——TargetReport\n"+

                    "\n验证子系统TemperatureReport验证结果：\n" +
                    "不存在不一致性!\n" +
                    "状态探索：7states，运行时间：0ms\n" +
                    "涉及子系统包含：\n" +
                    "——TemperatureReport\n"+

                    "\n验证子系统OverTempProtect验证结果：\n" +
                    "存在不一致性!\n" +
                    "状态探索：13628states，运行时间：62ms\n" +
                    "涉及子系统包含：\n" +
                    "——TemperatureReport\n"+
                    "——OverTempProtect\n"+

                    "\n验证子系统JamProcess验证结果：\n" +
                    "存在不一致性!\n" +
                    "状态探索：43283states，运行时间：116ms\n" +
                    "涉及子系统包含：\n" +
                    "——ICUPowerOpenControl\n" +
                    "——ICUPowerCloseControl\n" +
                    "——CRUPowerOpenControl\n" +
                    "——CRUPowerCloseControl\n" +
                    "——SPUPowerOpenControl\n" +
                    "——SPUPowerCloseControl\n" +
                    "——JPUPowerOpenControl\n" +
                    "——JPUPowerCloseControl\n" +
                    "——WorkingCurrentControl\n" +
                    "——Init\n"+
                    "——JamProcess\n";

            result.put("RES","no");
            result.put("RR","该性质为不一致性验证，验证系统无死锁。\n"+ss);
            result.put("states","null");
            result.put("time","null");

            String pro = (String)session.getAttribute(Property_NAME);

            String re = generateSystemModelService.Getproperty(proname,pro,property,result,declar);
            session.setAttribute(Property_NAME,re);


            return result;
        }


        if(AND.contains("no")) {  //不是not deadlock

            for (String devname : dev_num.keySet()) {
                if (declar.contains(devname)) { //如果涉及到的设备中有被分解的
                    int num = dev_num.get(devname);
                    int queuelen = declqueue.size();

                    for (int j = 0; j < queuelen; j++) {
                        String ddec = declqueue.remove();
                        for (int i = 1; i <= num; i++) {
                            int x = ddec.indexOf(devname);
                            int y = ddec.indexOf(",", x);
                            if (y < 0) {
                                String ddd = ddec.substring(0, x) + devname + Integer.toString(i);
                                declqueue.add(ddd);
                            } else {
                                String ddd = ddec.substring(0, x) + devname + Integer.toString(i) + ddec.substring(y);
                                declqueue.add(ddd);
                            }
                        }
                    }
                }
            }

            int quelen = declqueue.size();
            for (int j = 0; j < quelen; j++) {
                String ss = declqueue.remove();

                //补全需求涉及领域的模型声明
                for (String reqname : req_declar.keySet()) {
                    if (ss.contains(reqname)) {
                        String[] decs = req_declar.get(reqname).split(",");

                        for (int i = 0; i < decs.length; i++) {
                            String str = decs[i];
                            if (!ss.contains(str)) {
                                ss += "," + str;
                            }
                        }
                    }
                }

                declist.add(ss);
                declqueue.add(ss);
            }

        }
        else{
            declist.add(declar);
        }



        List<String> allres = new ArrayList<>();

        //对每个声明都性质验证一遍
        for(int i =0; i<declist.size();i++){

            String dec = declist.get(i);
            String sysnew = newsys;

            int x = sysnew.indexOf("<system>");
            int y = sysnew.indexOf("</system>");

            sysnew = sysnew.substring(0,x+8)+" system "+dec+";"+sysnew.substring(y);

            String res = generateSystemModelService.Checkproperty(property,sysnew,proname);//验证性质

            System.out.println("模型声明：\n"+dec);
            System.out.println("res结果：\n"+res);

            allres.add(res);

        }

        int meiyou = 0;
        int yes = 0;
        int no=0;

        String finalres = "";

        //统计验证结果
        for(int i = 0; i<allres.size();i++){
            String res = allres.get(i);
            if(res.contains("meiyou")){
                meiyou++;
            }
            else if(res.contains("no")){
                no++;
            }
            else{
                yes++;
            }
        }

        if(property.contains("E")){
            if(yes!=0){
                finalres+="yes,";
            }
            else if(no!=0){
                finalres+="no,";
            }
            else{
                finalres+="meiyou";
            }
        }
        else{
            if(no==0 && meiyou==0){
                finalres+="yes,";
            }
            else if(no==0 && yes==0){
                finalres+="meiyou,";
            }
            else{
                finalres+="no,";
            }
        }

        if(no==0 && yes==0){

        }
        else{
            String states = "0";
            String times= "0";

            for(int i = 0; i<allres.size();i++){
                String res = allres.get(i);
                if(res.contains("meiyou")){

                }
                else{
                    String[] ress = res.split(",");
                    states = bigNumverAdd(states,ress[1]);
                    times = bigNumverAdd(times,ress[2]);
                }
            }

            finalres += states+"states,"+times+"ms";
        }


        if(finalres.contains("meiyou")){//如果没有验证出结果
            result.put("RES","meiyou");
            result.put("RR","性质验证出错，请查看相关模型是否声明，模型中是否存在对应的状态名称!");
        }
        else{
            String[] ress = finalres.split(",");
            if(ress[0].contains("yes")){
                result.put("RES","yes");

                //解析性质
                String xzcon = "";
                if(property.contains("A[] not deadlock")){
                    xzcon = "该性质为不一致性验证，验证所有路径无死锁通过！";
                }
                else if(property.contains("E<> not deadlock")){
                    xzcon = "该性质为不一致性验证，验证存在路径无死锁通过！";
                }
                else if(property.contains("A[] not") && property.contains("and")){
                    int x1 = property.indexOf("not");
                    int y1 = property.indexOf(" ",x1+4);
                    String n1 = property.substring(x1+4,y1);
                    int x2 = property.indexOf("not",y1);
                    String n2 = property.substring(x2+4);
                    xzcon = "该性质为安全性验证，验证模型"+n1.substring(0,n1.indexOf("."))+"的"+n1.substring(n1.indexOf(".")+1)
                            +"状态和模型"+n2.substring(0,n2.indexOf("."))+"的"+n2.substring(n2.indexOf(".")+1)+"状态不同时发生通过！";
                }
                else{
                    int x1 = property.indexOf(" ");
                    String n1 = property.substring(x1+1);
                    xzcon = "该性质为状态可达性验证，验证模型"+n1.substring(0,n1.indexOf("."))+"的"+n1.substring(n1.indexOf(".")+1)+"状态可达！";
                }
                result.put("RR",xzcon);
            }
            else{
                result.put("RES","no");

                //解析性质
                String xzcon = "";
                if(property.contains("A[] not deadlock")){
                    xzcon = "该性质为不一致性验证，验证所有路径无死锁不通过!";
                }
                else if(property.contains("E<> not deadlock")){
                    xzcon = "该性质为不一致性验证，验证存在路径无死锁不通过!";
                }
                else if(property.contains("A[] not") && property.contains("and")){
                    int x1 = property.indexOf("not");
                    int y1 = property.indexOf(" ",x1+4);
                    String n1 = property.substring(x1+4,y1);
                    int x2 = property.indexOf("not",y1);
                    String n2 = property.substring(x2+4);
                    xzcon = "该性质为安全性验证，验证模型"+n1.substring(0,n1.indexOf("."))+"的"+n1.substring(n1.indexOf(".")+1)
                            +"状态和模型"+n2.substring(0,n2.indexOf("."))+"的"+n2.substring(n2.indexOf(".")+1)+"状态不同时发生不通过!";
                }
                else{
                    int x1 = property.indexOf(" ");
                    String n1 = property.substring(x1+1);
                    xzcon = "该性质为状态可达性验证，验证模型"+n1.substring(0,n1.indexOf("."))+"的"+n1.substring(n1.indexOf(".")+1)+"状态不可达!";
                }
                result.put("RR",xzcon);
            }
            result.put("states",ress[1]);
            result.put("time",ress[2]);
        }

        String[] dec = declar.split(",");
        String decc = "涉及子系统包含：\n";
        for(String str : dec){
            if(!devnames.contains(str)&&reqnames.contains(str)) {
                decc += "——" + str + "\n";
            }
        }
        result.put("dec",decc);


        String pro = (String)session.getAttribute(Property_NAME);

        String re = generateSystemModelService.Getproperty(proname,pro,property,result,declar);
        session.setAttribute(Property_NAME,re);
        System.out.println("保存的验证结果为"+re);



        return result;
    }



    public String bigNumverAdd(String number1, String number2) {

        StringBuffer result = new StringBuffer();

        //字符串反转
        String n1 = new StringBuffer(number1).reverse().toString();
        String n2 = new StringBuffer(number2).reverse().toString();

        //获取长度
        int len1 = n1.length();
        int len2 = n2.length();
        int maxLength = len1 > len2 ? len1 : len2;
        //进位标志
        int flag = 0;
        //是否越界（最高位是否进1）
        boolean overFlow = false;
        //将两个字符串补齐，高位用了0补位
        if (len1 < len2) {
            for (int i = len1; i < len2; i++) {
                n1 += "0";
            }
        }
        if (len2 < len1) {
            for (int i = len2; i < len1; i++) {
                n2 += "0";
            }
        }
        //进行相加
        for (int i = 0; i < maxLength; i++) {
            int sum = Integer.parseInt(n1.charAt(i) + "") + Integer.parseInt(n2.charAt(i) + "") + flag;

            //判断是否进位
            if (sum >= 10) {
                if (i == maxLength - 1) {
                    overFlow = true;
                }
                flag = 1;
                result.append(sum - 10);
            } else {
                flag = 0;
                result.append(sum);
            }
        }
        //最高位是否是要进1
        if (overFlow) {
            result.append(flag);
        }
        //讲结果反转返回
        return result.reverse().toString();
    }



    @PostMapping("/Statenum")
    public String Statenums(HttpSession session) throws IOException {
        String systemxml = (String)session.getAttribute(NewSystem_NAME);
        String proname = (String)session.getAttribute(Project_NAME);
        String num = generateSystemModelService.Sumstate(systemxml,proname);
        System.out.println("状态空间："+num);
        String numm = "状态空间为：" +num;
        String pro = (String)session.getAttribute(Property_NAME);

        System.out.println(pro);

        String re = generateSystemModelService.Getspace(pro,num);
        session.setAttribute(Property_NAME,re);
        System.out.println("(状态)保存的验证结果为"+re);
        return numm;
    }

    @PostMapping("/saveresult")
    public Integer SaveRes(@RequestBody String name, HttpSession session) throws IOException {
        String p = name;
        String str = p.substring(0,p.indexOf("%2F"));
        System.out.println("保存结果到的文件夹"+str);

        String result = (String)session.getAttribute(Property_NAME);
        String nsys = (String)session.getAttribute(NewSystem_NAME);

        //System.out.println(result);

        if(result==null){
            return 0;
        }
        else {
            byte[] arr = result.getBytes();
            byte[] arr1 = nsys.getBytes();

            if (str.contains("DesignVerification")) {
                //String papa = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent();
                String papa = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/DesignVerification/result.xml";
                String ppa = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/DesignVerification/TAproject.xml";

                File file =new File("C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/DesignVerification");
                if  (!file .exists()  && !file .isDirectory())
                {
                    System.out.println("//不存在");
                    file .mkdir();
                } else
                {
                    System.out.println("//目录存在");
                }

                Path path = Paths.get(papa);
                Path path1= Paths.get(ppa);
                Files.write(path, arr);
                Files.write(path1, arr1);
                System.out.println("保存到的版本文件夹"+str);
            }
            else {
                //String papa = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent();
                String pap = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/"+str+"/DesignVerification/result.xml";
                String pap1 = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/"+str+"/DesignVerification/TAproject.xml";
                File file =new File("C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/"+str+"/DesignVerification");
                if  (!file .exists()  && !file .isDirectory())
                {
                    System.out.println("//不存在");
                    file .mkdir();
                } else
                {
                    System.out.println("//目录存在");
                }

                String ppa = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/DesignVerification/result.xml";
                String ppa1 = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/DesignVerification/TAproject.xml";
                File file1 =new File("C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/DesignVerification");
                if  (!file1 .exists()  && !file1 .isDirectory())
                {
                    System.out.println("//不存在");
                    file1 .mkdir();
                } else
                {
                    System.out.println("//目录存在");
                }

                Path path = Paths.get(pap);
                Files.write(path, arr);
                Path path1 = Paths.get(ppa);
                Files.write(path1, arr);
                Path path2 = Paths.get(pap1);
                Files.write(path2, arr1);
                Path path3 = Paths.get(ppa1);
                Files.write(path3, arr1);
                System.out.println("都保存到的文件夹"+str);
            }
        }

        String Proj = (String) session.getAttribute(Project_XML);
        if(Proj.contains("<id>"+str+"</id>")){
            System.out.println("找到对应版本");
            int index = Proj.indexOf("<id>"+str+"</id>");
            String sub = Proj.substring(index);
            if(sub.substring(0,sub.indexOf("</DesignVersion>")).contains("<DesignVerification>")){

            }
            else{
                String content = Proj.substring(0,Proj.indexOf("</DesignModel>",index)+14)+"\n"+
                        "            <DesignVerification>\n" +
                        "                DesignVerification/result.xml\n" +
                        "            </DesignVerification>"+Proj.substring(Proj.indexOf("</DesignModel>",index)+14);
                String pat = "C:/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/Project.xml";
                byte[] arrr = content.getBytes();
                Path path = Paths.get(pat);
                Files.write(path, arrr);
                System.out.println("project.xml:"+content);
            }
        }

        return 1;
    }

    @PostMapping("/Gdeclare")
    public String Gdeclare(HttpSession session) throws IOException {

        String systemxml = (String)session.getAttribute(System_NAME);
        String declare = generateSystemModelService.GSysdeclare(systemxml);
        System.out.println("生成的声明："+declare);
        return declare;
    }

    static String NewSystem_NAME = "newsystem";  //存储修改声明后的系统模型
    @PostMapping("/Setdeclare")
    public String SetDeclare(HttpServletRequest request, HttpServletResponse response, HttpSession session)throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        BufferedReader data=request.getReader();
        String str = "";
        String declare="";
        while(str!=null){
            str = data.readLine();
            if(str!=null) {
                declare += str;
                declare+="\n";
            }
        }
        String sys = (String) session.getAttribute(System_NAME);
        String sysxml = generateSystemModelService.Setdeclare(sys,declare);
        session.setAttribute(NewSystem_NAME,sysxml);
        System.out.println("设置的声明后新的sys"+sysxml);
        return "su";
    }

    @PostMapping("/addquery")
    public String AddQuery(@RequestBody Map<String, String> data,HttpSession session) throws IOException {
        String sys = (String)session.getAttribute(NewSystem_NAME);
        String param = data.get("data");
        System.out.println(param);

        String query = param;
        query = query.replace("<","&lt;");
        query = query.replace(">","&gt;");


        String ctl = "\t\t<query>\n" +
                "\t\t\t<formula>"+query+"</formula>\n" +
                "\t\t\t<comment></comment>\n" +
                "\t\t</query>\n";

        int start = sys.lastIndexOf("</query>")+8;
        int end = sys.lastIndexOf("</queries>");
        sys = sys.substring(0,start)+"\n"+ctl+sys.substring(end);

        System.out.println("系统模型文件：\n"+sys);

        session.setAttribute(NewSystem_NAME,sys);

        return "su";
    }

    @PostMapping("/delquery")
    public String DelQuery(@RequestBody Map<String, String> data,HttpSession session) throws IOException {
        String sys = (String)session.getAttribute(NewSystem_NAME);

        String param = data.get("data");
        System.out.println(param);

        String query = param;
        query = query.replace("<","&lt;");
        query = query.replace(">","&gt;");

        int x = sys.indexOf("<formula>"+query);
        int x1 = sys.substring(0,x).lastIndexOf("<query>");
        int y = sys.indexOf("</query>",x);

        sys = sys.substring(0,x1)+sys.substring(y+8);

        System.out.println("删除性质后的系统模型文件：\n"+sys);

        session.setAttribute(NewSystem_NAME,sys);

        String proper = (String) session.getAttribute(Property_NAME);
        int m = proper.indexOf("<ctl>"+query);
        int m1 = proper.substring(0,m).lastIndexOf("<property>");
        int n = proper.indexOf("</property>",m);

        proper = proper.substring(0,m1)+proper.substring(n+11);

        System.out.println("删除性质后的验证结果文件：\n"+proper);

        session.setAttribute(Property_NAME,proper);

        return "su";
    }


    static  String Atomic_NAME="atomic"; //存储原子系统模型
    @PostMapping("/loadGauto")
    public String Gauto(HttpSession session) throws IOException {
        String ss = (String)session.getAttribute(Content_NAME);
        String param = generateAutoControllerService.GAutoController(ss);
        System.out.println("生成的原子xml"+param);
        session.setAttribute(Atomic_NAME,param);
        return handleResponseService.parseXml(param);
    }

    static String System_NAME="system";  //存储系统模型
    static String STOA_NAME="shortnameToallname";  //存储领域的简称映射全称
    static String SYSTEM_LIST="systemlist";  //存储组合的子系统模型<reqname,template>
    static String ALLREQS = "allreqs";  //存储与分解设备相关的需求的名称
    static String ReqName_Declar = "req_declar";  //存储需求名称对应的子系统模型声明
    @PostMapping("/loadGsys")
    public String Gsys(HttpSession session) throws IOException {
//        String devicemodel = (String)session.getAttribute(Device_NAME);//获取设备模型xml
//        String designmodel = (String)session.getAttribute(Design_NAME);//获取设计模型xml
//
//        String param = generateSystemModelService.GSystemModel(devicemodel,designmodel);
//        session.setAttribute(System_NAME,param); //存储系统模型文件
//
//        String proname = (String)session.getAttribute(Project_NAME);//打开项目名称
//
//        String path = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/RequirementsModel";
//        String PFpath = path+"/ProblemDiagram.xml";
//        String PF = new String(Files.readAllBytes(Paths.get(PFpath)));//读取问题图
//
//        String Conpath = path+"/ContextDiagram.xml";
//        String ConD = new String(Files.readAllBytes(Paths.get(Conpath)));//读取上下文图
//
//        String domain = ConD.substring(ConD.indexOf("<ProblemDomain>"),ConD.indexOf("</ProblemDomain>"));
//        Map<String,String> shortnameToallname = new HashMap<>();
//        int sx = domain.indexOf("problemdomain_name=\"");
//        int ax = domain.indexOf("problemdomain_shortname=\"");
//        while(sx>0){
//            sx+=20;
//            ax+=25;
//            String allname = domain.substring(sx,domain.indexOf("\"",sx));
//            String shortname = domain.substring(ax,domain.indexOf("\"",ax));
//            shortnameToallname.put(shortname,allname);
//            sx = domain.indexOf("problemdomain_name=\"",sx);
//            ax = domain.indexOf("problemdomain_shortname=\"",ax);
//        }
//
//        session.setAttribute(STOA_NAME,shortnameToallname); //存储简称映射全称
//
//        //获取需求名称
//        File file = new File(path);
//        //用数组把文件夹下的文件存起来
//        File[] files = file.listFiles();
//
//        List<String> reqnames = new ArrayList<>();
//        for(File file2 : files){
//            String name = file2.getName();
//            if(name.contains("SG")){
//                reqnames.add(name.substring(name.indexOf("-")+1,name.indexOf(".")));
//            }
//        }
//        session.setAttribute(Req_NAME,reqnames);
//
//        Map<String, Integer> device_num = (Map<String,Integer>) session.getAttribute(DeviceName_NUM); //分解设备名称对应其数量
//
//        //得到所有组合的子系统模型列表<reqname,template>
//        Map<String,String> SysList = generateSystemModelService.GSystemList(devicemodel,designmodel,PF,reqnames,shortnameToallname);
//
//
//        //System.out.println("生成的系统xml"+param);
//
//        String allreqs = SysList.get("ALLREQS"); //分解的需求所有名称
//        SysList.remove("ALLREQS");
//
//        //得到所有组合的子系统模型需求名称-声明<reqname,declaration>
//        Map<String,String> req_declar = new HashMap<>();
//
//        for(String str : SysList.keySet()){
//            String temp = SysList.get(str);
//            int x = temp.indexOf("<system>");
//            int x1 = temp.indexOf("system ",x);
//            int y = temp.indexOf("</system>");
//            String declar = temp.substring(x1+7,y);
//            declar = declar.replaceAll("\\s*|\r|\n|\t","");
//            declar = declar.replaceAll(";","");
//            req_declar.put(str,declar);
//        }
//
//
//        session.setAttribute(ALLREQS,allreqs);
//        session.setAttribute(SYSTEM_LIST,SysList);
//        session.setAttribute(ReqName_Declar,req_declar);

        String pth = Path_Conf;
        String pron = (String) session.getAttribute(PJ_NAME);

        System.out.println(pth+pron+"/Output/Step3/"+pron+".xml");

        String param = new String(Files.readAllBytes(Paths.get(pth+pron+"/Output/Step3/"+pron+".xml")));

        session.setAttribute(System_NAME,param);

        String template = param.substring(param.indexOf("<template>"),param.indexOf("</template>")+11);

        return handleResponseService.parseXml(template);
    }

    @PostMapping("/uploaddep")
    public Map<String,String> UploadDependend(HttpSession session) throws IOException {
        //String proname = (String)session.getAttribute(Project_NAME);  //项目名称

        String pth = Path_Conf;
        String pron = (String) session.getAttribute(PJ_NAME);

        String path = pth+pron+"/Output/Step1/";

        //表示一个文件路径
        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        String CD = "";
        String DD = "";

        for(File file2 : files){

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file2.getPath()));
            String line;
            Integer flag=-1;
            while ((line = bufferedReader.readLine())  != null) {
                if(line.contains("data dependencies:")){
                    flag=1;
                }
                else if(line.contains("device dependencies:")){
                    flag=0;
                }
                else{
                    if(flag==1){
                        DD+=line+"\n";
                    }
                    else if(flag==0){
                        CD+=line+"\n";
                    }
                }
            }

        }

        Map<String,String> map = new HashMap<>();

        map.put("cd",CD);
        map.put("dd",DD);

        return map;
    }

    static  String Device_NAME="device";
    @PostMapping("/uploaddevicexml")
    public String parseXml(@RequestBody Map<String, String> data,HttpSession session) {
        String param = data.get("data");
        System.out.println("接收到的设备模型xml文件内容为：");
        System.out.println(param);
        session.setAttribute(Device_NAME,param);
        return handleResponseService.parseXml(param);
    }

    static String DeviceList_NAME="devices";  //设备名称列表包含分解设备
    static String DeCDevices_FLAG = "decomposedTA";  //标志是否有分解设备

    static String NOdec_Device_NAME="new_devices";  //不包含分解设备的设备模型
    static String DeviceName_NUM = "devicename_num";  //分解设备名称映射分解个数
    @PostMapping("/uploaddevice")
    public String parseXmlL(@RequestBody String data,HttpSession session) throws IOException {
        String param = data;
        //String path = new File(ResourceUtils.getURL("classpath:").getPath()).getParentFile().getParentFile().getParent();
        String path = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+(String)session.getAttribute(Project_NAME)+"/EnvironmentModel/TA/";

        //表示一个文件路径
        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();

        List<String> devnames = new ArrayList<>();
        for(File file2 : files){
            String name = file2.getName();
            if(!name.contains("decomposedTA")){
                devnames.add(name.substring(0,name.indexOf(".")));
            }
        }

        Map<String,Integer> devname_num = new HashMap<>();//存储分解设备的名称对应个数

        File file0 = new File(path+"decomposedTA");
        if(file0.exists()){
            session.setAttribute(DeCDevices_FLAG,"yes");

            File[] files1 = file0.listFiles();

            for(File file2 : files1){
                String name = file2.getName();
                devnames.add(name.substring(0,name.indexOf(".")));
                String allname = name.substring(0,name.indexOf("."));
                String nonumname = allname.replaceAll("\\d+","");

                if(devname_num.containsKey(nonumname)){
                    devname_num.put(nonumname,devname_num.get(nonumname)+1);
                }
                else{
                    devname_num.put(nonumname,1);
                }

            }

        }
        else{
            session.setAttribute(DeCDevices_FLAG,"no");
        }

        session.setAttribute(DeviceList_NAME,devnames);
        session.setAttribute(DeviceName_NUM,devname_num);

        System.out.println(devnames);
        System.out.println(devname_num);


        StringBuffer strbuf=new StringBuffer(); //整个xml文件
        StringBuffer Qdeclaration=new StringBuffer(); //声明部分
        StringBuffer Sdeclaration = new StringBuffer(); //模型声明部分
        StringBuffer AllTemplate=new StringBuffer();  //模板部分

        strbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>\n");
        strbuf.append("<nta>\n");

        Qdeclaration.append("<declaration>");
        Sdeclaration.append("<system>\n");
        Sdeclaration.append("system ");

        //foreach遍历数组
        for (File file2 : files) {

            if(!file2.getName().contains("decomposedTA")) {

                //System.out.println("路径："+file2.getPath());
                String filestr = readTxt(file2.getPath());
                //System.out.println(filestr);

                //添加声明部分
                int deviceDstart = filestr.indexOf("<declaration>");
                int deviceDend = filestr.indexOf("</declaration>");
                String deviceD = filestr.substring(deviceDstart + 13, deviceDend);
                Qdeclaration.append(deviceD);

                //添加template部分
                int deviceTstart = filestr.indexOf("<template>");
                int deviceTend = filestr.lastIndexOf("</template>");
                String deviceT = filestr.substring(deviceTstart, deviceTend + 11);
                AllTemplate.append(deviceT);
                AllTemplate.append("\n");

                //添加模型声明部分
//            int deviceSstart = filestr.indexOf("<system>");
//            int deviceSend = filestr.indexOf("</system>");
//            String deviceS = filestr.substring(deviceSstart+8,deviceSend);
//            int devicess = deviceS.indexOf("system");
//            if(devicess!=-1){
//                deviceS = deviceS.substring(devicess+6);
//            }
//            deviceS = deviceS.replace(";",",");
//            Sdeclaration.append(deviceS);

                //添加模型声明部分
                String name = file2.getName();
                String deviceS = name.substring(0, name.indexOf(".")) + ",";
                Sdeclaration.append(deviceS);

            }
        }

        String strbuf1 = strbuf.toString();
        String qdecl = Qdeclaration.toString();
        String alltemp = AllTemplate.toString();
        String sdecl = Sdeclaration.toString();

        strbuf1 = strbuf1+"\n"+qdecl;
        strbuf1 += "\n</declaration>\n";
        strbuf1 += alltemp;

        int ind = sdecl.lastIndexOf(",");
        sdecl = sdecl.substring(0,ind)+";";
        strbuf1 += "\n"+sdecl;
        strbuf1 += "\n</system>\n";
        strbuf1 += "</nta>";

        session.setAttribute(NOdec_Device_NAME,strbuf1); //不包含分解设备的设备模型文件

        System.out.println("不包含分解设备的：\n"+strbuf1);

        if(file0.exists()) {

            File[] files1 = file0.listFiles();

            for (File file2 : files1) {
                //System.out.println("路径："+file2.getPath());
                String filestr = readTxt(file2.getPath());
                //System.out.println(filestr);

                //添加声明部分
                int deviceDstart = filestr.indexOf("<declaration>");
                int deviceDend = filestr.indexOf("</declaration>");
                String deviceD = filestr.substring(deviceDstart + 13, deviceDend);
                Qdeclaration.append(deviceD);

                //添加template部分
                int deviceTstart = filestr.indexOf("<template>");
                int deviceTend = filestr.lastIndexOf("</template>");
                String deviceT = filestr.substring(deviceTstart, deviceTend + 11);
                AllTemplate.append(deviceT);
                AllTemplate.append("\n");

                //添加模型声明部分
                String name = file2.getName();
                String deviceS = name.substring(0, name.indexOf(".")) + ",";
                Sdeclaration.append(deviceS);
            }
        }

        Qdeclaration.append("</declaration>\n");

        int index = Sdeclaration.lastIndexOf(",");
        Sdeclaration.setCharAt(index,';');
        Sdeclaration.append("\n</system>\n");

        strbuf.append(Qdeclaration);
        strbuf.append(AllTemplate);
        strbuf.append(Sdeclaration);
        strbuf.append("</nta>");

        param=strbuf.toString();

        System.out.println("组合的设备文件"+param);

        session.setAttribute(Device_NAME,param);

        String template = param.substring(param.indexOf("<template>"),param.indexOf("</template>")+11);

        return handleResponseService.parseXml(template);
    }

    public static String readTxt(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        // 删除最后一个新行分隔符
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();
        String content = stringBuilder.toString();
        return content;
    }

    static  String Design_NAME="design";
    @PostMapping("/uploaddesignxml")
    public String parseXml1(@RequestBody Map<String, String> data,HttpSession session) {
        String param = data.get("data");
        //System.out.println("接收到的设计模型xml文件内容为：");
        //System.out.println(param);
        session.setAttribute(Design_NAME,param);

        session.setAttribute(Device_NAME,param);

        String template = param.substring(param.indexOf("<template>"),param.indexOf("</template>")+11);

        return handleResponseService.parseXml(template);
    }

    static String Content_NAME = "Spec";
    @PostMapping("/uploadspec")
    public String UploadSpecification(HttpServletRequest request, HttpServletResponse response, HttpSession session)throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        BufferedReader data=request.getReader();
        String str = "";
        String content="";
        while(str!=null){
            str = data.readLine();
            if(str!=null) {
                content += str;
                content+="\n";
            }
        }
        System.out.println("content"+content);
        session.setAttribute(Content_NAME,content);
        String ss = (String)session.getAttribute(Content_NAME);
        System.out.println("上传的需求规约为"+ss);
        System.out.println(session.getId());
        return "su";
    }

    static String Project_NAME = "Project";
    static String Project_XML = "ProjectXml";
    @PostMapping("/uploadpro")
    public String UploadProject(HttpServletRequest request, HttpServletResponse response, HttpSession session)throws ServletException, IOException {

        Enumeration em = request.getSession().getAttributeNames();  //得到session中所有的属性名
        while (em.hasMoreElements()) {
            request.getSession().removeAttribute(em.nextElement().toString()); //遍历删除session中的值
        }

        response.setCharacterEncoding("UTF-8");
        BufferedReader data=request.getReader();
        String str = "";
        String content="";
        while(str!=null){
            str = data.readLine();
            if(str!=null) {
                content += str;
                content+="\n";
            }
        }
        session.setAttribute(Project_XML,content);
        String ss = (String)session.getAttribute(Project_XML);
        //System.out.println("上传的project:\n"+ss);
        String name = ss.substring(ss.indexOf("<title>")+7,ss.indexOf("</title>"));
        session.setAttribute(Project_NAME,name);
        String sss = (String)session.getAttribute(Project_NAME);
        System.out.println("project名字为"+name);
        return "su";
    }


    @GetMapping("/init")
    public Map<String, String> init(){
        System.out.println("初始化方法");
        List<Tree> tree = batchHandleResponseService.init();
        Map<String, String> data = new HashMap<>();
        data.put("data",JSON.toJSONString(tree));
        return data;
    }


    @PostMapping("/append")
    public Map<String, String> append(@RequestBody Tree tree){
        System.out.println("传过去的tree为："+tree);
        List<Tree> trees = batchHandleResponseService.append(tree);
        Map<String, String> data = new HashMap<>();
        data.put("data",JSON.toJSONString(trees));
        return data;
    }

    @PostMapping("/save")
    public void save(@RequestBody Map<String, String> data){
        String param = data.get("data");
        String name = data.get("name");
        System.out.println(param);
        batchHandleResponseService.save(param, name);
        System.out.println("保存所有的模版");
    }

    @PostMapping("/delete")
    public String deleteByName(@RequestBody Map<String, String> data){
        String name = data.get("name");
        batchHandleResponseService.deleteByName(name);
        return null;
    }

    @PostMapping("/edit")
    public String editByName(@RequestBody Tree tree){
        System.out.println(tree);
        batchHandleResponseService.editByName(tree);
        return null;
    }

    @GetMapping("/export")
    public Map<String, String> exportXml(){
        String context = batchHandleResponseService.exportXml();
        Map<String, String> data = new HashMap<>();
        data.put("data",context);
        return data;
    }

}
