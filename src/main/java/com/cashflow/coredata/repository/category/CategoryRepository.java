package com.cashflow.coredata.repository.category;

import com.cashflow.coredata.domain.dto.response.CategoryResponse;
import com.cashflow.coredata.domain.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT EXISTS (" +
            "SELECT * FROM tb_category category " +
            "WHERE UPPER(category.name) = UPPER(:name) " +
            "AND category.user_id = :userId " +
            "AND category.active = true)", nativeQuery = true)
    Long existsByNameIgnoreCase(String name, Long userId);

    @Query(value = "SELECT new com.cashflow.coredata.domain.dto.response.CategoryResponse(" +
            " category.id, " +
            " category.name, " +
            " category.color, " +
            " category.icon " +
            ") FROM Category category " +
            "WHERE category.userId = :userId " +
            "AND UPPER(category.name) LIKE UPPER(CONCAT('%', :name, '%')) " +
            "ORDER BY category.name ASC")
    Page<CategoryResponse> findByNameLikeIgnoreCase(String name, Long userId, Pageable pageable);

}
