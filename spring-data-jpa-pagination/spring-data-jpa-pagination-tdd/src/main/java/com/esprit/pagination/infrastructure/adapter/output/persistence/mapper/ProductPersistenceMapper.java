package com.esprit.pagination.infrastructure.adapter.output.persistence.mapper;


import com.esprit.pagination.domain.model.Product;
import com.esprit.pagination.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductPersistenceMapper extends GenericPersistenceMapper<ProductEntity, Product> {

    //ProductEntity toProductEntity(Product product);
//
    //Product toProduct(ProductEntity productEntity);
//
    //List<Product> toProducts(List<ProductEntity> allProducts);
}