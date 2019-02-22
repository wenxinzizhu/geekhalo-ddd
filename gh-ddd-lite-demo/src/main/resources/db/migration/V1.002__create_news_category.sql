create table tb_news_category
(
	id bigint auto_increment primary key,

	name varchar(32) null,
	status tinyint null,

	create_time bigint not null,
	update_time bigint not null,
	version tinyint not null
);

