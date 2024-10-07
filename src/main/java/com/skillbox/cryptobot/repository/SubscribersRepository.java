package com.skillbox.cryptobot.repository;


import com.skillbox.cryptobot.model.Subscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface SubscribersRepository extends JpaRepository<Subscribers, Long> {

    Subscribers findSubscribersByUserId(Long id);

    List<Subscribers> findSubscribersByPriceGreaterThan(BigDecimal price);

    default List<Long> findUserIdByPriceGreaterThan(BigDecimal price) {

        List<Long> userIds = new ArrayList<>();
        List<Subscribers> subscribers = findSubscribersByPriceGreaterThan(price);

        for (Subscribers sub : subscribers) {
            Long userId = sub.getUserId();
            userIds.add(userId);
        }

        return userIds;
    }
}


