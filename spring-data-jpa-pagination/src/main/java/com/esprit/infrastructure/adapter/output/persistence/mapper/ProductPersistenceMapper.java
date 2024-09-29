package com.esprit.infrastructure.adapter.output.persistence.mapper;

import com.esprit.domain.model.Product;
import com.esprit.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductPersistenceMapper {

    ProductEntity toProductEntity(Product product);

    Product toProduct(ProductEntity productEntity);

}