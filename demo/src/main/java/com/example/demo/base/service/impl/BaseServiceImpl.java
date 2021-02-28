package com.example.demo.base.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.base.repository.BaseRepository;
import com.example.demo.base.service.BaseService;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;



/**
 * 基础Service类的实现类,实现Service的基本方法.简化子类中无意义的重复方法,不要在事务中进行业务逻辑处理,不要做无意义的事情,Service中只做必要的增删改查必要的事情,Repository只做数据库访问
 * 
 * @author robert
 *
 * @param <R>  项目中的JpaRepository子类对象
 * @param <T>  项目中的JpaRepository对应的实体类对象
 * @param <ID> 项目中的JpaRepository对应的实体类对象ID</br>
 *             2020-8-10 14:56:32
 * 
 *             <p>
 *             注意从Service层中查询返会的实体类对象,不可以修改属性,如果事务已经关闭,将会报异常,事务没有关闭则会更新数据库
 *             </p>
 *
 */
@Transactional(readOnly = true) // 默认设置只读事务,大多数情况下都是查询,只读事务与读写事务的比较大的运行性能区别就是只读事务避免了Hibernate的检测和同步持久对象的状态的更新,提升了运行性能。
public abstract class BaseServiceImpl<R extends BaseRepository<T, ID>, T, ID extends Long>
		implements BaseService<R, T, ID> {

	@Autowired
	protected R entityRepository;

	@PersistenceContext
	protected EntityManager entityManager;

	protected JPAQueryFactory queryFactory;

	@PostConstruct // 指定初始化queryFactory
	public void init() {
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	@Override
	public Iterable<T> findAll(Predicate predicate) {
		return entityRepository.findAll(predicate);
	}

	/**
	 * 通过ID查找一个对象,如果对象不存在,那么调用对象的非ID属性时会抛出一个对象未找到异常
	 * javax.persistence.EntityNotFoundException:
	 */
	@Override
	public T getOne(ID id) {
		return entityRepository.getOne(id);
	}

	@Override
	public Optional<T> findOne(Example<T> example) {
		return entityRepository.findOne(example);
	}

	@Override
	public List<T> findAll() {
		List<T> findAll = entityRepository.findAll();
		return findAll;
	}

	@Override
	public List<T> findAllById(Iterable<ID> ids) {
		List<T> findAllById = entityRepository.findAllById(ids);
		return findAllById;
	}

	@Override
	public Optional<T> findById(ID id) {
		Optional<T> findById = entityRepository.findById(id);
		return findById;
	}

	@Override
	public List<T> findAll(Specification<T> spec) {
		return entityRepository.findAll(spec);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteById(ID id) {
		entityRepository.deleteById(id);
	}

	/**
	 * 保存或者更新所有
	 */
	@Override
	@Transactional(readOnly = false)
	public List<T> saveAll(Iterable<T> entities) {
		return entityRepository.saveAll(entities);
	}

	/**
	 * 保存或者更新
	 */
	@Override
	@Transactional(readOnly = false)
	public T save(T t) {
		return entityRepository.save(t);
	}

	@Override
	public long count(Predicate predicate) {
		long count = entityRepository.count(predicate);
		return count;
	}

}
