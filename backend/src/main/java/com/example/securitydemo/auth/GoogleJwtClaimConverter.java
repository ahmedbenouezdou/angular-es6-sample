package com.example.securitydemo.auth;

import org.springframework.core.convert.converter.Converter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleJwtClaimConverter implements Converter<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> convert(Map<String, Object> claims) {
        Map<String, Object> mutable = new HashMap<>(claims);
        Object hd = claims.get("hd");
        if (hd != null) {
            mutable.put("tenant", hd);
        }
        if (!mutable.containsKey("roles")) {
            mutable.put("roles", List.of("USER"));
        }
        return mutable;
    }
}
