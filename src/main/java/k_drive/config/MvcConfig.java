package k_drive.config;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import k_drive.intercept.SystemIc;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "k_drive.controller", "k_drive.config", "k_drive.service" })
@MapperScan(basePackages = { "k_drive.maps" })
public class MvcConfig extends WebMvcConfigurerAdapter {

	static HikariConfig config = new HikariConfig();
	static HikariDataSource ds;
	static SqlSessionFactoryBean sqlSessionFactory;
	static {
		config.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		config.setDriverClassName("oracle.jdbc.OracleDriver");
		config.setUsername("scott");
		config.setPassword("Tiger07#");
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
	}

	@Bean(name = "con")
	public Connection connection() throws SQLException {
		return ds.getConnection();
	}

	@Bean(name = "sqlsession")
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(ds);
		sqlSessionFactory.setTypeAliasesPackage("k_drive.maps");
		return (SqlSessionFactory) sqlSessionFactory.getObject();
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(ds);
	}
	
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();		
		tilesConfigurer.setDefinitions(new String[] { "/WEB-INF/views/**/tiles.xml" });
		tilesConfigurer.setCheckRefresh(true);
		return tilesConfigurer;
	}
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		TilesViewResolver viewResolver = new TilesViewResolver();
		registry.viewResolver(viewResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SystemIc())
		.addPathPatterns("/web_hard","/web_hard/*");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
		registry.addResourceHandler("/img/**").addResourceLocations("/static/img/");
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxInMemorySize(100000000);
		resolver.setMaxUploadSize(200000000);
		return resolver;
	}
}
