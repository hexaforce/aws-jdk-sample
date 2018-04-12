CREATE TABLE `mail_send_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `last_modified_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  `email_address` varchar(250) DEFAULT NULL COMMENT 'send mail address',
  `history_type` enum('Undetermined','Delivery','Bounce','Complaint') NOT NULL COMMENT 'Undetermined:未確認、Delivery:送信済み、Bounce:バウンス、Complaint:苦情',
  `subject` varchar(200) NOT NULL COMMENT 'contents subject',
  `server_timestamp` datetime DEFAULT NULL COMMENT 'datetime the server determined',
  `bounce_type` varchar(45) DEFAULT NULL COMMENT 'bounce type',
  `bounce_sub_type` varchar(45) DEFAULT NULL COMMENT 'bounce sub type',
  `retry_count` int(3) DEFAULT '0' COMMENT 'retry count',
  `next_retry` datetime DEFAULT NULL COMMENT 'next execution datetime',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='メール送信履歴'