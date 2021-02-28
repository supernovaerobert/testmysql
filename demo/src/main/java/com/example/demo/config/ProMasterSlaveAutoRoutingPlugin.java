package com.example.demo.config;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.support.DbHealthIndicator;
import com.baomidou.dynamic.datasource.support.DdConstants;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;

import lombok.extern.slf4j.Slf4j;


/**
 * 读写分离
 * 
 * @author Administrator
 *
 */
@Slf4j
@Aspect
@Component
@Order(value = 3)
public class ProMasterSlaveAutoRoutingPlugin {

	@Autowired
	private DynamicDataSourceProperties properties;

	/*
	 * 切点
	 */
	@Pointcut("target(com.example.demo.base.service.BaseService)")
	public void baseServicePointcut() {
	}

	/**
	 * 环绕加redis锁
	 * 
	 * @param pjp
	 * @return
	 */
	@Around(value = "baseServicePointcut()")
	public Object around(ProceedingJoinPoint pjp) {
		boolean empty = true;
		try {

			// 获得当前线程的数据源
			empty = StringUtils.isEmpty(DynamicDataSourceContextHolder.peek());
			String threadName = Thread.currentThread().getName();
			long threadId = Thread.currentThread().getId();
			String threadInfo = "线程名" + threadName + " , 线程ID" + threadId;
			log.info(threadInfo + " 线程是否没有数据源"
					+ empty);
			if (empty) {
				MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
				// 获取环绕方法对象
				 Method method = methodSignature.getMethod();
				// 通过方法,获取注解对象,以便于获取注解中的参数
				Transactional transactional = method.getAnnotation(Transactional.class);

				String slaveOrMaster = getDataSource(transactional);

				log.info(threadInfo + " 获取的连接池" + slaveOrMaster);

				DynamicDataSourceContextHolder.push(slaveOrMaster);
			}
			log.info("当前线程数据源" + DynamicDataSourceContextHolder.peek());
			return  pjp.proceed();
		
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			if (empty) {
				DynamicDataSourceContextHolder.clear();
			}
		}
	}

	/**
	 * 获取动态数据源名称，重写注入 DbHealthIndicator 支持数据源健康状况判断选择
	 *
	 * @param mappedStatement mybatis MappedStatement
	 * @return 获取真实的数据源名称
	 */
	public String getDataSource(Transactional transactional) {
		String slave = DdConstants.SLAVE;
		if (properties.isHealth()) {
			/*
			 * 根据从库健康状况，判断是否切到主库
			 */
			boolean health = DbHealthIndicator.getDbHealth(DdConstants.SLAVE);
			if (!health) {
				health = DbHealthIndicator.getDbHealth(DdConstants.MASTER);
				if (health) {
					slave = DdConstants.MASTER;
				}
			}
		}
		if (transactional == null) {
			// 如果没有注解,说明是默认只读
			return slave;
		} else {
			// 如果是只读 slave ,否则 MASTER;
			return transactional.readOnly() ? slave : DdConstants.MASTER;
		}

	}

}
