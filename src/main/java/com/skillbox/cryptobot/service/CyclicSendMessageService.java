package com.skillbox.cryptobot.service;

import com.skillbox.cryptobot.bot.CryptoBot;
import com.skillbox.cryptobot.repository.SubscribersRepository;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@EnableAsync
public class CyclicSendMessageService {

    private final CryptoBot bot;
    private final SubscribersRepository subscribersRepository;
    private final CryptoCurrencyService cryptoCurrencyService;
    private final String TEXT_TO_MESSAGE = "Пора покупать, стоимость биткоина ";
    private final String USD = " UDS";
    private Map<Long, LocalTime> addressees = new HashMap<>();

    @Value("${telegram.bot.notify.delay.value}")
    private Long minimalGap;   //minutes

    @Async
    @Scheduled(cron = "${telegram.bot.notify.rate.cron}")
    public void cyclicSendMessage() {

        BigDecimal currentPrice;
        List<Long> listUserId = new ArrayList<>();
        try {
            currentPrice = BigDecimal.valueOf(cryptoCurrencyService.getBitcoinPrice());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (currentPrice != null) {
            listUserId = subscribersRepository.findUserIdByPriceGreaterThan(currentPrice);
            updateAddressees(listUserId);
        }

        sendMessageToAddressees(currentPrice);
    }

    public void updateAddressees(List<Long> userIds) {
        for (Long userId : userIds) {
            if (!addressees.containsKey(userId)) {
                addressees.put(userId, LocalTime.now().minusMinutes(minimalGap));
            }
        }

        for (Long userId : addressees.keySet()) {
            if (!userIds.contains(userId)) {
                addressees.remove(userId);
            }
        }
    }

    public void sendMessageToAddressees(BigDecimal price) {
        for (Long userId : addressees.keySet()) {
            LocalTime timeOfLastSending = addressees.get(userId);
            LocalTime nowTime = LocalTime.now();
            if (nowTime.isAfter(timeOfLastSending.plusMinutes(minimalGap))) {
                addressees.put(userId, nowTime);
                bot.sendMessage(userId, TEXT_TO_MESSAGE + TextUtil.toString(price) + USD);
            }
        }
    }

}
