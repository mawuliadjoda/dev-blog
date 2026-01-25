package com.esprit.product.adapters.outbound.persistence.mapper;

import com.esprit.product.adapters.outbound.persistence.entity.ProductEntity;
import com.esprit.product.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductPersistenceMapper extends GenericPersistenceMapper<ProductEntity, Product> {

}
