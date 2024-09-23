package com.harvard.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class GenerateAutoControllerService {

    public String GAutoController(String Spec) throws IOException {
        String s = Spec;
        String Sxml="";  //最后生成xml文件字符串

        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line; //读取每行原子系统
        StringBuffer strbuf=new StringBuffer(); //整个xml文件
        StringBuffer Qdeclaration=new StringBuffer(); //声明部分
        StringBuffer AllTemplate=new StringBuffer();  //模板部分
        StringBuffer Sdeclaration = new StringBuffer(); //模型声明

        strbuf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<!DOCTYPE nta PUBLIC '-//Uppaal Team//DTD Flat System 1.1//EN' 'http://www.it.uu.se/research/group/darts/uppaal/flat-1_2.dtd'>\n");
        strbuf.append("<nta>\n");
        Qdeclaration.append("<declaration>\n");
        Sdeclaration.append("<system>\nsystem ");

        while ( (line = br.readLine()) != null ) {
            AllTemplate.append("<template>\n");

            StringBuffer Tlocation=new StringBuffer(); //location部分
            StringBuffer Ttransition=new StringBuffer();  //transition部分

            //提取交互部分，x，y坐标中间
            int x = line.indexOf("{");
            int y = line.lastIndexOf("}");
            String name = line.substring(0,x).replace(" ", "");//模板名字
            name.replace("-","_");

            Sdeclaration.append(name);
            Sdeclaration.append(",");

            String inter = line.substring(x+1,y);
            String head="";
            String ifbody="";
            String bingbody="";
            String nail="";

            if(inter.indexOf("if")!=-1){
                head = inter.substring(0,inter.indexOf("if"));
                ifbody=inter.substring(inter.indexOf("if"));
                nail = ifbody.substring(ifbody.lastIndexOf("}")+1);
            }
            else if(inter.indexOf("|")!=-1){
                head = inter.substring(0,inter.indexOf("{"));
                bingbody = inter.substring(inter.indexOf("{"));
                nail = ifbody.substring(ifbody.lastIndexOf("}")+1);
            }
            else{
                head = inter;
            }

            String[] heads=head.split(";");
            String[] ifbodys=ifbody.split("}");
            String[] bingbodys=bingbody.split("\\|");
            String[] nails=nail.split(";");

            int initx=100,inity=100;
            int count=1;

            AllTemplate.append("<name>"+name+"</name>\n");
            //添加初始状态
            Tlocation.append("<location id=\"id0\" x=\""+Integer.toString(initx)+"\" y=\""+Integer.toString(inity)+"\"></location>\n");

            HashMap<String, Integer> DeviceType = new HashMap<String,Integer>();//标记设备类型

            int flaglocation=0;
            int endlocation=0;
            int prelocation=0;

            //对交互部分进行处理
            //首先heads
            for(int i=0;i< heads.length;i++){
                if(heads[i].indexOf("[")==-1){ //如果是设备先进行类型标记
                    int ind=heads[i].lastIndexOf(" ");
                    String[] dname = heads[i].substring(ind+1).split(",");
                    String dtype = heads[i].substring(0,ind);
                    //Data Storage:1,Active Device:2,Clock:3,Sensor Device:4,Actuator Device:5
                    if(dtype=="Data Storage"){
                        for(int j=0;j<dname.length;j++){
                            DeviceType.put(dname[j],1);
                        }
                    }
                    else{
                        for(int j=0;j<dname.length;j++){
                            DeviceType.put(dname[j],2);
                        }
                    }
                }
                else{
                    //添加状态节点
                    prelocation=endlocation;
                    endlocation += 1;
                    initx = 100 + (count % 3) * 100;
                    inity = 100 + (count / 3) * 100;
                    count++;
                    Tlocation.append("<location id=\"id" + Integer.toString(endlocation) + "\" "
                            + "x=\"" + Integer.toString(initx) + "\" "
                            + "y=\"" + Integer.toString(inity) + "\"></location>\n");

                    int a = heads[i].lastIndexOf(" ");
                    int b = heads[i].lastIndexOf("[");
                    String ddname = heads[i].substring(a + 1, b);//设备名称

                    //添加迁移约束
                    Ttransition.append("<transition>\n");
                    Ttransition.append("<source ref=\"id"+Integer.toString(prelocation)+"\"/>\n" +
                            "<target ref=\"id"+Integer.toString(endlocation)+"\"/>\n");

                    if (heads[i].indexOf("sends") != -1) { //sends指令
                        int sind = heads[i].indexOf("sends");
                        int toind = heads[i].indexOf("to");
                        String instr = heads[i].substring(sind + 6, toind).replace(" ", "");

                        if (DeviceType.get(ddname) == 1) {  //指令对象是数据存储设备
                            Ttransition.append("<label kind=\"assignment\" " + "x=\"" + Integer.toString(initx)
                                    + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "=1" + "</label>\n");
                        } else {  //其他设备
                            Ttransition.append("<label kind=\"synchronisation\" " + "x=\"" + Integer.toString(initx)
                                    + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "!" + "</label>\n");
                        }
                    }
                    else {//receives指令
                        int rece = heads[i].indexOf("receives");
                        int fromind = heads[i].indexOf("from");
                        String instr = heads[i].substring(rece + 9, fromind).replace(" ", "");
                        Ttransition.append("<label kind=\"synchronisation\" " + "x=\"" + Integer.toString(initx)
                                + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "?" + "</label>\n");
                    }
                    Ttransition.append("</transition>\n");
                }
            }

            ArrayList<Integer> ifendlocations = new ArrayList<Integer>();
            if(ifbody.indexOf("if")!=-1) {//对if处理
                flaglocation = endlocation;
                int indy=(count / 3) * 100;
                int indx = 100+(count % 3) * 100;
                for (int i = 0; i < ifbodys.length; i++) {
                    String[] inss = ifbodys[i].split(";");
                    indy+=100;
                    for(int j=0;j<inss.length;j++) {
                        int lkh = inss[j].indexOf("(");
                        int rkh = inss[j].indexOf(")");
                        String sel;//条件
                        if (lkh != -1) {
                            sel = inss[j].substring(lkh + 1, rkh);
                        } else {
                            sel = "";
                        }

                        int a = inss[j].lastIndexOf(" ");
                        int b = inss[j].lastIndexOf("[");
                        String ddname = inss[j].substring(a + 1, b);//设备名称

                        //添加状态节点
                        prelocation=endlocation;
                        endlocation += 1;
                        initx = indx+j*100;
                        inity = indy;
                        count++;
                        Tlocation.append("<location id=\"id" + Integer.toString(endlocation) + "\" "
                                + "x=\"" + Integer.toString(initx) + "\" "
                                + "y=\"" + Integer.toString(inity) + "\"></location>\n");

                        //添加迁移约束
                        Ttransition.append("<transition>\n");
                        Ttransition.append("<source ref=\"id" + Integer.toString(flaglocation) + "\"/>\n");
                        Ttransition.append("<target ref=\"id" + Integer.toString(endlocation) + "\"/\n>");
                        if(sel!=""){
                            Ttransition.append("<label kind=\"guard\" " + "x=\"" + Integer.toString(initx)
                                    + "\" y=\"" + Integer.toString(inity) + "\">" + sel + "</label>\n");
                        }

                        if (inss[j].indexOf("sends") != -1) { //sends指令
                            int sind = inss[j].indexOf("sends");
                            int toind = inss[j].indexOf("to");
                            String instr = inss[j].substring(sind + 6, toind).replace(" ", "");
                            if (DeviceType.get(ddname) == 1) {  //指令对象是数据存储设备
                                Ttransition.append("<label kind=\"assignment\" " + "x=\"" + Integer.toString(initx)
                                        + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "=1" + "</label>\n");
                            }
                            else {  //其他设备
                                Ttransition.append("<label kind=\"synchronisation\" " + "x=\"" + Integer.toString(initx)
                                        + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "!" + "</label>\n");
                            }
                        }
                        else {//receives指令
                            int rece = inss[j].indexOf("receives");
                            int fromind = inss[j].indexOf("from");
                            String instr = inss[j].substring(rece + 9, fromind).replace(" ", "");
                            Ttransition.append("<label kind=\"synchronisation\" " + "x=\"" + Integer.toString(initx)
                                    + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "?" + "</label>\n");
                        }
                        Ttransition.append("</transition>\n");
                    }
                    ifendlocations.add(endlocation);
                }
            }

            ArrayList<Integer> bendlocations = new ArrayList<Integer>();
            if(bingbodys.length>1){
                flaglocation = endlocation;
                int indy=0;
                for(int i = 0;i< bingbodys.length;i++){
                    int n = bingbodys[i].indexOf("{");
                    int m = bingbodys[i].indexOf("}");
                    String[] inss = bingbodys[i].substring(n+1,m).split(";");
                    indy+=100;
                    for(int j=0;j<inss.length;j++){
                        int a = inss[j].lastIndexOf(" ");
                        int b = inss[j].lastIndexOf("[");
                        String ddname = inss[j].substring(a + 1, b);//设备名称

                        //添加状态节点
                        prelocation=endlocation;
                        endlocation += 1;
                        initx = 100 + (count % 3) * 100;
                        inity = indy;
                        count++;
                        int xx;
                        if(j==0){
                            xx=flaglocation;
                        }
                        else{
                            xx=prelocation;
                        }
                        Tlocation.append("<location id=\"id" + Integer.toString(endlocation) + "\" "
                                + "x=\"" + Integer.toString(initx) + "\" "
                                + "y=\"" + Integer.toString(inity) + "\"></location>\n");

                        Ttransition.append("<transition>\n");
                        Ttransition.append("<source ref=\"id"+Integer.toString(xx)+"\"/>\n" +
                                "<target ref=\"id"+Integer.toString(endlocation)+"\"/>\n");

                        if (inss[j].indexOf("sends") != -1) { //sends指令
                            int sind = inss[j].indexOf("sends");
                            int toind = inss[j].indexOf("to");
                            String instr = inss[j].substring(sind + 6, toind).replace(" ", "");
                            if (DeviceType.get(ddname) == 1) {  //指令对象是数据存储设备
                                Ttransition.append("<label kind=\"assignment\" " + "x=\"" + Integer.toString(initx)
                                        + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "=1" + "</label>\n");
                            }
                            else {  //其他设备
                                Ttransition.append("<label kind=\"synchronisation\" " + "x=\"" + Integer.toString(initx)
                                        + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "!" + "</label>\n");
                            }
                        }
                        else {//receives指令
                            int rece = inss[j].indexOf("receives");
                            int fromind = inss[j].indexOf("from");
                            String instr = inss[j].substring(rece + 9, fromind).replace(" ", "");
                            Ttransition.append("<label kind=\"synchronisation\" " + "x=\"" + Integer.toString(initx)
                                    + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "?" + "</label>\n");
                        }
                        Ttransition.append("</transition>\n");
                    }
                    bendlocations.add(endlocation);
                }
            }

            if(nail.indexOf("[")!=-1){
                if(!ifendlocations.isEmpty()){
                    //添加状态节点
                    prelocation=endlocation;
                    endlocation += 1;
                    initx = 100 + (count % 3) * 100;
                    inity = 100 + (count / 3) * 100;
                    count++;
                    Tlocation.append("<location id=\"id" + Integer.toString(endlocation) + "\" "
                            + "x=\"" + Integer.toString(initx) + "\" "
                            + "y=\"" + Integer.toString(inity) + "\"></location>\n");

                    for(int i=0;i<ifendlocations.size();i++){
                        Ttransition.append("<transition>\n");
                        Ttransition.append("<source ref=\"id"+Integer.toString(ifendlocations.get(i))+"\"/>\n" +
                                "<target ref=\"id"+Integer.toString(endlocation)+"\"/>\n");
                        Ttransition.append("</transition>\n");
                    }
                }
                else if(!bendlocations.isEmpty()){
                    //添加状态节点
                    prelocation=endlocation;
                    endlocation += 1;
                    initx = 100 + (count % 3) * 100;
                    inity = 100 + (count / 3) * 100;
                    count++;
                    Tlocation.append("<location id=\"id" + Integer.toString(endlocation) + "\" "
                            + "x=\"" + Integer.toString(initx) + "\" "
                            + "y=\"" + Integer.toString(inity) + "\"></location>\n");

                    for(int i=0;i<bendlocations.size();i++){
                        Ttransition.append("<transition>\n");
                        Ttransition.append("<source ref=\"id"+Integer.toString(bendlocations.get(i))+"\"/>\n" +
                                "<target ref=\"id"+Integer.toString(endlocation)+"\"/>\n");
                        Ttransition.append("</transition>\n");
                    }
                }

                for(int i=0;i< nails.length;i++){
                    //添加状态节点
                    prelocation=endlocation;
                    endlocation += 1;
                    initx = 100 + (count % 3) * 100;
                    inity = 100 + (count / 3) * 100;
                    count++;
                    Tlocation.append("<location id=\"id" + Integer.toString(endlocation) + "\" "
                            + "x=\"" + Integer.toString(initx) + "\" "
                            + "y=\"" + Integer.toString(inity) + "\"></location>\n");

                    int a = nails[i].lastIndexOf(" ");
                    int b = nails[i].lastIndexOf("[");
                    String ddname = nails[i].substring(a + 1, b);//设备名称

                    //添加迁移约束
                    Ttransition.append("<transition>\n");
                    Ttransition.append("<source ref=\"id"+Integer.toString(prelocation)+"\"/>\n" +
                            "<target ref=\"id"+Integer.toString(endlocation)+"\"/>\n");
                    if (nails[i].indexOf("sends") != -1) { //sends指令
                        int sind = nails[i].indexOf("sends");
                        int toind = nails[i].indexOf("to");
                        String instr = nails[i].substring(sind + 6, toind).replace(" ", "");
                        if (DeviceType.get(ddname) == 1) {  //指令对象是数据存储设备
                            Ttransition.append("<label kind=\"assignment\" " + "x=\"" + Integer.toString(initx)
                                    + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "=1" + "</label>\n");
                        } else {  //其他设备
                            Ttransition.append("<label kind=\"synchronisation\" " + "x=\"" + Integer.toString(initx)
                                    + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "!" + "</label>\n");
                        }
                    }
                    else {//receives指令
                        int rece = nails[i].indexOf("receives");
                        int fromind = nails[i].indexOf("from");
                        String instr = nails[i].substring(rece + 9, fromind).replace(" ", "");
                        Ttransition.append("<label kind=\"synchronisation\" " + "x=\"" + Integer.toString(initx)
                                + "\" y=\"" + Integer.toString(inity) + "\">" + instr + "?" + "</label>\n");
                    }
                    Ttransition.append("</transition>\n");
                }
                Ttransition.append("<transition>\n");
                Ttransition.append("<source ref=\"id"+Integer.toString(endlocation)+"\"/>\n" +
                        "<target ref=\"id0\"/>\n");
                Ttransition.append("</transition>\n");
            }
            else{
                if(!ifendlocations.isEmpty()){
                    for(int i=0;i<ifendlocations.size();i++){
                        Ttransition.append("<transition>\n");
                        Ttransition.append("<source ref=\"id"+Integer.toString(ifendlocations.get(i))+"\"/>\n" +
                                "<target ref=\"id0\"/>\n");
                        Ttransition.append("</transition>\n");
                    }
                }
                else if(!bendlocations.isEmpty()){
                    for(int i=0;i<bendlocations.size();i++){
                        Ttransition.append("<transition>\n");
                        Ttransition.append("<source ref=\"id"+Integer.toString(bendlocations.get(i))+"\"/>\n" +
                                "<target ref=\"id0\"/>\n");
                        Ttransition.append("</transition>\n");
                    }
                }
                else{
                    Ttransition.append("<transition>\n");
                    Ttransition.append("<source ref=\"id"+Integer.toString(endlocation)+"\"/>\n" +
                            "<target ref=\"id0\"/>\n");
                    Ttransition.append("</transition>\n");
                }
            }

            Tlocation.append("<init ref=\"id0\"/>\n");
            AllTemplate.append(Tlocation);
            AllTemplate.append(Ttransition);
            AllTemplate.append("</template>\n");
        }

        Qdeclaration.append("</declaration>\n");

        int index = Sdeclaration.lastIndexOf(",");
        Sdeclaration.setCharAt(index,';');
        Sdeclaration.append("</system>");

        strbuf.append(Qdeclaration);
        strbuf.append(AllTemplate);
        strbuf.append(Sdeclaration);
        strbuf.append("</nta>");
        Sxml=strbuf.toString();
        return Sxml;
    }

}
