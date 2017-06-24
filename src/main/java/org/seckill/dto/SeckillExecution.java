package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;

/**
 * 用于封装秒杀执行后的结果
 * Created by fyy on 6/22/17.
 */
public class SeckillExecution {

    private long seckillId;

    private int state;

    private String stateInfo;

    //秒杀成功的对象
    private SuccessKilled successKilled;

    //    public SeckillExecution(long seckillId, int state, String stateInfo, SuccessKilled successKilled) {
//        this.seckillId = seckillId;
//        this.state = state;
//        this.stateInfo = stateInfo;
//        this.successKilled = successKilled;
//    }
    //加入枚举后改造构造方法
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }
}
