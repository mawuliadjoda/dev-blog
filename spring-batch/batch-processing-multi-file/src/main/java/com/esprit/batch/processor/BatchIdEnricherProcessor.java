package com.esprit.batch.processor;

import com.esprit.domain.model.StgCustomer;
import com.esprit.domain.model.StgOrder;
import lombok.NonNull;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ItemProcessor;

public class BatchIdEnricherProcessor<T> implements ItemProcessor<T, T> {
    @Override
    public T process(@NonNull T item) {
        var ctx = StepSynchronizationManager.getContext();

        if (ctx == null) {
            throw new IllegalStateException("Step context is not available");
        }

        var batchId = String.valueOf(ctx.getStepExecution().getJobParameters().getString("batchId"));

        if (item instanceof StgCustomer c) c.setBatchId(batchId);
        if (item instanceof StgOrder o) o.setBatchId(batchId);
        return item;
    }
}
