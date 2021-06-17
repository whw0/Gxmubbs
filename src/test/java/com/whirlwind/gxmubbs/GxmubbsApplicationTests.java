package com.whirlwind.gxmubbs;

import com.whirlwind.gxmubbs.dao.AlphaDao;
import com.whirlwind.gxmubbs.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootTest
@ContextConfiguration(classes=GxmubbsApplication.class)
public class GxmubbsApplicationTests implements ApplicationContextAware {
 	//implements ApplicationContextAware可以调用容器
	//定义一个applicationcontext来接收容器
	ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Test
	public  void testApplicationContext(){
		System.out.println(applicationContext);

		//这里容器已经加载AlphaDaoHibernatelmp bean
		AlphaDao alphaDao=applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());

		//这样可以指定加载bean
		//这个名字实在注解后面定义的
		alphaDao=applicationContext.getBean("alphaHibernate",AlphaDao.class);
		System.out.println(alphaDao.select());

		//这个名字是默认的首字母小写
		alphaDao=applicationContext.getBean("alphaDaoMyBatisImpl",AlphaDao.class);
		System.out.println(alphaDao);
	}

	//在Spring中只实例化一次bean,是单例的
	@Test
	public void testBeanManagement(){
		AlphaService alphaService=applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	@Test
	public void testBeanConfig(){
		//这是外部bean
		SimpleDateFormat simpleDateFormat=applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	//以上方法只是为了了解原理
	//有更加简便的方法

	//默认是取高优先级的bean
	//可以指定
	@Autowired
	@Qualifier("alphaHibernate")
	private AlphaDao alphaDao;

	@Autowired
	private  AlphaService alphaService;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI(){
		System.out.println(alphaDao.select());
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}
}
