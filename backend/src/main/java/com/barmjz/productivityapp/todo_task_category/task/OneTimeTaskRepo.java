package com.barmjz.productivityapp.todo_task_category.task;
import com.barmjz.productivityapp.todo_task_category.category.Category;
import com.barmjz.productivityapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OneTimeTaskRepo extends JpaRepository<OneTimeTask, Long> {
    Optional<List<OneTimeTask>> getAllByUserId(Long userId);
    Optional<List<OneTimeTask>> getAllByCategory(Category category);
    Optional<List<OneTimeTask>> getAllByUserIdAndTodoEquals(Long userId, boolean todo);

    Optional<List<OneTimeTask>> getAllByUserIdAndTodoEqualsOrderByDueDate(Long userId, boolean todo);


    Optional<List<OneTimeTask>> getTop10ByUserIdAndCompletionDateNotNullOrderByCompletionDateDesc(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE OneTimeTask t SET t.completionDate = ?2 WHERE t.id = ?1")
    void markTaskAsDone(Long taskId, Date currentDate);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE OneTimeTask t SET t.completionDate = null WHERE t.id = ?1")
    void unMarkTaskAsDone(Long taskId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE OneTimeTask t SET t.todo = ?2 WHERE t.id = ?1")
    void changeTodoFlag(Long taskId, boolean bool);


    Optional<OneTimeTask> getByCreationDate(Date creationDate);

    @Query("SELECT t FROM OneTimeTask t WHERE t.user = ?1 AND t.completionDate IS NOT NULL")
    Optional<List<OneTimeTask>> getCompletedTasks(User user);


}
