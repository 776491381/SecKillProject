package org.seckill.exception;

/**
 * 重复秒杀异常（运行时异常）
 * Created by fyy on 6/22/17.
 */
public class RepeatKillExecution extends SeckillExecution{

    public RepeatKillExecution(String message) {
        super(message);
    }

    public RepeatKillExecution(String message, Throwable cause) {
        super(message, cause);
    }
}
