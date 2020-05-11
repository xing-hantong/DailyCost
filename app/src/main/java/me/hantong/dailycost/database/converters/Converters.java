package me.hantong.dailycost.database.converters;

import androidx.room.TypeConverter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据库类型转换器
 *
 * @author X
 */
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static BigDecimal stringToBig(int intDecimal) {
        return new BigDecimal(intDecimal);
    }

    @TypeConverter
    public static int bigToString(BigDecimal bigDecimal) {
        return bigDecimal.intValue();
    }
}
