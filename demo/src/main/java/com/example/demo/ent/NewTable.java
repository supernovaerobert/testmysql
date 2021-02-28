package com.example.demo.ent;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@Entity
@FieldNameConstants
@Table(name = "new_table")
@ToString
public class NewTable {

	@Id // 系统统一使用雪花算法ID
	private Long id;

	private Long columns;


}
