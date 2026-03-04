package com.cashflow.coredata.repository.category;

import com.cashflow.coredata.domain.dto.response.category.CategorySummaryResponse;
import com.cashflow.coredata.domain.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT EXISTS (" +
            "SELECT * FROM tb_category category " +
            "WHERE UPPER(category.name) = UPPER(:name) " +
            "AND category.user_id = :userId " +
            "AND category.active = true)", nativeQuery = true)
    Long existsByNameIgnoreCase(String name, Long userId);

    @Query(value = "SELECT EXISTS (" +
            "SELECT * FROM tb_category category " +
            "WHERE UPPER(category.name) = UPPER(:name) " +
            "AND category.user_id = :userId " +
            "AND category.id != :categoryId " +
            "AND category.active = true)", nativeQuery = true)
    Long existsByNameIgnoreCaseDifferentCategoryId(String name, Long userId, Long categoryId);

    @Query(value = "SELECT new com.cashflow.coredata.domain.dto.response.category.CategorySummaryResponse(" +
            " category.id, " +
            " category.name, " +
            " category.color, " +
            " category.icon " +
            ") FROM Category category " +
            "WHERE category.userId = :userId " +
            "AND category.active = true " +
            "AND UPPER(category.name) LIKE UPPER(CONCAT('%', :name, '%')) " +
            "ORDER BY category.name ASC")
    Page<CategorySummaryResponse> findByNameLikeIgnoreCase(String name, Long userId, Pageable pageable);

    Optional<Category> findByIdAndUserIdAndActiveTrue(Long id, long userId);

}
