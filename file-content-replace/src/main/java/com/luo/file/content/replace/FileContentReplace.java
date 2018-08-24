package com.luo.file.content.replace;

import com.luo.file.content.replace.util.OptionUtil;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.luo.file.content.replace.InputOption.REPLACE_MODE_LINE;

/**
 * 文件内容替换类
 *
 * @author xiangnan
 * date 2018/8/23 11:23
 */
public class FileContentReplace {

    public static void main(String[] args) throws ParseException {

        int lineNum = contentReplace(args);
        if (lineNum > 0) {
            System.out.println("文件替换完成，更新行数: " + lineNum);
        } else {
            System.out.println("未替换任何内容");
        }
    }

    /**
     * 文本替换，如果有替换返回true，否则返回false
     */
    private static int contentReplace(String[] args) {

        Options options = OptionUtil.initOptions();
        InputOption option = null;
        try {
            // 解析args
            option = OptionUtil.parseOption(new DefaultParser().parse(options, args));
        } catch (Exception e) {
            System.out.println("参数输入错误 :(, e=" + e.getMessage());

            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("Options", options);
            System.exit(-1);
        }

        // 文本读取
        List<String> contentList = null;
        try {
            if (option.getMode().equals(REPLACE_MODE_LINE)) {
                contentList = FileUtils.readLines(new File(option.getFile()), "utf-8");
            } else {
                contentList = Arrays.asList(FileUtils.readFileToString(new File(option.getFile()), "utf-8"));
            }
        } catch (Exception e) {
            System.out.println("文件读取错误, e=" + e.getMessage());

            HelpFormatter hf = new HelpFormatter();
            hf.printHelp("Options", options);
            System.exit(-2);
        }

        // 文本替换
        int result = 0;
        String contain = option.getContain();
        String from = option.getFrom();
        String to = option.getTo();

        System.out.println("替换条件: " + from + " -> " + to);
        for (int i = 0; i < contentList.size(); i++) {
            String oldContent = contentList.get(i);
            String content = oldContent;

            if (contain != null && !content.contains(contain)) {
                continue;
            }
            if (from != null && to != null) {
                content = content.replace(from, to);
            }

            content = option.getContentReplace().replace(content);

            contentList.set(i, content);

            if (!oldContent.equals(content)) {
                result++;
                if (option.getMode().equals(REPLACE_MODE_LINE)) {
                    // 打印替换行号和内容
                    System.out.println((i + 1) + ": " + oldContent);
                    System.out.println("    -> " + content);
                } else {
                    System.out.println(from + " -> " + to);
                }
            }
        }

        // 文本写入
        if (result > 0) {
            try {
                FileUtils.writeLines(new File(option.getFile()), contentList);
            } catch (Exception e) {
                System.out.println("文件写入出错 :(, e=" + e.getMessage());
                System.exit(-3);
            }
        }

        return result;
    }
}
