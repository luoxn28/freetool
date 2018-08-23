package com.luo.file.content.replace.util;

import com.luo.java.compiler.JavaStringCompiler;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 根据contentCode获取对应的ContentReplace实例
 *
 * @author xiangnan
 * date 2018/8/23 13:46
 */
public class ContentReplaceBuilder {

    public static ContentReplace of(String contentCode) throws Exception {
        if (StringUtils.isBlank(contentCode)) {
            return new ContentReplace();
        }

        String content = ContentReplace.SOURCE;
        String replaceClass = ContentReplace.class.getSimpleName() + "Inner";

        content = content.replace("public class " + ContentReplace.class.getSimpleName(),
                "public class " + replaceClass  + " extends " + ContentReplace.class.getName());
        content = content.replace("return content", "return " + contentCode);

        JavaStringCompiler compiler = new JavaStringCompiler();
        Map<String, byte[]> results = compiler.compile(replaceClass + ".java", content);
        Class<?> clazz = compiler.loadClass(ContentReplace.class.getPackage().getName() + "." + replaceClass, results);

        return  (ContentReplace) clazz.newInstance();
    }
}
