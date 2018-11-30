### 从git仓库源码中查找是否包含某个字符串的工程

1. 引入hutool依赖
```xml
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>4.1.19</version>
</dependency>
```

2. 在当前目录`user.dir`下添加`gits.txt`文件，内容是各个git工程名，格式如下：
```
project-aaa
project-bbb
...
```

3.运行代码
```java
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 从git仓库源码中查找是否包含符合条件的服务
 */
public class FindXxxFromGitSource {

    /**
     * linux window 系统文件路径分隔符
     */
    private static char systemSeparator = FileUtil.isWindows() ? '\\' : '/';

    private static String gitPathPrefix = "git@192.168.1.xxx:xxx/";

    /**
     * 文件后缀，非空
     */
    static String fileSuffix = ".java";

    /**
     * 待查找字符串
     */
    static String findStr = "xxx";

    public static void main(String[] args) {
        String gitsPath = System.getProperty("user.dir") + systemSeparator + "gits.txt";
        if (!FileUtil.exist(gitsPath)) {
            System.out.println("输入文件不存在: " + gitsPath);
            return;
        }

        String gitDir = System.getProperty("user.dir") + systemSeparator + "temp";
        if (FileUtil.exist(gitDir)) {
            System.out.println("临时目录已存在: " + gitDir);
        }

        List<String> result = new ArrayList<>();
        FileUtil.readUtf8Lines(gitsPath).stream().filter(StrUtil::isNotBlank)
                .distinct().forEach(gitName -> {
            // git clone
            String savePath = gitDir + systemSeparator + gitName + systemSeparator;
            if (!FileUtil.exist(savePath)) {
                System.out.print(RuntimeUtil.execForStr(
                        "git clone " + gitPathPrefix + gitName + ".git " + savePath));
            } else {
                System.out.print(gitName + ": " + JSONUtil.toJsonStr(RuntimeUtil.execForStr("git -C " + savePath + " pull")));
            }

            List<String> fileList = getAllFilePath(Arrays.asList(savePath), fileSuffix);
            for (String file : fileList) {
                if (FileUtil.readUtf8String(file).contains(findStr)) {
                    // 包含某个字符串
                    result.add(gitName + " : " +  StrUtil.subAfter(file, systemSeparator, true));
                }
            }
        });

        System.out.println("包含符合查找条件的服务有:");
        result.forEach(System.out::println);
    }

    /**
     * 递归获取文件夹下所有符合后缀名称的文件
     */
    static List<String> getAllFilePath(List<String> pathList, String suffix) {
        List<String> result = new ArrayList<>();
        List<String> other = new ArrayList<>();
        pathList.forEach(path -> Arrays.stream(FileUtil.ls(path)).forEach(file -> {
            if (file.isFile()) {
                if (StrUtil.isNotBlank(suffix) && file.getAbsolutePath().endsWith(suffix)) {
                    result.add(file.getAbsolutePath());
                }
            } else {
                other.add(file.getAbsolutePath());
            }
        }));

        if (CollectionUtil.isNotEmpty(other)) {
          result.addAll(getAllFilePath(other, suffix));
        }
        return result;
    }

}
```
