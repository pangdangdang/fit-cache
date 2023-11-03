package com.fit.cache.qconfig;

import com.ctrip.corp.foundation.common.fit.cache.rule.KeyRule;
import com.ctrip.corp.foundation.common.plus.config.ConfigurationFunc;
import com.ctrip.corp.foundation.common.util.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

/**
 * @author sh
 */
public class QConfigDataLoader {

    public static Map<String, String> getCacheSizeMap() {
        return ConfigurationFunc.getConfigMap(ConfigConstant.CACHE_SIZE_MAP);
    }

    /**
     * 获取规则
     *
     * @return
     */
    public static List<KeyRule> getKeyRule() {
        String keyRule = ConfigurationFunc.getString(ConfigConstant.KEY_RULE);
        if (keyRule == null) {
            return null;
        }
        return JSONUtil.parse(keyRule, new TypeReference<List<KeyRule>>() {});
    }

}
