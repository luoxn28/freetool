package com.luo.file.content.replace;

import com.luo.file.content.replace.util.ContentReplace;
import lombok.Data;

/**
 * @author xiangnan
 * date 2018/8/23 16:37
 */
@Data
public class InputOption {
    public static final String REPLACE_MODE_LINE = "line";
    public static final String REPLACE_MODE_FILE = "file";

    private String file;

    private String mode = REPLACE_MODE_LINE;
    private String contain;
    private String from;
    private String to;

    private ContentReplace contentReplace = new ContentReplace();

    public void setMode(String mode) {
        assert REPLACE_MODE_FILE.equals(mode) || REPLACE_MODE_LINE.equals(mode);
        this.mode = mode;
    }
}
