package com.harvard.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class GenerateSystemModelService {

    public String Sumstate(String systemxml,String proname) throws IOException {
        String s = systemxml;
        String num="";

        ArrayList<Integer> nums = new ArrayList<Integer>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line; //读取每行原子系统
        int index = -1;
        int nn = 0;
        BigInteger summ = BigInteger.valueOf(1);

        while ((line = br.readLine()) != null) {
            if (line.indexOf("<template>") >= 0) {
                index = 1;
            }
            if (index == 1) {
                if (line.indexOf("</location>") >= 0) {
                    nn++;
                }
            }
            if (line.indexOf("</template>") >= 0) {
                index = -1;
                nums.add(nn);
                nn = 0;
            }
        }
        for (Integer item : nums) {
            summ = summ.multiply(BigInteger.valueOf(item));
        }
        double d = summ.doubleValue();
        System.out.println(d);

        num=Double.toString(d);

        return num;
    }

    public String Setdeclare(String Systemxml, String content){
        String sysxml = Systemxml;
        String declare = content;
        int declareDstart = sysxml.indexOf("<declaration>");
        int declareDend = sysxml.indexOf("</declaration>");
        sysxml = sysxml.substring(0,declareDstart+13)+"\n"+declare+sysxml.substring(declareDend);
        return sysxml;
    }

    public String GSysdeclare(String Systemxml){
        String sysxml = Systemxml;
        String declare = "";
        int declareDstart = sysxml.indexOf("<declaration>");
        int declareDend = sysxml.indexOf("</declaration>");
        declare = sysxml.substring(declareDstart+13,declareDend);
        return declare;
    }

    public Map<String,String> GSystemList(String dev, String des, String pf, List<String> reqnames, Map<String,String> sToallname){
        String pd = pf.substring(pf.indexOf("<Reference>"),pf.indexOf("</Reference>"));
        String pdd = pf.substring(pf.indexOf("<Constraint>"),pf.indexOf("</Constraint>"));
        Map<String,String> req_template = new HashMap<>();

        //保存所有被分解的相关的需求名称
        String allreqs = "";

        for(int i=0;i< reqnames.size();i++){
            String reqname = reqnames.get(i);

            String dlistname = ""; //存储需求约束的领域名称

            //在需求引用中查找与需求相关的领域名称
            int index = pd.indexOf("reference_to=\""+reqname+"\"");
            while(index >= 0){
                int x = pd.substring(0,index).lastIndexOf("reference_from=\"")+16;
                int y = pd.indexOf("\"",x);
                String devname = pd.substring(x,y);
                if(!dlistname.contains(devname)){
                    dlistname += devname+",";
                }
                index = pd.indexOf("reference_to=\""+reqname+"\"",index+1);
            }

            int index1= pd.indexOf("reference_from=\""+reqname+"\"");
            while(index1 >= 0){
                int x = pd.indexOf("reference_to=\"",index1);
                int y = pd.indexOf("\"",x+14);
                String devname = pd.substring(x+14,y);
                if(!dlistname.contains(devname)){
                    dlistname += devname+",";
                }
                index1 = pd.indexOf("reference_from=\""+reqname+"\"",index1+1);
            }

            //在需求约束中查找与需求相关的领域名称
            int index2 = pdd.indexOf("constraint_from=\""+reqname+"\"");
            while(index2 >= 0){
                int x = pdd.indexOf("constraint_to=\"",index2);
                int y = pdd.indexOf("\"",x+15);
                String devname = pdd.substring(x+15,y);
                if(!dlistname.contains(devname)){
                    dlistname += devname+",";
                }
                index2 = pdd.indexOf("constraint_from=\""+reqname+"\"",index2+1);
            }

            int index3 = pdd.indexOf("constraint_to=\""+reqname+"\"");
            while(index3 >= 0){
                int x = pdd.substring(0,index3).lastIndexOf("constraint_from=\"")+17;
                int y = pdd.indexOf("\"",x);
                String devname = pdd.substring(x,y);
                if(!dlistname.contains(devname)){
                    dlistname += devname+",";
                }
                index3 = pdd.indexOf("constraint_to=\""+reqname+"\"",index3+1);
            }



            //stbuf组合模型
            StringBuffer stbuf = new StringBuffer();
            stbuf.append("<nta>\n");



            //提取需求对应设计模型
            int reqs = des.indexOf(reqname+"</name>");
            reqs = des.substring(0,reqs).lastIndexOf("<template>");
            int reqe = des.indexOf("</template>",reqs)+11;
            String reqtemplate = des.substring(reqs,reqe);
            stbuf.append(reqtemplate);
            stbuf.append("\n");

            //添加模型声明
            String sys = reqname;

            String[] dlist = dlistname.split(",");

            //提取需求相关领域模型
            for(int j=0;j<dlist.length;j++){
                String domainname = dlist[j];

                //设备模型简称
                if(dev.indexOf(domainname+"</name>")>0){
                    sys += ","+sToallname.get(domainname);

                    int doms = dev.indexOf(domainname+"</name>");
                    doms = dev.substring(0,doms).lastIndexOf("<template>");
                    int dome = dev.indexOf("</template>",doms)+11;
                    String domtemplate = dev.substring(doms,dome);
                    stbuf.append(domtemplate);
                    stbuf.append("\n");
                }
                //设备模型全称
                else if(dev.indexOf(sToallname.get(domainname)+"</name>")>0){
                    sys += ","+sToallname.get(domainname);
                    int doms = dev.indexOf(sToallname.get(domainname)+"</name>");
                    doms = dev.substring(0,doms).lastIndexOf("<template>");
                    int dome = dev.indexOf("</template>",doms)+11;
                    String domtemplate = dev.substring(doms,dome);
                    stbuf.append(domtemplate);
                    stbuf.append("\n");
                }
                //数据存储域全称
                else if(des.indexOf(sToallname.get(domainname)+"</name>")>0){
                    sys += ","+sToallname.get(domainname);
                    int doms = des.indexOf(sToallname.get(domainname)+"</name>");
                    doms = des.substring(0,doms).lastIndexOf("<template>");
                    int dome = des.indexOf("</template>",doms)+11;
                    String domtemplate = des.substring(doms,dome);
                    stbuf.append(domtemplate);
                    stbuf.append("\n");
                }
                //数据存储域简称
                else{

                        sys += "," + sToallname.get(domainname);
                        int doms = des.indexOf(domainname + "</name>");
                        doms = des.substring(0, doms).lastIndexOf("<template>");
                        int dome = des.indexOf("</template>", doms) + 11;
                        String domtemplate = des.substring(doms, dome);
                        stbuf.append(domtemplate);
                        stbuf.append("\n");

                }
            }

            stbuf.append("<system>system "+sys+";"+"</system>\n");
            stbuf.append("</nta>");

            req_template.put(reqname,stbuf.toString());

//            //队列存放分解的系统模型
//            Queue<String> Sysqueue = new LinkedList<>();
//            Sysqueue.add(stbuf.toString());
//            int count = 1;

//            //判断有无分解的设备模型
//            for(String dname : device_num.keySet()){
//                if(sys.contains(dname)){
//                    int nums = device_num.get(dname);//记录设备被分解的个数
//                    int queuelen = Sysqueue.size(); //当前队列的长度
//
//                    for(int n = 0; n<queuelen; n++){
//                        String system = Sysqueue.remove();
//                        for(int ii=1; ii<=nums; ii++){
//                            int x = system.indexOf(dname+"</name>");
//                            int y = system.indexOf("</template>",x);
//                            int x1 = system.substring(0,x).lastIndexOf("<template>");
//
//                            //截取分解的设备模型
//                            int xx = dev.indexOf(dname+Integer.toString(ii)+"</name>");
//                            int yy = dev.indexOf("</template>",xx);
//                            int xx1 = dev.substring(0,xx).lastIndexOf("<template>");
//                            String ddev = dev.substring(xx1,yy+11);
//
//                            //替换分解的设备模型
//                            String newsystem = system.substring(0,x1)+ddev+system.substring(y+11);
//
//                            //修改模型声明
//                            int ind1 = newsystem.indexOf("<system>");
//                            int ind2 = newsystem.indexOf(dname,ind1);
//                            int ind3 = newsystem.indexOf(",",ind2);
//                            if(ind3<0){
//                                ind3 = newsystem.indexOf(";",ind2);
//                            }
//                            newsystem = newsystem.substring(0,ind2)+dname+Integer.toString(ii)+newsystem.substring(ind3);
//
//                            //存储组合的系统模型
//                            req_template.put(reqname+Integer.toString(count),newsystem);
//                            count++;
//
//                            if(!allreqs.contains(reqname)) {
//                                allreqs += reqname + ",";
//                            }
//
//                            Sysqueue.add(newsystem);
//                        }
//                    }
//                }
//            }

        }

        req_template.put("ALLREQS",allreqs);

        return req_template;
    }

    public String GSystemModel(String devicemodel, String designmodel) throws IOException {
        String Systemxml="";  //最后生成xml文件字符串
        String device = devicemodel;
        String design = designmodel;

        StringBuffer strbuf=new StringBuffer(); //整个xml文件
        StringBuffer Qdeclaration=new StringBuffer(); //声明部分
        StringBuffer Sdeclaration = new StringBuffer(); //模型声明部分
        StringBuffer AllTemplate=new StringBuffer();  //模板部分

        strbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>\n");
        strbuf.append("<nta>\n");

        Qdeclaration.append("<declaration>\n");

        int deviceDstart = device.indexOf("<declaration>");
        int deviceDend = device.indexOf("</declaration>");
        String deviceD = device.substring(deviceDstart+13,deviceDend);
        Qdeclaration.append(deviceD);

        int designDstart = design.indexOf("<declaration>");
        int designDend = design.indexOf("</declaration>");
        String designD = design.substring(designDstart+13,designDend);
        Qdeclaration.append(designD);


        Qdeclaration.append("</declaration>\n");


        int deviceTstart = device.indexOf("<template>");
        int deviceTend = device.lastIndexOf("</template>");
        String deviceT = device.substring(deviceTstart,deviceTend+11);
        AllTemplate.append(deviceT);
        AllTemplate.append("\n");

        int designTstart = design.indexOf("<template>");
        int designTend = design.lastIndexOf("</template>");
        String designT = design.substring(designTstart,designTend+11);
        AllTemplate.append(designT);
        AllTemplate.append("\n");


        Sdeclaration.append("<system>\n");
        Sdeclaration.append("system ");

        int deviceSstart = device.indexOf("<system>");
        int deviceSend = device.indexOf("</system>");
        String deviceS = device.substring(deviceSstart+8,deviceSend);
        int devicess = deviceS.indexOf("system");
        if(devicess!=-1){
            deviceS = deviceS.substring(devicess+6);
        }
        deviceS = deviceS.replace(";",",");
        Sdeclaration.append(deviceS);

        int designSstart = design.indexOf("<system>");
        int designSend = design.indexOf("</system>");
        String designS = design.substring(designSstart+8,designSend);
        int designss = designS.indexOf("system");
        if(designss != -1) {
            designS = designS.substring(designss + 6);
        }
        designS = designS.replace(";",",");
        Sdeclaration.append(designS);


        int index = Sdeclaration.lastIndexOf(",");
        Sdeclaration.setCharAt(index,';');
        Sdeclaration.append("\n</system>\n");


        strbuf.append(Qdeclaration);
        strbuf.append(AllTemplate);
        strbuf.append(Sdeclaration);
        strbuf.append("<queries>\n" +
                "<query>\n" +
                "<formula></formula>\n" +
                "<comment></comment>\n" +
                "</query>\n" +
                "</queries>\n");
        strbuf.append("</nta>");
        Systemxml = strbuf.toString();

        return Systemxml;
    }

    public Map<String,String> ModifyModelDeclaration(String pp, String system, String proname, List<String> devnames) throws IOException {

        Map<String,String> modires = new HashMap<>();
        String newsys = system;

        //获取全部模型声明
        int syss = system.indexOf("<system>");
        int sysss = system.indexOf("system ",syss);
        int syse = system.indexOf("</system>");
        String declar = system.substring(sysss+7,syse);
        declar = declar.replaceAll("\\s*|\r|\n|\t","");
        declar = declar.replaceAll(";","");

        if(pp.contains("not deadlock")){
            if(pp.contains("A")){
                modires.put("AND","yes");
            }
            else{
                modires.put("AND","no");
            }

            modires.put("flag","yes");
            modires.put("system",newsys);
            modires.put("declar",declar);
            return modires;
        }
        else{
            modires.put("AND","no");
        }

        List<String> name = new ArrayList<>();
        String[] rn = pp.split(" ");
        for(int i=0;i<rn.length;i++){
            if(rn[i].contains(".")){
                name.add(rn[i].substring(0,rn[i].indexOf(".")));
            }
        }
        //System.out.println(devnames);
        int flag=0;
        for(String str : name){
            if(!devnames.contains(str)){
                flag=1;
            }
        }
        if(flag==0){
            newsys = system;
            modires.put("flag","yes");
            modires.put("system",newsys);
            modires.put("declar",declar);
            return modires;
        }
        String path = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/RequirementsModel";
        String CDpath = path+"/ControlDependency.txt";
        String cd = new String(Files.readAllBytes(Paths.get(CDpath)));//读取控制依赖
        String DDpath = path+"/DataDependency.txt";
        String dd = new String(Files.readAllBytes(Paths.get(DDpath)));//读取数据依赖
        String Spath = path+"/Sort.txt";
        String Sort = new String(Files.readAllBytes(Paths.get(Spath)));//读取依赖序列

        String sort = Sort.substring(cd.indexOf("Sort:")+6);
        String content =  cd;

        String ModelDecla="";
        for(String str : name){
            if(devnames.contains(str)){
                if(!ModelDecla.contains(str)){
                    ModelDecla = ModelDecla + str + ",";
                }
                continue;
            }

            //add
            if(!sort.contains(str+":")){
                if(!ModelDecla.contains(str)){
                    ModelDecla = ModelDecla + str + ",";
                }
                continue;
            }
            int ind = sort.indexOf(str+":");
            int inde = sort.indexOf(":",ind);
            int indee = sort.indexOf("\n",inde);
            String reqdes = "";
            if(indee<0){
                reqdes = sort.substring(inde+1);
            }
            else{
                reqdes = sort.substring(inde+1,indee);
            }
            String[] reqss = reqdes.split(",");

            for(String req : reqss){
                String[] reqq = req.split("->");
                for(String re : reqq){
                    if(!ModelDecla.contains(re)){
                        ModelDecla = ModelDecla + re + ",";
                    }
                }
            }

//            String ssort = sort.substring(0,inde);
//            String[] sorts = ssort.split("<-");
//            List<String> CDdepends = new ArrayList<>();
//            CDdepends.add(str);
//            for(String sstr : sorts){
//                if(sstr.contains("{")){
//                    int x = sstr.indexOf("{");
//                    int y = sstr.indexOf("}");
//                    String[] ns = sstr.substring(x+1,y).split(",");
//                    for(String sss : ns){
//                        CDdepends.add(sss);
//                    }
//                }
//            }
//            for(int i=0; i <CDdepends.size();i++){
//                String srr = CDdepends.get(i);
//                String ss = content;
//                if(ss.contains(srr+ " ->")) {
//                    if (!ModelDecla.contains(srr)) {
//                        ModelDecla += srr + ",";
//                    }
//                    while (ss.contains(srr + " ->")) {
//                        int y = ss.indexOf("because of {", ss.indexOf(srr + " ->"));
//                        int yy = ss.indexOf("}", y);
//                        String[] yyy = ss.substring(y + 12, yy).split(",");
//                        for (String s : yyy) {
//                            if (!ModelDecla.contains(s)) {
//                                ModelDecla += s + ",";
//                            }
//                        }
//                        ss = ss.substring(yy + 1);
//                    }
//                }
//                else{
//                    if (!ModelDecla.contains(srr)) {
//                        ModelDecla += srr + ",";
//                    }
//                }
//            }
            //add

//            if(dd.contains(str+" ->")){
//                int start = dd.indexOf(str+" ->");
//
//                int reqs = dd.indexOf("{",start);
//                int reqe = dd.indexOf("}",reqs);
//                String reqname = dd.substring(reqs+1,reqe);
//                String[] reqnames = reqname.split(",");
//                for(int i=0;i<reqnames.length;i++){
//                    if(!ModelDecla.contains(reqnames[i])){
//                        ModelDecla = ModelDecla + reqnames[i] + ",";
//                    }
//                }
//            }
            System.out.println("每次修改声明："+ModelDecla);
        }

        String[] CDdepends = ModelDecla.split(",");
        for(String srr : CDdepends){
            if(srr==null){
                continue;
            }
            String ss = content;
            while (ss.contains(srr + " ->")) {
                int y = ss.indexOf("because of {", ss.indexOf(srr + " ->"));
                int yy = ss.indexOf("}", y);
                String[] yyy = ss.substring(y + 12, yy).split(",");
                for (String s : yyy) {
                    if (!ModelDecla.contains(s)) {
                        ModelDecla += s + ",";
                    }
                }
                ss = ss.substring(yy + 1);
            }
        }

        System.out.println("最终声明为："+ModelDecla);

        int last = ModelDecla.lastIndexOf(",");
        modires.put("declar",ModelDecla.substring(0,last));
        ModelDecla = "system "+ModelDecla.substring(0,last)+";";

        newsys = system;
        newsys = newsys.substring(0,newsys.indexOf("<system>")+8)+ModelDecla+newsys.substring(newsys.indexOf("</system>"));
        modires.put("system",newsys);

        modires.put("flag","no");
        return modires;
    }


    public String GetQuery(String system, String designname, String proname) throws IOException {
        String sys = system;
        String des = designname;

        String path = "C:/软件需求与设计模型的形式化验证工具/SRDVT/PF/UserProject/test/"+proname+"/RequirementsModel";

        File file = new File(path);
        //用数组把文件夹下的文件存起来
        File[] files = file.listFiles();
        String QD = "";

        for (File f : files) {
            // 判断元素是不是文件夹，是文件夹就重复调用此方法（递归）
            if (f.isDirectory()) {
                continue;
            }else {
                // 判断文件是不是以.txt结尾的文件，并且count++（注意：文件要显示扩展名）
                if (f.getName().endsWith(".xml") && f.getName().contains(des)) {
                    String Qpath = path+"/"+f.getName();
                    QD = new String(Files.readAllBytes(Paths.get(Qpath)));//读取情景图
                    break;
                }
            }
        }

        String PFpath = path+"/ProblemDiagram.xml";
        String PF = new String(Files.readAllBytes(Paths.get(PFpath)));//读取问题图

        String querylist = "";

        if(QD.contains("<BehEnable>")) {
            int Qbehs = QD.indexOf("<BehEnable>");
            int Qbehe = QD.indexOf("</BehEnable>",Qbehs);

            String str = QD.substring(Qbehs,Qbehe);

            while(str.contains("to_no=")){
                int inds = str.indexOf("to_no=\"");
                int inde = str.indexOf("\"",inds+7);
                String num = str.substring(inds+7,inde);

                int pinds = PF.indexOf("phenomenon_no=\""+num+"\"");
                int pinde = PF.indexOf("phenomenon_name=\"",pinds);
                int pindee = PF.indexOf("\"",pinde+17);
                String pname = PF.substring(pinde+17,pindee);

                String tname = FindTemplate(sys,pname);

                querylist += "E<> "+tname+"."+pname+",";

                str = str.substring(inds+7);
            }

            sys = AddQuery(sys,querylist);
            System.out.println("生成的查询性质："+querylist);
            //System.out.println(sys);
        }
        else{
            String que = "E<> not deadlock";
            que = que.replace("<","&lt;");
            que = que.replace(">","&gt;");
            que = "<query>\n" +
                    "\t\t\t<formula>"+que+"</formula>\n" +
                    "\t\t\t<comment></comment>\n" +
                    "\t\t</query>\n";

            sys = sys.substring(0,sys.indexOf("<queries>")+9)+"\n"+que+sys.substring(sys.indexOf("</queries>"));
        }

        return sys;
    }

    public String ModifyDeclar(String system, String declar){
        String sys = system;
        String dec = declar;

        int inds = sys.indexOf("<system>");
        int inde = sys.indexOf("</system>",inds);

        dec = dec.substring(0,dec.lastIndexOf(","));

        sys = sys.substring(0,inds+8)+"system "+dec+";"+sys.substring(inde);

        return sys;
    }


    public String CheckSysmodel(String system, String proname) throws IOException, InterruptedException {

        TimeUnit.SECONDS.sleep(2);//秒

        String result = system;
        String res = "";

        byte[] arr = result.getBytes();
        String papa = "C:/软件需求与设计模型的形式化验证工具/SRDVT/TArun/TAxml/"+proname+"localize.xml";

        Path path = Paths.get(papa);
        Files.write(path, arr);

        String cmd = "cmd /c C:/软件需求与设计模型的形式化验证工具/SRDVT/TArun/verifyta.exe -u -O std C:/软件需求与设计模型的形式化验证工具/SRDVT/TArun/TAxml/"+proname+"localize.xml";

        Process ps = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String cmdres = sb.toString();
        System.out.println("定位运行结果："+cmdres);

        if(cmdres.contains("Out of memory.")){
            int inds = cmdres.lastIndexOf("Verifying formula ");
            int inde = cmdres.indexOf(" ",inds+18);
            String num = cmdres.substring(inds+18,inde);
            String dname = FindDev(result,Integer.parseInt(num));
            res += dname+",";
        }
        else {
            if (!cmdres.contains("Formula is NOT satisfied.")) {
                res = "true";
            }
            else {
                res = "flase";
//                String cc = cmdres;
//                while(cc.contains("Formula is NOT satisfied.")) {
////                    int errs = cc.indexOf("Formula is NOT satisfied.");
////                    String ss = cc.substring(0, errs);
////                    int x = ss.lastIndexOf("Verifying formula ");
////                    int y = ss.indexOf(" ", x + 18);
////                    String num = ss.substring(x + 18, y);
////
////                    String dname = FindDev(result, Integer.parseInt(num));
////
////                    res += dname + ",";
////
////                    cc = cc.substring(errs + 26);
//
//                }
            }
        }

        return res;
    }

    public String FindDev(String system, int num){
        String str = system;
        str = system.substring(system.indexOf("<queries>"),system.indexOf("</queries>"));
        int numm = 0;
        String n = "";
        while(str.contains("<query>")){
            numm++;
            if(numm == num) {
                int inds = str.indexOf("<formula>");
                int inde = str.indexOf("</formula>",inds);
                String name = str.substring(inds+9,inde);
                name = name.substring(name.indexOf(" ")+1,name.indexOf("."));
                n = name;
                break;
            }
            int ind = str.indexOf("</query>");
            str  = str.substring(ind+8);
        }
        return n;
    }

    public String AEGetQuery(String system, String proper){
        String sys = system;
        String str = proper;
        str = str.replace("<","&lt;");
        str = str.replace(">","&gt;");
        String que = "";
        que += "<query>\n" +
                "\t\t\t<formula>"+str+"</formula>\n" +
                "\t\t\t<comment></comment>\n" +
                "\t\t</query>\n";

        sys = sys.substring(0,sys.indexOf("<queries>")+9)+"\n"+que+sys.substring(sys.indexOf("</queries>"));

        return sys;
    }

    public String AddQuery(String system, String querylist){
        String sys = system;
        String[] qll = querylist.split(",");

        String que = "";

        for (String str : qll) {
            str = str.replace("<","&lt;");
            str = str.replace(">","&gt;");
            que += "<query>\n" +
                    "\t\t\t<formula>"+str+"</formula>\n" +
                    "\t\t\t<comment></comment>\n" +
                    "\t\t</query>\n";
        }

        sys = sys.substring(0,sys.indexOf("<queries>")+9)+"\n"+que+sys.substring(sys.indexOf("</queries>"));

        return sys;
    }

    public String FindTemplate(String system, String name){
        String sys = system;
        String state = name;

        String res = "null";

        if(sys.contains(state+"</name>")){
            int s = sys.indexOf(">"+state+"</name>");
            String str = sys.substring(0,s);
            //System.out.println(str);
            int ss = str.lastIndexOf("<template>");
            //System.out.println(ss);
            int ns = str.indexOf("</name>",ss);

            String tname = str.substring(ss+10,ns);
            int nss = tname.indexOf(">");
            res = tname.substring(nss+1);

        }

        return res;
    }

    public String Checkquery(String pp, String system, String pth, String pj,String num) throws IOException, InterruptedException {
        String query = pp;
        String sys = system;

        //xml文件对<>敏感需要替换字符
        query = query.replace("<","&lt;");
        query = query.replace(">","&gt;");



        int x = system.indexOf("<queries>");
        int y = system.indexOf("</queries>",x);
        String result = system.substring(0,x+9)+"\n"+
                "\t\t<query>\n" +
                "\t\t\t<formula>"+query+"</formula>\n" +
                "\t\t\t<comment></comment>\n" +
                "\t\t</query>"+system.substring(y);

        int count = 1;//记录要验证性质在系统模型文件中的序号
        int start = result.indexOf("<queries>")+9;
        int end = result.lastIndexOf("</queries>");
        sys = result.substring(start,end);

        while(sys.contains("<query>")){

            start = sys.indexOf("<query>");
            end = sys.indexOf("</query>");
            String str = sys.substring(start,end);
            if(str.contains(query)){
                break;
            }
            count++;
            sys = sys.substring(end+8);
        }

        //将系统模型文件存入验证脚本文件夹下
        byte[] arr = result.getBytes();
        String papa = pth+"TArun/TAxml/"+pj+num+".xml";

        Path path = Paths.get(papa);
        Files.write(path, arr);
        //System.out.println("保存到的文件夹"+papa);

        //执行验证脚本命令
        String cmd = "cmd /c "+pth+"TArun/verifyta.exe -u -O std "+pth+"TArun/TAxml/"+pj+num+".xml";

        //Runtime.getRuntime().exec(cmd);

        Process ps = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String cmdres = sb.toString();
        System.out.println("运行结果："+cmdres);

        String res=Getres(count,cmdres);

        byte[] cmdr = cmdres.getBytes();
        String papa1 = pth+"TArun/TAresult/"+pj+num+".txt";
        String ppa1 = pth+"Project/"+pj+"/Output/Step3/"+pj+num+".txt";
        Path pathh = Paths.get(papa1);
        Path ptt = Paths.get(ppa1);
        Files.write(pathh, cmdr);
        Files.write(ptt,cmdr);

        return res;

    }

    public String Checkproperty(String pp, String system,String proname) throws IOException, InterruptedException {
        String query = pp;
        String sys = system;

        //xml文件对<>敏感需要替换字符
        query = query.replace("<","&lt;");
        query = query.replace(">","&gt;");



        int x = system.indexOf("<queries>");
        int y = system.indexOf("</queries>",x);
        String result = system.substring(0,x+9)+"\n"+
                "\t\t<query>\n" +
                "\t\t\t<formula>"+query+"</formula>\n" +
                "\t\t\t<comment></comment>\n" +
                "\t\t</query>"+system.substring(y);

        int count = 1;//记录要验证性质在系统模型文件中的序号
        int start = result.indexOf("<queries>")+9;
        int end = result.lastIndexOf("</queries>");
        sys = result.substring(start,end);

        while(sys.contains("<query>")){

            start = sys.indexOf("<query>");
            end = sys.indexOf("</query>");
            String str = sys.substring(start,end);
            if(str.contains(query)){
                break;
            }
            count++;
            sys = sys.substring(end+8);
        }


        //将系统模型文件存入验证脚本文件夹下
        byte[] arr = result.getBytes();
        String papa = "C:/软件需求与设计模型的形式化验证工具/SRDVT/TArun/TAxml/"+proname+".xml";

        Path path = Paths.get(papa);
        Files.write(path, arr);
        //System.out.println("保存到的文件夹"+papa);

        //执行验证脚本命令
        String cmd = "cmd /c C:/软件需求与设计模型的形式化验证工具/SRDVT/TArun/verifyta.exe -u -O std C:/软件需求与设计模型的形式化验证工具/SRDVT/TArun/TAxml/"+proname+".xml";

        //Runtime.getRuntime().exec(cmd);

        Process ps = Runtime.getRuntime().exec(cmd);
        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        String cmdres = sb.toString();
        System.out.println("运行结果："+cmdres);

        String res=Getres(count,cmdres);

        byte[] cmdr = cmdres.getBytes();
        String papa1 = "C:/软件需求与设计模型的形式化验证工具/SRDVT/TArun/TAresult/"+proname+".txt";
        Path pathh = Paths.get(papa1);
        Files.write(pathh, cmdr);

        return res;
    }

    public String Getres(int count,String cmdres) throws IOException, InterruptedException {

        TimeUnit.SECONDS.sleep(2);//秒

        String content = cmdres;

        String index = "formula "+Integer.toString(count);
        System.out.println("性质序号："+index);
        String res = "meiyou";
        //System.out.println("content:"+content);
        if(content.contains(index)){
            String sub = content.substring(content.indexOf(index));
            int x = sub.indexOf("Formula is ");
            int y = sub.indexOf(".",x);
            String rr = sub.substring(x,y);
            System.out.println("性质为："+rr);
            if(rr.contains("NOT")){
                res = "no";
            }
            else {
                res = "yes";
            }
            int states = sub.indexOf("States explored : ",y);
            int statee = sub.indexOf(" states",states);
            String num  = sub.substring(states+18,statee);
            res += ","+num;

            int runts = sub.indexOf("CPU user time used : ",statee);
            int runte = sub.indexOf(" ms",runts);
            String runtime = sub.substring(runts+21,runte);
            res += ","+runtime;

        }
        return res;
    }

    public String Localizeproperty(String localize, String pro, String property){
        StringBuffer strbuf=new StringBuffer(); //整个xml文件
        String query = property;
        query = query.replace("<","&lt;");
        query = query.replace(">","&gt;");

        if(pro.contains("<ctl>"+query+"</ctl>")){

            int x = pro.indexOf("<ctl>"+query+"</ctl>");
            int y = pro.indexOf("</property>",x);
            String pree = pro.substring(0,y);
            int z = pree.lastIndexOf("\n");
            String pre = pro.substring(0,z+1);
            String end = pro.substring(z+1);

            int xxx = pre.indexOf("<localize>",x);

            if(xxx>=0){
                int m = pre.indexOf("<localize>",x);
                int n = pre.indexOf("</localize>",m);
                pre = pre.substring(0,m+10)+localize+pre.substring(n);
                strbuf.append(pre);
                strbuf.append(end);
            }
            else{
                strbuf.append(pre);
                String ss = "\t\t\t<localize>"+localize+"</localize>\n";
                strbuf.append(ss);
                strbuf.append(end);
            }

        }
        else{
            strbuf.append(pro);
        }

        return strbuf.toString();
    }

    public String Getproperty(String name, String pro, String property, Map<String,String> res, String declar) throws InterruptedException {

        StringBuffer strbuf=new StringBuffer(); //整个xml文件
        String query = property;
        query = query.replace("<","&lt;");
        query = query.replace(">","&gt;");

        if(pro==null){
            strbuf.append("<project>\n");
            strbuf.append("\t<title>"+name+"</title>\n");
            strbuf.append("\t<properties>\n");

            strbuf.append("\t\t<property>\n");
            strbuf.append("\t\t\t<ctl>"+query+"</ctl>\n");

            if(res.get("RES").contains("meiyou")){
                strbuf.append("\t\t\t<result>"+res.get("RR")+"</result>\n");
            }
            else if(res.get("RES").contains("yes")){
                strbuf.append("\t\t\t<result>"+"通过，"+res.get("RR")+"\n"+res.get("dec")+"</result>\n");
                strbuf.append("\t\t\t<states>"+res.get("states")+"</states>\n");
                strbuf.append("\t\t\t<time>"+res.get("time")+"</time>\n");
                strbuf.append("\t\t\t<model>"+declar+"</model>\n");
            }
            else{
                strbuf.append("\t\t\t<result>"+"不通过，"+res.get("RR")+"\n"+res.get("dec")+"</result>\n");
                strbuf.append("\t\t\t<states>"+res.get("states")+"</states>\n");
                strbuf.append("\t\t\t<time>"+res.get("time")+"</time>\n");
                strbuf.append("\t\t\t<model>"+declar+"</model>\n");
            }

            strbuf.append("\t\t</property>\n");

            strbuf.append("\t</properties>\n");
            strbuf.append("</project>");
        }
        else if(pro.contains(query)){
            int x = pro.indexOf("<ctl>"+query+"</ctl>");
            int y = pro.indexOf("</ctl>",x)+7;
            String pre = pro.substring(0,y);
            strbuf.append(pre);

            if(res.get("RES").contains("meiyou")){
                strbuf.append("\t\t\t<result>"+res.get("RR")+"</result>\n");
            }
            else if(res.get("RES").contains("yes")){
                strbuf.append("\t\t\t<result>"+"通过，"+res.get("RR")+"\n"+res.get("dec")+"</result>\n");
                strbuf.append("\t\t\t<states>"+res.get("states")+"</states>\n");
                strbuf.append("\t\t\t<time>"+res.get("time")+"</time>\n");
                strbuf.append("\t\t\t<model>"+declar+"</model>\n");
            }
            else{
                strbuf.append("\t\t\t<result>"+"不通过，"+res.get("RR")+"\n"+res.get("dec")+"</result>\n");
                strbuf.append("\t\t\t<states>"+res.get("states")+"</states>\n");
                strbuf.append("\t\t\t<time>"+res.get("time")+"</time>\n");
                strbuf.append("\t\t\t<model>"+declar+"</model>\n");
            }

            int z = pro.indexOf("</model>",y)+9;
            String end = pro.substring(z);
            strbuf.append(end);
        }
        else{
            String pre = pro.substring(0,pro.lastIndexOf("</property>")+11);
            strbuf.append(pre);

            strbuf.append("\n\t\t<property>\n");
            strbuf.append("\t\t\t<ctl>"+query+"</ctl>\n");

            if(res.get("RES").contains("meiyou")){
                strbuf.append("\t\t\t<result>"+res.get("RR")+"</result>\n");
            }
            else if(res.get("RES").contains("yes")){
                strbuf.append("\t\t\t<result>"+"通过，"+res.get("RR")+"\n"+res.get("dec")+"</result>\n");
                strbuf.append("\t\t\t<states>"+res.get("states")+"</states>\n");
                strbuf.append("\t\t\t<time>"+res.get("time")+"</time>\n");
                strbuf.append("\t\t\t<model>"+declar+"</model>\n");
            }
            else{
                strbuf.append("\t\t\t<result>"+"不通过，"+res.get("RR")+"\n"+res.get("dec")+"</result>\n");
                strbuf.append("\t\t\t<states>"+res.get("states")+"</states>\n");
                strbuf.append("\t\t\t<time>"+res.get("time")+"</time>\n");
                strbuf.append("\t\t\t<model>"+declar+"</model>\n");
            }

            strbuf.append("\t\t</property>\n");

            strbuf.append("\t");
            String end = pro.substring(pro.lastIndexOf("</properties>"));
            strbuf.append(end);
        }
        return strbuf.toString();
    }

    public String Getspace(String pro, String num){
        StringBuffer strbuf=new StringBuffer(); //整个xml文件
        String pre = pro.substring(0,pro.lastIndexOf("</properties>")+13);
        strbuf.append(pre);

        strbuf.append("\n\t<statespace>"+num+"</statespace>\n");

        strbuf.append("</project>");

        return strbuf.toString();
    }

}
