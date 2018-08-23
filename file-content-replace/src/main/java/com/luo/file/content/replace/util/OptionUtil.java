package com.luo.file.content.replace.util;

import org.apache.commons.cli.Options;

/**
 * @author xiangnan
 * date 2018/8/23 16:26
 */
public class OptionUtil {

    public static Options initOptions() {
        Options options = new Options();
        options.addRequiredOption("file", null, true, "文件路径");

        options.addOption("mode", true, "每行替换还是文件整体替换，取值范围 line | file，默认line");
        options.addOption("from", true, "被替换的文本");
        options.addOption("to", true, "待替换的文本");

        options.addOption("replace", true, "自定义java替换代码");

        return options;
    }
}
