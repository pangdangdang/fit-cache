package com.fit.cache.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.cache.tool.FitCacheStaticConfig;

/**
 * 保存key的规则
 *
 * @author songhao
 */
public class KeyRuleHolder {

    private static final Logger LOG = LoggerFactory.getLogger(KeyRuleHolder.class);
    private static final String TITLE = "KeyRuleHolder";

    private static AtomicBoolean hasKeyRule = new AtomicBoolean(false);
    private static List<KeyRule> keyRuleList = new ArrayList<>();

    private static List<KeyRule> keyRules() {
        try {
            if (hasKeyRule.get()) {
                return keyRuleList;
            }
            String value = FitCacheStaticConfig.getStaticConfig("keyRule");
            ObjectMapper objectMapper = new ObjectMapper();
            keyRuleList = objectMapper.readValue(value, new TypeReference<List<KeyRule>>() {});
            return keyRuleList;
        } catch (Exception e) {
            LOG.error(TITLE, "KeyRuleHolder keyRules fail:{}", e);
            return keyRuleList;
        }
    }

    public static KeyRule findRule(String key) {
        for (KeyRule keyRule : keyRules()) {
            if (key.equals(keyRule.getKey())) {
                return keyRule;
            }
            if ((keyRule.isPrefix() && key.startsWith(keyRule.getKey()))) {
                return keyRule;
            }
            if ("*".equals(keyRule.getKey())) {
                return keyRule;
            }
        }
        return null;
    }

}
