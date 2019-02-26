package com.geekhalo.ddd.lite.domain.support.entity;

import com.geekhalo.ddd.lite.domain.support.jpa.JpaEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tb_user1")
public class User1 extends JpaEntity {
}
