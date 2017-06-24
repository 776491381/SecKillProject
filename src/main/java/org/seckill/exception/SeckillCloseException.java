package org.seckill.exception;

/**
 * 秒杀关闭后再次秒杀异常
 * Created by fyy on 6/22/17.
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
