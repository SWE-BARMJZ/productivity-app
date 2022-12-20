package com.barmjz.productivityapp.todo_task_category.category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    Optional<Category> getCategoryByCategoryName(String categoryName);

    Optional<List<Category>> getCategoryByUserId(long userId);

}
