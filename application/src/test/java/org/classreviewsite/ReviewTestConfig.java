package org.classreviewsite;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.classreviewsite.domain.review.ClassReviewDataRepository;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@TestConfiguration
public class ReviewTestConfig {

    private final ClassReviewDataRepository classReviewDataRepository;

    public ReviewTestConfig(ClassReviewDataRepository classReviewDataRepository) {
        this.classReviewDataRepository = classReviewDataRepository;
    }

    @Bean
    public DataSource dataSource() {
        return new HikariDataSource();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource());
        emf.setPackagesToScan("org.classreviewsite.domain");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter() {{
            setDatabase(Database.MYSQL);
            setGenerateDdl(true);
            setShowSql(true);
        }});

        return emf;
    }

    @Bean
    public BeanPostProcessor persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public ClassReviewDataRepository reviewDataRepository() {
        return reviewDataRepository();
    }
}
