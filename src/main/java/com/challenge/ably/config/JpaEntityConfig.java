package com.challenge.ably.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaEntityConfig {


    @PersistenceContext
    private EntityManager entityManager;

}
