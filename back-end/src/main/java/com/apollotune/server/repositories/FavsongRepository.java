package com.apollotune.server.repositories;

import com.apollotune.server.entities.Favsong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavsongRepository extends JpaRepository<Favsong, Integer> {
    List<Favsong> findByUserIdAndSongId(Integer userId, Integer songId);

}
