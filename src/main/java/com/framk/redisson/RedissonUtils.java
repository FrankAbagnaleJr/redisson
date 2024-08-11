package com.framk.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.security.Key;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @auther: frank gee
 * @date: 2024-8-11 21:24 周日
 * @project_name: redissionDemo
 * @version: 1.0
 * @description TODO
 */
public class RedissonUtils {
    public static RedissonClient client;

    static{
        //1.实例化配置对象
        Config config = new Config();
        //2.设置redis服务器地址和密码,如果没有密码就就不用setPassword
        config.useSingleServer().setAddress("redis://110.40.192.128:6379").setPassword("123456");
        //3.实例化客户端对象
        client = Redisson.create(config);
    }

    //String类型
    //添加字符串类型
    public static void setStr(String key,Object val) {
        client.getBucket(key).set(val);
    }

    public static void setStr(String key, Object val, long seconds) {
        client.getBucket(key).set(val,seconds, TimeUnit.SECONDS);
    }

    public static void addIncr(String key) {
        int i = (int) client.getBucket(key).get();
        i++;
        client.getBucket(key).set(i);
    }

     //BitMap
    public static void setBitMap(String key,int index) {
        client.getBitSet(key).set(index, true);
    }

    //List
    public static void setList(String key,Object val) {
        client.getList(key).add(val);
    }

    public static void setList(String key, int index, Object val) {
        client.getList(key).add(index, val);
    }

    public static void setList(String key, List<Object> vals) {
        client.getList(key).addAll(vals);
    }

    public static Object getList(String key, int index) {
        return client.getList(key).get(index);
    }

    public static Object getList(String key) {
        return client.getList(key).remove(0);
    }

    //获取bitmap1的数量
    public static long getBitMap(String key) {
        return client.getBitSet(key).cardinality();
    }

    public static void expire(String key, long seconds) {
        client.getKeys().expire(key, seconds, TimeUnit.SECONDS);
    }

    public static long ttl(String key) {
        return client.getKeys().remainTimeToLive(key);
    }

    public static void setSet(String key, String val) {
        client.getSet(key).add(val);
    }

    //获取字符串类型
    public static Object getStr(String key) {
        return client.getBucket(key).get();
    }

    //Set类型
    public static Set<Object> getSet(String key) {
        return client.getSet(key).readAll();
    }

    public static int getSize(String key) {
        return client.getSet(key).size();
    }

    public static boolean exists(String key, Object val) {
        return client.getSet(key).contains(val);
    }

    //Hash类型
    public static void setHash(String key, String field, Object obj) {
        client.getMap(key).put(field, obj);
    }

    public static Object getHash(String key, String field) {
        return client.getMap(key).get(field);
    }

    public static void delField(String key, String field) {
        client.getMap(key).remove(field);
    }

    //删除
    public static void delSet(String key, Object val) {
        client.getSet(key).remove(val);
    }

    public static void delKey(String... keys) {
        client.getKeys().delete(keys);
    }

    //校验key
    public static boolean existsField(String key, String field) {
        return client.getMap(key).containsKey(field);
    }

    public static boolean checkKey(String key) {
        return client.getKeys().countExists(key) > 0;
    }

    public static String getKey(String key) {
        Iterator<String> keys = client.getKeys().getKeys().iterator();
        while (keys.hasNext()) {
            String k = keys.next();
            if (k.startsWith(key)) {
                return k;
            }
        }
        return null;
    }


    /**
     * 基于Redisson的分布式锁
     * @param key 分布式锁的key
     * @return 红锁
     */
    public static RLock getLock(String key) {
        return client.getLock(key);
    }

}
