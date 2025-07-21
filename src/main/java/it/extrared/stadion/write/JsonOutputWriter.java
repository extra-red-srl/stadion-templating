package it.extrared.stadion.write;

import static it.extrared.stadion.utils.CommonUtils.defFormatDate;
import static it.extrared.stadion.utils.CommonUtils.defFormatTemporalAccessor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class JsonOutputWriter implements OutputWriter {

    private JsonGenerator jGen;

    private Map<String, Object> globalProperties;

    public JsonOutputWriter(OutputStream outputStream, Map<String, Object> globalProperties)
            throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        this.jGen = jsonFactory.createGenerator(outputStream);
        this.globalProperties = globalProperties;
    }

    @Override
    public void startElement(Map<String, Object> extraData) throws IOException {
        jGen.writeStartObject();
    }

    @Override
    public void startCollection(Map<String, Object> extraData) throws IOException {
        jGen.writeStartArray();
    }

    @Override
    public void endElement(Map<String, Object> extraData) throws IOException {
        jGen.writeEndObject();
    }

    @Override
    public void endCollection(Map<String, Object> extraData) throws IOException {
        jGen.writeEndArray();
    }

    @Override
    public void writeFieldName(String fieldName, Map<String, Object> extraData) throws IOException {
        jGen.writeFieldName(fieldName);
    }

    @Override
    public void close() throws IOException {
        jGen.close();
    }

    @Override
    public void writeValue(Object value, Map<String, Object> extraData) throws IOException {
        if (value == null) {
            jGen.writeNull();
            return;
        }
        Class<?> type = value.getClass();
        if (String.class.isAssignableFrom(type)) {
            jGen.writeString(value.toString());
        } else if (Number.class.isAssignableFrom(type)) {
            writeNumeric(type, value);
        } else if (Boolean.class.isAssignableFrom(type)) {
            jGen.writeBoolean(Boolean.class.cast(value));
        } else if (Character.class.isAssignableFrom(type)) {
            jGen.writeString(String.valueOf(Character.class.cast(value)));
        } else if (Byte.class.isAssignableFrom(type)) {
            jGen.writeBinary(new byte[] {(byte) value});
        } else if (byte[].class.isAssignableFrom(type)) {
            jGen.writeBinary(byte[].class.cast(value));
        } else if (Date.class.isAssignableFrom(type)) {
            jGen.writeString(defFormatDate(Date.class.cast(value)));
        } else if (TemporalAccessor.class.isAssignableFrom(type)) {
            jGen.writeString(defFormatTemporalAccessor(TemporalAccessor.class.cast(value)));
        } else if (type.isArray()) {
            writeArray(value, type, extraData);
        } else if (Collection.class.isAssignableFrom(type)) {
            writeIterator(((Collection<?>) value).iterator(), extraData);
        } else if (Iterator.class.isAssignableFrom(type)) {
            writeIterator((Iterator<?>) value, extraData);
        } else {
            try {
                jGen.writeObject(value);
            } catch (Exception e) {
                String message =
                        ("Unable to write type %s in a json document. "
                                        + "Check if your accessor function (jpath,xpath, property path) is correct. "
                                        + "Original exeception is: ")
                                .formatted(type.getSimpleName());
                throw new IOException(message, e);
            }
        }
    }

    private void writeNumeric(Class<?> type, Object value) throws IOException {
        if (Integer.class.isAssignableFrom(type)) {
            jGen.writeNumber(Integer.class.cast(value));
        } else if (Double.class.isAssignableFrom(type)) {
            jGen.writeNumber(Double.class.cast(value));
        } else if (Float.class.isAssignableFrom(type)) {
            jGen.writeNumber(Float.class.cast(value));
        } else if (Long.class.isAssignableFrom(type)) {
            jGen.writeNumber(Long.class.cast(value));
        } else if (Short.class.isAssignableFrom(type)) {
            jGen.writeNumber(Short.class.cast(value));
        } else if (BigDecimal.class.isAssignableFrom(type)) {
            jGen.writeNumber(BigDecimal.class.cast(value));
        } else if (BigInteger.class.isAssignableFrom(type)) {
            jGen.writeNumber(BigInteger.class.cast(value));
        } else {
            throw new RuntimeException("Unrecognized number type %s".formatted(value));
        }
    }

    private void writeArray(Object value, Class<?> type, Map<String, Object> extraData)
            throws IOException {
        jGen.writeStartArray();
        if (Object[].class.isAssignableFrom(type)) {
            Object[] values = (Object[]) value;
            for (Object val : values) writeValue(val, extraData);
        } else if (short[].class.isAssignableFrom(type)) {
            short[] values = (short[]) value;
            for (short val : values) writeValue(val, extraData);
        } else if (int[].class.isAssignableFrom(type)) {
            int[] values = (int[]) value;
            for (int val : values) writeValue(val, extraData);
        } else if (long[].class.isAssignableFrom(type)) {
            long[] values = (long[]) value;
            for (long val : values) writeValue(val, extraData);
        } else if (float[].class.isAssignableFrom(type)) {
            float[] values = (float[]) value;
            for (float val : values) writeValue(val, extraData);
        } else if (double[].class.isAssignableFrom(type)) {
            double[] values = (double[]) value;
            for (double val : values) writeValue(val, extraData);
        } else if (char[].class.isAssignableFrom(type)) {
            char[] values = (char[]) value;
            for (char val : values) writeValue(val, extraData);
        } else if (boolean[].class.isAssignableFrom(type)) {
            boolean[] values = (boolean[]) value;
            for (boolean val : values) writeValue(val, extraData);
        } else {
            throw new RuntimeException(
                    "Array type %s not recognized".formatted(type.getSimpleName()));
        }
        jGen.writeEndArray();
    }

    private void writeIterator(Iterator<?> values, Map<String, Object> extraData)
            throws IOException {
        jGen.writeStartArray();
        while (values.hasNext()) writeValue(values.next(), extraData);
        jGen.writeEndArray();
    }
}
