package com.shanglan.pulongwan.utils;

import com.shanglan.pulongwan.entity.Field;
import com.shanglan.pulongwan.entity.TopicDetail;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 对象池
 */
public class FieldPoolFactory extends BasePooledObjectFactory<Field> {

    static GenericObjectPool<Field> pool = null;

    // 取得对象池工厂实例
    public synchronized static GenericObjectPool<Field> getInstance() {
        if (pool == null) {
            GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
            poolConfig.setMaxIdle(-1);
            poolConfig.setMaxTotal(-1);
            poolConfig.setMinIdle(100);
            poolConfig.setLifo(false);
            pool = new GenericObjectPool<Field>(new FieldPoolFactory(), poolConfig);
        }
        return pool;
    }

    public static Field borrowObject() throws Exception{
        return (Field) FieldPoolFactory.getInstance().borrowObject();
    }

    public static void returnObject(Field field) throws Exception{
        FieldPoolFactory.getInstance().returnObject(field);
    }

    public static void close() throws Exception{
        FieldPoolFactory.getInstance().close();
    }

    public static void clear() throws Exception{
        FieldPoolFactory.getInstance().clear();
    }

    @Override
    public Field create() throws Exception {
        return new Field();
    }

    @Override
    public PooledObject<Field> wrap(Field field) {
        return new DefaultPooledObject<>(field);
    }
}
