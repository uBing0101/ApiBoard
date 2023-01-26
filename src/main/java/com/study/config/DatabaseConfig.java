package com.study.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Configuration
@PropertySource("classpath:/application.properties")
@RequiredArgsConstructor
public class DatabaseConfig {

  private final ApplicationContext context;
  // 스프링 컨테이너(Container)중 하나로, 빈(Bean)의 생성과 사용, 관계, 생명 주기 등을 관리
  // 여기서는 MyBatis의 Mapper XML 경로를 처리하기위해 사용

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.hikari")
  public HikariConfig hikariConfig() {
    return new HikariConfig();
  }

  @Bean
  public DataSource dataSource() {
    return new HikariDataSource(hikariConfig());
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    // SqlSessionFactory
    // DB의 커넥션과 ,SQL 실행에 대한 모든것을 갖는 객체
    // 해당 Bean 에 주석처리한 factoryBean.setMapperLocations를 통해 Mapper XML의 경로를 지정 하였지만
    // (classpath는 src/main.resources 디렉터리를 의미) 해당 경로에 Mapper XML을 추가
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource());
    factoryBean.setMapperLocations(context.getResources("classpath:/mappers/**/*Mapper.xml"));
    return factoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSession() throws Exception {
    // sqlSession
    // SQL 실행에 필요한 모든 메서드(INSERT, UPDATE, DELETE, SELECT) 를 갖는 객체
    return new SqlSessionTemplate(sqlSessionFactory());
  }
}
