package com.studpay.template.adapters.outbound.persistence.mapper;


import com.studpay.template.adapters.outbound.persistence.entity.ProductEntity;
import com.studpay.template.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductPersistenceMapper extends GenericPersistenceMapper<ProductEntity, Product> {

}
