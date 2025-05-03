package com.example.market.util;


import com.example.market.dto.ProductFilterDto;
import com.example.market.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@UtilityClass
public class PredicateFormationAssistant {

    public List<Predicate> createFromDto(ProductFilterDto productFilterDTO, CriteriaBuilder cb, Root<Product> root){
        List<Predicate> predicates = new ArrayList<>();
        if(productFilterDTO.getTitle()!=null && !productFilterDTO.getTitle().isBlank()){
            log.info("filter title (allowed in search input) {} was added to predicates",productFilterDTO.getTitle());
            predicates.add(cb.like(cb.lower(root.get("title")),"%"+productFilterDTO.getTitle().toLowerCase()+"%"));
        }
        if(productFilterDTO.getCategory()!=null && !productFilterDTO.getCategory().isBlank()){
            log.info("filter category {} was added to predicates",productFilterDTO.getCategory());
            predicates.add(cb.like(cb.lower(root.get("category")),"%"+productFilterDTO.getCategory().toLowerCase()+"%"));
        }
        if(productFilterDTO.getMinPrice()!=null){
            log.info("filter minPrice {} was added to predicates",productFilterDTO.getMinPrice());
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"),productFilterDTO.getMinPrice()));
        }
        if(productFilterDTO.getMaxPrice()!=null){
            log.info("filter maxPrice {} was added to predicates",productFilterDTO.getMaxPrice());
            predicates.add(cb.lessThanOrEqualTo(root.get("price"),productFilterDTO.getMaxPrice()));
        }
        return predicates;
    }


}
