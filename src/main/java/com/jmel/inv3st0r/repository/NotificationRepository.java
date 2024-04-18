package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {
    ArrayList<Notification> findAllByUserIdOrderByDateDesc(Long userId);
}
