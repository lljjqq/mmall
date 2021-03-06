package cn.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 使用guava实现tokenCache以及LRU算法本地缓存
 * @Author WJ
 * @Date 2018/8/23 10:59
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    /**
     * 声明静态内存块
     * initialCapacity(1000)：设置缓存的初始化容量
     * maximumSize(10000)：缓存的最大容量，当超过此容量时，guawa的cache会使用LRU算法来移除缓存项
     * expireAfterAccess(12, TimeUnit.HOURS)：设置有效期 12小时
     */
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现,当调用get取值的时候,如果key没有对应的值,就调用这个方法进行加载.
                @Override
                public String load(String s) throws Exception {
                    //防止空指针异常，使用字符串null null.equals()会报空指针异常
                    return "null";
                }
            });

    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    public static String getKey(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            logger.error("localCache get error", e);
        }
        return null;
    }
}
