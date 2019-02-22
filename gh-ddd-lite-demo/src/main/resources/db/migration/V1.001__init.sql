create table tb_student
(
	id bigint auto_increment primary key,
	create_time bigint not null,
	update_time bigint not null,
	version tinyint not null,
	age bigint null,
	name varchar(32) null,
	address varchar(255) null,
	hobby varchar(255) null,
	instant datetime null,
	parent_id bigint null,
	property int null,
	salary decimal(19,2) null,
	status tinyint null,
	user_id bigint null,
	uuid varchar(64) null
);

