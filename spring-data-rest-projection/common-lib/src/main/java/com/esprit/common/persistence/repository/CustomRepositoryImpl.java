package com.esprit.common.persistence.repository;

import com.esprit.common.persistence.entities.ProductEntity;
import com.esprit.common.persistence.specification.ProductSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

@RequiredArgsConstructor
public class CustomRepositoryImpl implements  CustomRepository<ProductEntity>{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProductEntity> search(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ProductEntity> query = cb.createQuery(ProductEntity.class);
        Root<ProductEntity> root = query.from(ProductEntity.class);
        // query.where(cb.equal(root.get("name"), name));
        query.where(ProductSpecification.byName(name).toPredicate(root, query, cb));
        return em.createQuery(query).getResultList();
    }
}
