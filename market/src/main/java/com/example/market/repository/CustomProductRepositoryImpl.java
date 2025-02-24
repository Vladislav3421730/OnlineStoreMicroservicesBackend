package com.example.market.repository;


import com.example.market.dto.ProductFilterDto;
import com.example.market.model.Product;
import com.example.market.util.PredicateFormationAssistant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomProductRepositoryImpl implements CustomProductRepository {

    EntityManager entityManager;

    @Override
    public Page<Product> findAll(ProductFilterDto productFilterDTO, PageRequest pageRequest) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        List<Predicate> predicates = PredicateFormationAssistant.createFromDto(productFilterDTO, cb, root);

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        sortProducts(pageRequest, query, cb, root);
        List<Product> products = entityManager.createQuery(query)
                .setFirstResult((int) pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        long totalElements = getTotalAmountByFilter(productFilterDTO);

        return new PageImpl<>(products, pageRequest, totalElements);
    }

    @Override
    public void deleteProductWithOrderItems(Long productId) {
        Product product = entityManager.find(Product.class, productId);
        if (product != null) {
            entityManager.createQuery("DELETE FROM OrderItem o WHERE o.product.id = :productId")
                    .setParameter("productId", productId)
                    .executeUpdate();
            entityManager.createQuery("DELETE FROM Cart c WHERE c.product.id = :productId")
                    .setParameter("productId", productId)
                    .executeUpdate();
            entityManager.remove(product);
        }
    }

    private int getTotalAmountByFilter(ProductFilterDto productFilterDTO) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Product> root = query.from(Product.class);

        List<Predicate> predicates = PredicateFormationAssistant.createFromDto(productFilterDTO, cb, root);
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }
        query.select(cb.count(root));
        return entityManager.createQuery(query)
                .getSingleResult()
                .intValue();
    }

    private void sortProducts(PageRequest pageRequest, CriteriaQuery<Product> query, CriteriaBuilder cb, Root<Product> root) {
        if (pageRequest.getSort() != null && pageRequest.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageRequest.getSort().forEach(order -> {
                if (order.isAscending()) {
                    orders.add(cb.asc(root.get(order.getProperty())));
                } else {
                    orders.add(cb.desc(root.get(order.getProperty())));
                }
            });
            query.orderBy(orders);
        }
    }

}
