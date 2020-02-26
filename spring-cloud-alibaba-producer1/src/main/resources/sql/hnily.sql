CREATE TABLE `local_try_log` (
	`tx_no` varchar(64) NOT NULL COMMENT '事务id',
	`create_time` datetime DEFAULT NULL,
	PRIMARY KEY (`tx_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `local_confirm_log` (
	`tx_no` varchar(64) NOT NULL COMMENT '事务id',
	`create_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8

CREATE TABLE `local_cancel_log` (
	`tx_no` varchar(64) NOT NULL COMMENT '事务id',
	`create_time` datetime DEFAULT NULL,
PRIMARY KEY (`tx_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
