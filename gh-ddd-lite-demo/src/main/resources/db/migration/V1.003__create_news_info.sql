create table tb_news_info
(
	id bigint auto_increment primary key,

  category_id bigint not null,
	status tinyint null,
	title varchar(64) not null,
	content text null,

	create_time datetime not null,
	update_time datetime not null,
	version tinyint not null
);

