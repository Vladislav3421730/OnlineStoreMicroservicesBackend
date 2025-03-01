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
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CustomProductRepositoryImpl implements CustomProductRepository {

    private static final String SORT_VALUE_CHEAP = "cheap";
    private static final String SORT_VALUE_EXPENSIVE = "expensive";
    private static final String SORT_VALUE_ALPHABET = "alphabet";
    private static final String SORT_FIELD_TITLE = "title";
    private static final String SORT_FIELD_COAST = "coast";
    private static final String SORT_FIELD_ID = "id";

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
        if (productFilterDTO.getSort() != null) {
            switch (productFilterDTO.getSort()) {
                case SORT_VALUE_CHEAP -> query.orderBy(cb.asc(root.get(SORT_FIELD_COAST)));
                case SORT_VALUE_EXPENSIVE -> query.orderBy(cb.desc(root.get(SORT_FIELD_COAST)));
                case SORT_VALUE_ALPHABET -> query.orderBy(cb.asc(root.get(SORT_FIELD_TITLE)));
                default ->  query.orderBy(cb.asc(root.get(SORT_FIELD_ID)));
            }
        }
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


}
