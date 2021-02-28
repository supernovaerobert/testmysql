package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.ent.NewTable;
import com.example.demo.ser.NewTableSer;

@RestController
public class NewTableController {

	@Autowired
	private NewTableSer newTableSer;

	@GetMapping("test")
	public List<NewTable> test() {

		List<NewTable> findAll = newTableSer.findAll();

		return findAll;
	}

	@GetMapping("save")
	public NewTable save(Long id, Long column) {

		NewTable t = new NewTable();
		t.setId(id);
		t.setColumns(column);
		newTableSer.save(t);

		return t;
	}

	@GetMapping("readAndWrite")
	public List<NewTable> readAndWrite(Long id, Long column) {

		NewTable t = new NewTable();
		t.setId(id);
		t.setColumns(column);
		newTableSer.save(t);

		List<NewTable> findAll = newTableSer.findAll();

		System.err.println(findAll);

		t = new NewTable();
		t.setId(id + 1);
		t.setColumns(column);
		newTableSer.save(t);

		findAll = newTableSer.findAll();

		System.err.println(findAll);

		return findAll;
	}

}
