package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    ArrayList<Notification> findAllByUserIdOrderByDateDesc(Long userId);
}
