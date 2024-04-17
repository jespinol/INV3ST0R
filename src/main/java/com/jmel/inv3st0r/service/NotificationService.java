package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.enums.NotificationType;
import com.jmel.inv3st0r.enums.TransactionType;
import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Notification;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.model.User;
import com.jmel.inv3st0r.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepo;

    public boolean hasNewNotifications(Long userId) {
        return notificationRepo.existsByUserIdAndDateAfter(userId, LocalDateTime.now().minusMinutes(5));
    }

    public List<Notification> getRecentNotifications(Long userId) {
        return notificationRepo.findAllByUserIdOrderByDateDesc(userId).stream()
                .filter(notification -> notification.getDate().isBefore(LocalDateTime.now().minusMinutes(2)))
                .limit(5)
                .toList();
    }

    public List<Notification> getAllNotifications(Long userId) {
        return notificationRepo.findAllByUserIdOrderByDateDesc(userId).stream()
                .filter(notification -> notification.getDate().isBefore(LocalDateTime.now().minusMinutes(2)))
                .toList();
    }

    public void createNotification(Transaction transaction) {
        Notification notification = new Notification();
        notification.setUser(transaction.getAccount().getUser());
        notification.setType(transaction.getTransactionType().equals(TransactionType.BUY) ? NotificationType.BUY : NotificationType.SELL);
        notification.setMessage("%s %s stock with account %s.".formatted(transaction.getTransactionType().equals(TransactionType.BUY) ? "Purchased" : "Sold", transaction.getSymbol(), transaction.getAccount().getAccountName()));
        notificationRepo.save(notification);
    }

    public void createNotification(Account account, double fundAmount) {
        Notification notification = new Notification();
        notification.setUser(account.getUser());
        notification.setType(NotificationType.FUND);
        notification.setMessage("Account %s funded with $ %.2f".formatted(account.getAccountName(), fundAmount));
        notificationRepo.save(notification);
    }

    public void createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(NotificationType.INFO);
        notification.setMessage(message);
        notificationRepo.save(notification);
    }
}
