package com.luo.file.content.replace;

import com.luo.file.content.replace.util.ContentReplace;
import com.luo.file.content.replace.util.ContentReplaceBuilder;
import com.luo.file.content.replace.util.OptionUtil;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

/**
 * 文件内容替换类
 *
 * @author xiangnan
 * date 2018/8/23 11:23
 */
public class FileContentReplace {

    public static void main(String[] args) throws ParseException {

        String[] args2 = {"-file", "zzz", "-replace", "."};
        testParser(args2);

    }

    public static void testParser(String[] args) throws ParseException {
        Options options = OptionUtil.initOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("t")) {
            System.out.println(cmd.getOptionValue("t"));
        }

        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("Options", options);



        if (cmd.hasOption("dir")) {
            System.out.println(cmd.getOptionValue("dir"));
        }
    }

    public void test() throws Exception {
        ContentReplace replace = ContentReplaceBuilder.of("content.toUpperCase()");
        System.out.println(replace.replace("hello world"));

        List<String> contentList = FileUtils.readLines(new File("G:\\FarBox\\ssm\\深入浅出Spring IoC.md"),
                "utf-8");

        for (int i = 0; i < contentList.size(); i++) {
            String content = contentList.get(i);
            if (!content.contains("![](")) {
                continue;
            }

            contentList.set(i, content + "aaa");
        }

        contentList.forEach(content -> {
            if (content.contains("![](")) {
                System.out.println(content);
            }
        });

        System.out.println(contentList.size());
    }
}
