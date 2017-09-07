package com.shanglan.pulongwan.utils;

import com.shanglan.pulongwan.entity.TopicDetail;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 对象池
 */
public class TopicDetailPoolFactory extends BasePooledObjectFactory<TopicDetail> {

    static GenericObjectPool<TopicDetail> pool = null;

    // 取得对象池工厂实例
    public synchronized static GenericObjectPool<TopicDetail> getInstance() {
        if (pool == null) {
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMaxIdle(-1);
            poolConfig.setMaxTotal(-1);
            poolConfig.setMinIdle(100);
            poolConfig.setLifo(false);
            pool = new GenericObjectPool<TopicDetail>(new TopicDetailPoolFactory(), poolConfig);
        }
        return pool;
    }

    public static TopicDetail borrowObject() throws Exception{
        return (TopicDetail) TopicDetailPoolFactory.getInstance().borrowObject();
    }

    public static void returnObject(TopicDetail topicDetail) throws Exception{
        TopicDetailPoolFactory.getInstance().returnObject(topicDetail);
    }

    public static void close() throws Exception{
        TopicDetailPoolFactory.getInstance().close();
    }

    public static void clear() throws Exception{
        TopicDetailPoolFactory.getInstance().clear();
    }

    @Override
    public TopicDetail create() throws Exception {
        return new TopicDetail();
    }

    @Override
    public PooledObject<TopicDetail> wrap(TopicDetail topicDetail) {
        return new DefaultPooledObject<>(topicDetail);
    }


    @Override
    public void destroyObject(PooledObject<TopicDetail> p) throws Exception {
        super.destroyObject(p);
    }

    @Override
    public boolean validateObject(PooledObject<TopicDetail> p) {
        return super.validateObject(p);
    }

    @Override
    public void activateObject(PooledObject<TopicDetail> p) throws Exception {
        super.activateObject(p);
    }

    @Override
    public void passivateObject(PooledObject<TopicDetail> p) throws Exception {
        super.passivateObject(p);
    }
}
