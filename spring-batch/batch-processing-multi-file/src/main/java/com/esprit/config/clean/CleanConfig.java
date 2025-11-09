package com.esprit.config.clean;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class CleanConfig {

    /*
    @Bean
    public Step cleanAndRejectStep(JobRepository jobRepository,
                                   PlatformTransactionManager txManager,
                                   NamedParameterJdbcTemplate jdbc) {
        return new StepBuilder("cleanAndRejectStep", jobRepository)
                .tasklet((contribution, ctx) -> {
                    String batchId = String.valueOf(
                            ctx.getStepContext().getJobParameters().get("batchId")
                    );
                    var p = Map.of("batchId", batchId);

                    // Reset (idempotence)
                    jdbc.update("UPDATE stg_customer SET valid_flag=TRUE, error_code=NULL, error_msg=NULL WHERE batch_id=:batchId", p);
                    jdbc.update("UPDATE stg_order    SET valid_flag=TRUE, error_code=NULL, error_msg=NULL WHERE batch_id=:batchId", p);

                    // Règles stg_customer
                    jdbc.update("""
          UPDATE stg_customer
             SET valid_flag = FALSE, error_code = 'EMAIL_FORMAT', error_msg = 'Email invalide'
           WHERE batch_id = :batchId AND valid_flag = TRUE
             AND (email IS NULL OR email !~ '^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')
        """, p);

                    jdbc.update("""
          UPDATE stg_customer
             SET valid_flag = FALSE, error_code = 'GENDER_INVALID', error_msg = 'Valeur non autorisée'
           WHERE batch_id = :batchId AND valid_flag = TRUE
             AND COALESCE(gender,'') NOT IN ('Male','Female','Other','Unknown')
        """, p);

                    jdbc.update("""
          UPDATE stg_customer
             SET valid_flag = FALSE, error_code = 'DOB_FUTURE', error_msg = 'Date de naissance future'
           WHERE batch_id = :batchId AND valid_flag = TRUE
             AND dob > CURRENT_DATE
        """, p);

                    jdbc.update("""
          UPDATE stg_customer
             SET valid_flag = FALSE, error_code = 'DOB_AGE_RANGE', error_msg = 'Âge > 120 ans'
           WHERE batch_id = :batchId AND valid_flag = TRUE
             AND dob IS NOT NULL AND dob < CURRENT_DATE - INTERVAL '120 years'
        """, p);

                    // Règles stg_order
                    jdbc.update("""
          UPDATE stg_order
             SET valid_flag = FALSE, error_code = 'AMOUNT_NEG', error_msg = 'Montant négatif'
           WHERE batch_id = :batchId AND valid_flag = TRUE
             AND amount IS NOT NULL AND amount < 0
        """, p);

                    jdbc.update("""
          UPDATE stg_order
             SET valid_flag = FALSE, error_code = 'STATUS_INVALID', error_msg = 'Valeur non autorisée'
           WHERE batch_id = :batchId AND valid_flag = TRUE
             AND COALESCE(status,'') NOT IN ('NEW','PAID','CANCELLED','SHIPPED','REFUNDED')
        """, p);

                    jdbc.update("""
          UPDATE stg_order
             SET valid_flag = FALSE, error_code = 'DATE_FUTURE', error_msg = 'Date future'
           WHERE batch_id = :batchId AND valid_flag = TRUE
             AND order_date > CURRENT_DATE
        """, p);

                    // Pré-validation FK (contre staging customers valides du même batch)
                    jdbc.update("""
          UPDATE stg_order o
             SET valid_flag = FALSE, error_code = 'FK_CUSTOMER', error_msg = 'Customer inexistant'
           WHERE o.batch_id = :batchId AND o.valid_flag = TRUE
             AND NOT EXISTS (
               SELECT 1 FROM stg_customer c
                WHERE c.batch_id = o.batch_id
                  AND c.valid_flag = TRUE
                  AND c.customer_id = o.customer_id
             )
        """, p);

                    // Traçage des rejets
                    jdbc.update("DELETE FROM rejets WHERE batch_id = :batchId", p);

                    jdbc.update("""
          INSERT INTO rejets (batch_id, source_table, source_pk, error_code, error_msg, payload_json)
          SELECT :batchId, 'stg_customer', CAST(customer_id AS VARCHAR), error_code, error_msg, to_jsonb(s.*)
            FROM stg_customer s
           WHERE s.batch_id = :batchId AND s.valid_flag = FALSE AND s.error_code IS NOT NULL
        """, p);

                    jdbc.update("""
          INSERT INTO rejets (batch_id, source_table, source_pk, error_code, error_msg, payload_json)
          SELECT :batchId, 'stg_order', CAST(order_id AS VARCHAR), error_code, error_msg, to_jsonb(o.*)
            FROM stg_order o
           WHERE o.batch_id = :batchId AND o.valid_flag = FALSE AND o.error_code IS NOT NULL
        """, p);

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

     */

}
