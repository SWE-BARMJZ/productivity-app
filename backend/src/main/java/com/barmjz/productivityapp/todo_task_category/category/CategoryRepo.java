package com.barmjz.productivityapp.todo_task_category.category;
import com.barmjz.productivityapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {


    @Query("SELECT c FROM Category c WHERE c.category_name = ?1 AND c.user = ?2")
    Optional<Category> getCategoryByCategoryNameAndUser(String categoryName, User user);

    Optional<List<Category>> getCategoryByUserId(long userId);

    @Modifying
    @Query("UPDATE Category SET category_name = ?1 WHERE id = ?2")
    void renameCategory(String categoryName, long id);

}
