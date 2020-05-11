package me.hantong.dailycost.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 记账记录
 *
 * @author X
 */
@Entity(foreignKeys = @ForeignKey(entity = RecordType.class, parentColumns = "id", childColumns = "record_type_id"),
        indices = {@Index(value = {"record_type_id", "time", "money", "create_time"})})
public class Record implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public BigDecimal money;

    public String remark;

    public Date time;

    @ColumnInfo(name = "create_time")
    public Date createTime;

    @ColumnInfo(name = "record_type_id")
    public int recordTypeId;
}
