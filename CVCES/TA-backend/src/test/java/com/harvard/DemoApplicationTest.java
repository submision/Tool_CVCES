package com.harvard;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.harvard.entity.JsonRootBean;
import com.harvard.entity.Tree;
import com.harvard.service.BatchHandleResponseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author liqing
 * @Description TODO
 * @Date 2022/12/25 上午1:25
 * @Version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTest {
    @Resource
    BatchHandleResponseService batchHandleResponseService;
    @Test
    public void test1() {
        String context = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<nta> \n" +
                "  <declaration>// Place global declarations here.</declaration>  \n" +
                "  <template> \n" +
                "    <name>Template</name>  \n" +
                "    <declaration>// Place local declarations here.</declaration> \n" +
                "  </template>  \n" +
                "  <system>// Place template instantiations here. Process = Template(); // List one or more processes to be composed into a system. system Process;</system>  \n" +
                "  <queries> \n" +
                "    <query> \n" +
                "      <formula/>  \n" +
                "      <comment/> \n" +
                "    </query> \n" +
                "  </queries> \n" +
                "</nta>";
        int i = context.indexOf("<template>");
        int j = context.indexOf("</template>");
        System.out.println(context.substring(i,j+11));
        String substring = context.substring(i, j + 11);
        String finalContext = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<nta> \n" +
                "  <declaration>// Place global declarations here.</declaration>  \n" +substring+"\n"+                "  <system>// Place template instantiations here. Process = Template(); // List one or more processes to be composed into a system. system Process;</system>  \n" +
                "  <queries> \n" +
                "    <query> \n" +
                "      <formula/>  \n" +
                "      <comment/> \n" +
                "    </query> \n" +
                "  </queries> \n" +
                "</nta>";
        System.out.println(finalContext);
    }
    @Test
    public void test2() {
    }
    @Test
    public void test3() {
        String str = "{\"name\":\"declaration\",\"level\":3,\"id\":\"dcfc8b046a1a4f939c6884fce5d33ea0\",\"pid\":\"7d2f0b308f64484b963e2a61ce66cc6f\",\"children\":[{\"name\":\"declaration\",\"level\":3,\"id\":\"ff77239d6e184d0ba85cecae0e3f0b5a\",\"pid\":\"dcfc8b046a1a4f939c6884fce5d33ea0\"}]}";
        Tree tree = JSON.parseObject(str, new TypeReference<Tree>() {});
        String s = batchHandleResponseService.batchParseXMl2(tree);
        System.out.println("输出的最终结果"+s);
    }
}
