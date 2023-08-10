package com.fit.cache.rule;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存key的规则
 *
 * @author songhao
 */
public class KeyRuleHolder {

    /**
     * todo 这里要替换成配置中心取
     */
    public static final List<KeyRule> KEY_RULES = new ArrayList<>();


    /**
     * 判断该key命中了哪个rule
     */
    public static String rule(String key) {
        KeyRule keyRule = findRule(key);
        if (keyRule != null) {
            return keyRule.getKey();
        }
        return "";
    }

    public static KeyRule findRule(String key) {
        KeyRule prefix = null;
        KeyRule common = null;
        for (KeyRule keyRule : KEY_RULES) {
            if (key.equals(keyRule.getKey())) {
                return keyRule;
            }
            if ((keyRule.isPrefix() && key.startsWith(keyRule.getKey()))) {
                prefix = keyRule;
            }
            if ("*".equals(keyRule.getKey())) {
                common = keyRule;
            }
        }
        if (prefix != null) {
            return prefix;
        }
        return common;
    }

    /**
     * 判断key是否在配置的要探测的规则内
     */
    public static boolean isKeyInRule(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        //遍历该app的所有rule，找到与key匹配的rule。
        for (KeyRule keyRule : KEY_RULES) {
            if ("*".equals(keyRule.getKey()) || key.equals(keyRule.getKey()) ||
                    (keyRule.isPrefix() && key.startsWith(keyRule.getKey()))) {
                return true;
            }
        }
        return false;
    }

}
