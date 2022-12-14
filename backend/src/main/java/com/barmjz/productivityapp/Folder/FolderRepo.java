package com.barmjz.productivityapp.Folder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FolderRepo extends JpaRepository<Folder,Long> {
    List<Folder> getFoldersByUserId(Long userId);
    boolean existsFolderById(Long folderId);
    boolean existsFolderByNameAndUser_Id(String name, Long user_id);
}
