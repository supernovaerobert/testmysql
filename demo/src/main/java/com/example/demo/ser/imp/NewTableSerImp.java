package com.example.demo.ser.imp;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.example.demo.base.service.impl.BaseServiceImpl;
import com.example.demo.ent.NewTable;
import com.example.demo.rep.NewTableRep;
import com.example.demo.ser.NewTableSer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewTableSerImp extends BaseServiceImpl<NewTableRep, NewTable, Long> implements NewTableSer {

	@Override
	public void deleteByIds(Collection<Long> ids) {

	}

}