create table tb_news_category2
(
	_id bigint auto_increment primary key,
  id char(64) not null,
	name varchar(32) null,
	status tinyint null,

	create_time datetime not null,
	update_time datetime not null,
	version tinyint not null
);

