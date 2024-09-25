package com.skillbox.cryptobot.repository;


import com.skillbox.cryptobot.model.Subscribers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscribersRepository extends JpaRepository<Subscribers, Long> {
}
