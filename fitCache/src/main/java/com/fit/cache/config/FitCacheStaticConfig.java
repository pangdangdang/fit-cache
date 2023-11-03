package com.fit.cache.tool;

import java.io.FileInputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * @author songhao
 */
public class FitCacheStaticConfig {

    private static final Logger LOG = LoggerFactory.getLogger(FitCacheStaticConfig.class);
    private static final String TITLE = "FitCacheStaticConfig";
    private static AtomicBoolean hasParse = new AtomicBoolean(false);
    private static ConcurrentHashMap<String, String> staticConfigMap;

    private static void parse() {// 创建Yaml对象
        try {
            Yaml yaml = new Yaml();
            FileInputStream file = new FileInputStream("static-config.yaml");
            staticConfigMap = yaml.load(file);
            file.close();
        } catch (Exception e) {
            LOG.error(TITLE, "FitCacheStaticConfig parse fail:{}", e);
        }
    }

    public static String getStaticConfig(String key) {
        if (!hasParse.get()) {
            parse();
        }
        if (staticConfigMap == null || !staticConfigMap.containsKey(key)) {
            return null;
        }
        return staticConfigMap.get(key);
    }

    public static int getIntConfig(String key) {
        try {
            return Integer.parseInt(getStaticConfig(key));
        } catch (Exception e) {
            return 6;
        }
    }

}
