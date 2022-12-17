package com.barmjz.productivityapp.todo;

import com.barmjz.productivityapp.category.Category;
import com.barmjz.productivityapp.category.CategoryRepo;
import com.barmjz.productivityapp.task.*;
import com.barmjz.productivityapp.user.User;
import com.barmjz.productivityapp.user.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class TodoService {
    private final Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();

    private final UserRepo userRepo;

    private final CategoryRepo categoryRepo;

    private final OneTimeTaskRepo oneTimeTaskRepo;

    private final RepeatedTaskRepo repeatedTaskRepo;





    public List<Task> getTasks(){
        List<Task> tasks = new ArrayList<>();
        List<RepeatedTask> repeatedTasks = getRepeatedTasks();
        List<OneTimeTask> oneTimeTasks = getOnetimeTasks();
        tasks.addAll(repeatedTasks);
        tasks.addAll(oneTimeTasks);
        return tasks;
    }
    private List<OneTimeTask> getOnetimeTasks(){
        User user = userRepo.getUserByEmail(userAuthentication.getName()).get();
        return oneTimeTaskRepo.getAllByUserIdAndIsToDoEqualsOrderByDueDateAsc(user.getId(), true).orElseThrow();
    }

    private List<RepeatedTask> getRepeatedTasks(){
        User user = userRepo.getUserByEmail(userAuthentication.getName()).get();
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String currentDay = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        Optional<List<RepeatedTask>> repeatedTasks;
        switch (currentDay){
            case "Saturday":
                repeatedTasks = repeatedTaskRepo.getAllByUserIdAndSaturdayEqualsAndLastRemovalDateNot(user.getId(), true, date);
            case "Sunday":
                repeatedTasks = repeatedTaskRepo.getAllByUserIdAndSundayEqualsAndLastRemovalDateNot(user.getId(), true, date);
            case "Monday":
                repeatedTasks = repeatedTaskRepo.getAllByUserIdAndMondayEqualsAndLastRemovalDateNot(user.getId(), true, date);
            case "Tuesday":
                repeatedTasks = repeatedTaskRepo.getAllByUserIdAndTuesdayEqualsAndLastRemovalDateNot(user.getId(), true, date);
            case "Wednesday":
                repeatedTasks = repeatedTaskRepo.getAllByUserIdAndWednesdayEqualsAndLastRemovalDateNot(user.getId(), true, date);
            case "Thursday":
                repeatedTasks = repeatedTaskRepo.getAllByUserIdAndThursdayEqualsAndLastRemovalDateNot(user.getId(), true, date);
            case "Friday":
                repeatedTasks = repeatedTaskRepo.getAllByUserIdAndFridayEqualsAndLastRemovalDateNot(user.getId(), true, date);
            default:
                repeatedTasks = null;
        }
        return repeatedTasks.get();
    }

    public void addOnetimeTask(OneTimeTask oneTimeTask){
        oneTimeTaskRepo.save(oneTimeTask);
    }

    public void addRepeatedTask(RepeatedTask repeatedTask){
        repeatedTaskRepo.save(repeatedTask);
    }

    public void markOnetimeTask(Long id){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        oneTimeTaskRepo.markTaskAsDone(id, (java.sql.Date) date);

    }

    public void unmarkOneTimeTask(Long id){
        oneTimeTaskRepo.unMarkTaskAsDone(id);
    }

    public void markRepeatedTask(Long id){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        repeatedTaskRepo.changeRemovalDate(id, date);
    }

    public void unMarkRepeatedTask(long id){
        RepeatedTask unmakredTask = repeatedTaskRepo.getById(id);
        Instant now = unmakredTask.getLastRemovalDate().toInstant();
        Instant yesterday = now.minus(1, ChronoUnit.DAYS);
        repeatedTaskRepo.changeRemovalDate(id, Date.from(yesterday));
    }

    public Map<String, List<OneTimeTask>> categorizeOnetimeTasks(){
        Map<String, List<OneTimeTask>> stringListMap = new HashMap<>();
        List<Category> categories = categoryRepo.findAll();
        for (Category category: categories){
            List<OneTimeTask> categoryTasks = oneTimeTaskRepo.getAllByCategory(category).get();
            stringListMap.put(category.getCategoryName(), categoryTasks);
        }
        return stringListMap;
    }

    public Map<String, List<RepeatedTask>> categorizeRepeatedTasks(){
        Map<String, List<RepeatedTask>> stringListMap = new HashMap<>();
        List<Category> categories = categoryRepo.findAll();
        for (Category category: categories){
            List<RepeatedTask> categoryTasks = repeatedTaskRepo.getAllByCategory(category).get();
            stringListMap.put(category.getCategoryName(), categoryTasks);
        }
        return stringListMap;
    }


}
