package dev.hroberts.fileshare.configuration;

import dev.hroberts.fileshare.persistence.InMemoryFileInfoDatabase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class BeanConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public InMemoryFileInfoDatabase inMemoryFileInfoDatabase() {
        return new InMemoryFileInfoDatabase();
    }
}
