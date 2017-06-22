/*--数据库初始化--*/
CREATE DATABASE seckill;

use seckill;

CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
`name` VARCHAR (120) NOT NULL COMMENT '商品名称',
`number` INT NOT NULL COMMENT '库存数量',
`start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀开始时间',
`end_time` TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_sop_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT = 1000 DEFAULT CHARSET = utf8 COMMENT = '秒杀库存';


insert into seckill(name,number,start_time,end_time)
VALUES
 ('1000秒杀iphone',100,'2017-6-20 00:00:00','2018-1-1 00:00:00'),
 ('100秒杀小米',100,'2017-6-20 00:00:00','2018-1-1 00:00:00'),
 ('300秒杀华为',100,'2017-6-20 00:00:00','2018-1-1 00:00:00'),
 ('2000秒杀三星',100,'2017-6-20 00:00:00','2018-1-1 00:00:00'),
 ('200秒杀一家',100,'2017-6-20 00:00:00','2018-1-1 00:00:00');



/*--登录认证相关信息--*/

create table success_killed(

`seckill_id` bigint NOT NULL comment '商品库存ID',
`user_phone` bigint NOT NULL  comment '用户手机',
`state` tinyint NOT  NULL DEFAULT  -1 comment '状态 -1无效 0成功 1已付款',
`create_time` TIMESTAMP NOT NULL ,
PRIMARY KEY (seckill_id,user_phone) ,/*联合主键*/
key idx_create_time(create_time)


)ENGINE = InnoDB DEFAULT CHARSET = utf8 COMMENT = '秒杀成功明细表'




