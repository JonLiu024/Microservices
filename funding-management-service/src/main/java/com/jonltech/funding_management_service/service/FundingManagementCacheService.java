package com.jonltech.funding_management_service.service;

import com.jonltech.funding_management_service.model.FundingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class FundingManagementCacheService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    private static final String FUNDING_STATUS_PREFIX = "wildlife_funding_status_";

    public void cacheFundingStatus(String wildlifeId, FundingStatus fundingStatus) {
        redisTemplate.opsForValue().set(FUNDING_STATUS_PREFIX + wildlifeId, fundingStatus, 10, TimeUnit.MINUTES);
    }

    public FundingStatus getFundingStatusFromCache(String wildlifeId) {
        return (FundingStatus) redisTemplate.opsForValue().get(FUNDING_STATUS_PREFIX + wildlifeId);
    }

    public void removeFundingStatusFromCache(String wildlifeId) {
        redisTemplate.delete(FUNDING_STATUS_PREFIX + wildlifeId);
    }
}