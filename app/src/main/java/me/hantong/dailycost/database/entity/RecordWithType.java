package me.hantong.dailycost.database.entity;

import androidx.room.Relation;

import java.util.List;

/**
 * 包含 RecordType 的 Record
 *
 * @author X
 */
public class RecordWithType extends Record {
    @Relation(parentColumn = "record_type_id", entityColumn = "id", entity = RecordType.class)
    public List<RecordType> mRecordTypes;
}
