package com.example.demo.base.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import com.example.demo.base.repository.BaseRepository;
import com.querydsl.core.types.Predicate;

/**
 * 基础Service类的实现类,实现Service的基本方法.简化子类中无意义的重复方法,不要在事务中进行业务逻辑处理,不要做无意义的事情,
 * Service中只做必要的增删改查必要的事情,需要强制事务一致的事情,Repository只做数据库访问
 * 
 * @author robert
 *
 * @param <R>  项目中的JpaRepository子类对象
 * @param <T>  项目中的JpaRepository对应的实体类对象
 * @param <ID> 项目中的JpaRepository对应的实体类对象ID</br>
 *             2020-8-10 14:56:32
 */
public interface BaseService<R extends BaseRepository<T, ID>, T, ID extends Long> {

	List<T> saveAll(Iterable<T> entities);

	public List<T> findAll();

	public List<T> findAllById(Iterable<ID> ids);

	Optional<T> findById(ID id);

	T getOne(ID id);

	T save(T t);

	List<T> findAll(Specification<T> spec);

	Optional<T> findOne(Example<T> example);

	void deleteById(ID id);

	void deleteByIds(Collection<Long> ids);

	Iterable<T> findAll(Predicate predicate);

	public long count(Predicate predicate);

}
