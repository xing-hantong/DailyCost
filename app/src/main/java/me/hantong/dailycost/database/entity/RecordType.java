package me.hantong.dailycost.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * 记账类型
 *
 * @author X
 */
@Entity(indices = {@Index(value = {"type", "ranking", "state"})})
public class RecordType implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    /**
     * 图片 name（本地mipmap）
     */
    @ColumnInfo(name = "img_name")
    public String imgName;

    @Ignore
    public static int TYPE_OUTLAY = 0;
    @Ignore
    public static int TYPE_INCOME = 1;
    /**
     * 类型
     * 0：支出
     * 1：收入
     *
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    public int type;
    /**
     * 排序
     */
    public long ranking;
    @Ignore
    public static int STATE_NORMAL = 0;
    @Ignore
    public static int STATE_DELETED = 1;
    /**
     * 状态
     * 0：正常
     * 1：已删除
     *
     * @see RecordType#STATE_NORMAL
     * @see RecordType#STATE_DELETED
     */
    public int state;
    /**
     * 是否选中，用于 UI
     */
    @Ignore
    public boolean isChecked;

    @Ignore
    public RecordType(String name, String imgName, int type) {
        this.name = name;
        this.imgName = imgName;
        this.type = type;
    }

    @Ignore
    public RecordType(String name, String imgName, int type, long ranking) {
        this.name = name;
        this.imgName = imgName;
        this.type = type;
        this.ranking = ranking;
    }

    public RecordType(int id, String name, String imgName, int type, long ranking) {
        this.id = id;
        this.name = name;
        this.imgName = imgName;
        this.type = type;
        this.ranking = ranking;
    }
}
