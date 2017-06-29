-- 存储过程
DELIMITER $$--换行
-- 定义存储过程
CREATE PROCEDURE `seckill`.`execute_seckill`
(in v_seckill_id bigint ,in v_phone bigint,
in v_kill_time  TIMESTAMP ,out r_result int)
BEGIN DECLARE insert_count int DEFAULT  0;
START TRANSACTION ;
insert ignore into success_killed (seckill_id,user_phone,create_time)
  VALUES (v_seckill_id,v_phone,v_kill_time);
SELECT ROW_COUNT() into insert_count;
IF(insert_count = 0) THEN
  ROLLBACK ;
  SET r_result = -1;
ELSEIF (insert_count < 0 )THEN
  ROLLBACK ;
  SET r_result = -2;
ELSE
  UPDATE seckill set number  = number - 1 where seckill_id = v_seckill_id and end_time > v_kill_time and start_time < v_kill_time and number > 0;
  SELECT  ROW_COUNT() into insert_count;
  IF(insert_count = 0) THEN
    ROLLBACK ;
    set r_result = 0;
  ELSEIF (insert_count < 0) THEN
    ROLLBACK ;
    set r_result = -2;
  ELSE
    COMMIT ;
    set r_result = 1;
  END IF;
END IF;
END;
$$

DELIMITER ; --换行
set @r_result = -3;
--执行存储过程
call execute_seckill(1003,13292123005,now(),@r_result);

select @r_result;