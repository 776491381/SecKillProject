package org.seckill.exception;

/**
 * 秒杀关闭后再次秒杀异常
 * Created by fyy on 6/22/17.
 */
public class SeckillCloseExecution extends SeckillExecution{
    public SeckillCloseExecution(String message) {
        super(message);
    }

    public SeckillCloseExecution(String message, Throwable cause) {
        super(message, cause);
    }
}
