package org.elasticsearch.bootstrap;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created by yangkongshi on 2017/12/4.
 */
public class JarHell {
    private JarHell() {
    }

    public static void checkJarHell() {
    }

    public static void checkJarHell(URL[] urls) {
    }

    public static void checkJarHell(Consumer<String> output) {
    }

    public static void checkVersionFormat(String targetVersion) {
    }

    public static void checkJavaVersion(String resource, String targetVersion) {
    }

    public static Set<URL> parseClassPath() {return new HashSet<>();}
}
