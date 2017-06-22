package org.seckill.exception;

/**
 * Created by fyy on 6/22/17.
 */
public class SeckillExecution extends RuntimeException {
    public SeckillExecution(String message) {
        super(message);
    }

    public SeckillExecution(String message, Throwable cause) {
        super(message, cause);
    }
}
