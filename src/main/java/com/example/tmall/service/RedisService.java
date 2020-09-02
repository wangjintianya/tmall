package com.example.tmall.service;

public interface RedisService {
    /**
     * 存储数据
     *
     * @param key   String
     * @param value String
     */
    void set(String key, String value);

    /**
     * 获取数据
     *
     * @param key String
     * @return String
     */
    String get(String key);

    /**
     * 设置超期时间
     *
     * @param key    String
     * @param expire long
     * @return boolean
     */
    boolean expire(String key, long expire);

    /**
     * 删除数据
     *
     * @param key String
     */
    void remove(String key);

    /**
     * 自增操作
     *
     * @param key   String
     * @param delta long
     * @return Long
     */
    Long increment(String key, long delta);
}
