package com.example.pettracker.pettracker;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "petTrackingEntityManager",
        transactionManagerRef = "petTrackingSqlTransactionManager",
        basePackages = {"com.example.pettracker.pettracker"})
@EntityScan(basePackages = {
        "com.example.pettracker.pettracker"})
public class JpaConfig {

    private final JpaProperties jpaProperties;

    @Autowired
    public JpaConfig(JpaProperties jpaProperties) {
        //All the Jpa properties in the currently active properties file
        this.jpaProperties = jpaProperties;
    }

    /**
     * Get the datasource related properties from the currently active properties file
     *
     * @return properties
     */
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource dataSourceForMySql() {
        return dataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean(name = "petTrackingEntityManager")
    public EntityManagerFactory entityManagerFactory() {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSourceForMySql());
        entityManagerFactoryBean.setPackagesToScan(
                "com.example.pettracker.pettracker"
        );
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties.getProperties());
        entityManagerFactoryBean.setPersistenceUnitName("powersavingzone");
        entityManagerFactoryBean.afterPropertiesSet();

        return entityManagerFactoryBean.getObject();
    }

    @Bean(name = "petTrackerSqlTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory petTrackingEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(petTrackingEntityManager);

        return transactionManager;
    }


}

