package com.amazon.kinesis.streaming.agent.processing.processors;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.net.InetAddress;
import java.util.List;

import com.amazon.kinesis.streaming.agent.processing.exceptions.DataConversionException;
import com.amazon.kinesis.streaming.agent.processing.interfaces.IDataConverter;
import com.amazon.kinesis.streaming.agent.config.Configuration;

/**
 * This example converter just wraps resulting data into brackets.
 *
 * For example, input line "hello there" will after conversion will look like "{hello there}"
 *
 * Created by myltik on 12/01/2016.
 */
public class AddCustomDataConverter implements IDataConverter {

    private static String APPEND_VALUES = "appendValues";
    private final List<String> addValues;

    public AddCustomDataConverter(Configuration config) {
        addValues = config.readList(APPEND_VALUES, String.class);
    }

    @Override
    public ByteBuffer convert(ByteBuffer data) throws DataConversionException {
        final byte[] dataBin = new byte[data.remaining()];
        data.get(dataBin);

        StringBuilder sb = new StringBuilder(2 + dataBin.length);
        sb.append(new String(dataBin, 0,(dataBin.length-1),StandardCharsets.UTF_8));
        sb.append(" ");
        try{
            sb.append(InetAddress.getLocalHost().getHostName());
        }
        catch(UnknownHostException e){
            throw new DataConversionException(e);
        }
        for (int i = 0; i < addValues.size(); i++) {
            sb.append(" ");
            sb.append(addValues.get(i));
        }


        return ByteBuffer.wrap(sb.toString().getBytes());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
