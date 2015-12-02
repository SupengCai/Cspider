/*
 Project:  scc
 Module:   scc-core
 File:     KryoRedisSerializer.java
 Modifier: oznyang
 Modified: 2013-10-11 17:37

 Copyright (c) 2013 Wisorg Ltd. All Rights Reserved.

 Copying of this document or code and giving it to others and the
 use or communication of the contents thereof, are forbidden without
 expressed authority. Offenders are liable to the payment of damages.
 All rights reserved in the event of the grant of a invention patent
 or the registration of a utility model, design or code.
 */
package com.supc.spider.support.redis;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * .
 * <p>
 *
 * @author <a href="mailto:oxsean@gmail.com">sean yang</a>
 * @version V1.0, 12-9-13
 */
public class KryoRedisSerializer<T> implements RedisSerializer<T> {

    private Kryo kryo = new Kryo();

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        byte[] buffer = new byte[2048];
        Output output = new Output(buffer);
        kryo.writeClassAndObject(output, t);
        return output.toBytes();
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        Input input = new Input(bytes);
        @SuppressWarnings("unchecked")
        T t = (T) kryo.readClassAndObject(input);
        return t;
    }
}
