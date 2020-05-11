package me.hantong.dailycost.database.entity;

import java.math.BigDecimal;

/**
 * 支出或收入的总和
 *
 * @author X
 */
public class SumMoneyBean {
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
     * 支出或收入的总和
     */
    public BigDecimal sumMoney;
}
