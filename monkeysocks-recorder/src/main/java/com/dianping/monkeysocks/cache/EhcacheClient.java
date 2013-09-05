package com.dianping.monkeysocks.cache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author yihua.huang@dianping.com
 * @date Dec 19, 2012
 */
public class EhcacheClient {

    private Logger logger = Logger.getLogger(EhcacheClient.class);

    private static volatile CacheManager manager;

    private static final String CACHE_NAME = "PACKAGE";

    private static final String CACHE_CONF = "ehcache.xml";

    private static final String CLEAR = "clear_cache";

    private static final String DUMP = "dump_cache";

    private static final String STAT = "stat_cache";

    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private static volatile EhcacheClient instance;

    public static EhcacheClient instance() {
        if (instance == null) {
            synchronized (EhcacheClient.class) {
                if (instance == null) {
                    instance = new EhcacheClient();
                    instance.init();
                }

            }
        }
        return instance;
    }

    public void init() {
        InputStream inputStream = null;
        try {
            inputStream = EhcacheClient.class.getClassLoader().getResourceAsStream(CACHE_CONF);
            if (manager == null) {
                synchronized (EhcacheClient.class) {
                    if (manager == null) {
                        manager = new CacheManager(inputStream);
                        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                            @Override
                            public void run() {
                                if (logger.isDebugEnabled()) {
                                    logger.debug("start to flush cache to disk");
                                }
                                try {
                                    Cache cache = manager.getCache(CACHE_NAME);
                                    cache.flush();
                                } catch (Exception e) {
                                    logger.warn("flush cache error!", e);
                                }
                            }
                        }, 1, 1, TimeUnit.MINUTES);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("init ehcache error!", e);
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                logger.warn("close error", e);
            }
            inputStream = null;
        }
    }

    public <T> T get(String key) {
        if (manager == null || key == null) {
            return null;
        }
        if (manager == null) {
            return null;
        }
        Cache cache = manager.getCache(CACHE_NAME);
        Element element = cache.get(key);
        if (element == null) {
            return null;
        }
        T value = (T) element.getObjectValue();
        return value;
    }

    /**
     * (non-Jsdoc)
     *
     * @see com.dianping.mail.postman.cache.service.CacheService#set(String,
     *      Object, int)
     */
    public <T> boolean set(String key, T value, int expireTime) {
        if (key == null || value == null) {
            throw new IllegalArgumentException(
                    "key and value should not be null");
        }
        if (manager == null) {
            return false;
        }
        Cache cache = manager.getCache(CACHE_NAME);
        Element element = new Element(key, value, Boolean.FALSE, 0, expireTime);
        cache.put(element);
        return true;
    }

    public void flush() {
        try {
            Cache cache = manager.getCache(CACHE_NAME);
            cache.flush();
            manager.shutdown();
            logger.info("flush cache to disk success!");
        } catch (Exception e) {
            logger.warn("flush cache error!", e);
        }
    }

}
