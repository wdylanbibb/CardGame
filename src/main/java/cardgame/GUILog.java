package cardgame;


import java.io.File;

public class GUILog {
    public static void println(Object o) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement s = stackTrace[2];
//        String file;
//        file = new File("src\\main\\java\\" + e.getClassName().replace(".", "\\")).getAbsolutePath();
//        System.out.printf("(build.gradle:1) %s\n", /*file, e.getLineNumber(),*/ s);
        System.out.printf("%s\n\tat %s(%s:%s)\n", o, s.getClassName(), s.getFileName(), s.getLineNumber());
    }
}
