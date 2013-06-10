alter table `content_list` add column `keyword` varchar(100) character set utf8 NOT NULL default '';
alter table `content_list` change column `download_url` `download_url` varchar(255) character set utf8 NOT NULL default '';
alter table `content_list` change column `preview_url` `preview_url` varchar(255) character set utf8 NOT NULL default '';
alter table `content_list` change column `other_details` `other_details` varchar(255) character set utf8 NOT NULL default '';
alter table `content_list` change column `author` `author` varchar(255) character set utf8 NOT NULL default '';
alter table `content_list` change column `supplier_id` `supplier_id` varchar(255) character set utf8 NOT NULL default '';
alter table `content_list` change column `short_item_ref` `short_item_ref` varchar(255) character set utf8 NOT NULL default '';