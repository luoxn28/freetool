package com.luo.file.content.replace.util;

import com.luo.file.content.replace.InputOption;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 * @author xiangnan
 * date 2018/8/23 16:26
 */
public class OptionUtil {

    public static String file = "file";
    public static String mode = "mode";
    public static String contain = "contain";
    public static String from = "from";
    public static String to = "to";
    public static String replace = "replace";

    public static Options initOptions() {
        Options options = new Options();
        options.addRequiredOption(file, null, true, "文件路径");

        options.addOption(mode, true, "每行替换还是文件整体替换，取值范围 line | file，默认line");
        options.addOption(contain, true, "包含contain文本的才会参与替换操作，相当于一个过滤条件");
        options.addOption(from, true, "被替换的文本");
        options.addOption(to, true, "待替换的文本");

        options.addOption(replace, true, "自定义java替换代码");

        return options;
    }

    public static InputOption parseOption(CommandLine cmd) throws Exception {
        InputOption inputOption = new InputOption();

        if (cmd.hasOption(file)) {
            inputOption.setFile(cmd.getOptionValue(file));
        }
        if (cmd.hasOption(mode)) {
            inputOption.setMode(cmd.getOptionValue(mode));
        }
        if (cmd.hasOption(contain)) {
            inputOption.setContain(cmd.getOptionValue(contain));
        }
        if (cmd.hasOption(from)) {
            inputOption.setFrom(cmd.getOptionValue(from));
        }
        if (cmd.hasOption(to)) {
            inputOption.setTo(cmd.getOptionValue(to));
        }
        if (cmd.hasOption(replace)) {
            inputOption.setContentReplace(ContentReplaceBuilder.of(cmd.getOptionValue(replace)));
        }

        assert inputOption.getFile() != null;
        return inputOption;
    }
}
