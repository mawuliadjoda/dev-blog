package com.esprit.infrastructure.adapter.output.persistence.specification;

import com.esprit.infrastructure.adapter.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecifications {
    public static Specification<ProductEntity> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("name"), STR."%\{name}%");
    }

    public static Specification<ProductEntity> hasNameIn(List<String> names) {
        return (root, query, criteriaBuilder) ->
                (names == null || names.isEmpty()) ? null : criteriaBuilder.in(root.get("name")).value(names);
        // return (root, query, criteriaBuilder) -> {
        //     if (names == null || names.isEmpty()) {
        //         return null;
        //     }
        //     return root.get("category").in(names);
        // };
    }

    public static Specification<ProductEntity> descriptionContains(String description) {
        return (root, query, criteriaBuilder) ->
                description == null ? null : criteriaBuilder.like(root.get("description"), "%" + description + "%");
    }

    public static Specification<ProductEntity> hasDdescriptionIn(List<String> descriptions) {
        return (root, query, criteriaBuilder) ->
                (descriptions == null || descriptions.isEmpty()) ? null : criteriaBuilder.in(root.get("description")).value(descriptions);
    }


    public static Specification<ProductEntity> nameHas(String name)  {

        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), STR."%\{name}%");
    }

    public static Specification<ProductEntity> hasPrice(Double price) {
        return (root, query, criteriaBuilder) ->
                price == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("price"), price);
    }

    public static Specification<ProductEntity> hasPriceGreaterThan(Double price) {
        return (root, query, criteriaBuilder) ->
                price == null ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThan(root.get("price"), price);
    }

    public static Specification<ProductEntity> hasPriceLessThan(Double price) {
        return (root, query, criteriaBuilder) ->
                price == null ? criteriaBuilder.conjunction() : criteriaBuilder.lessThan(root.get("price"), price);
    }
}
