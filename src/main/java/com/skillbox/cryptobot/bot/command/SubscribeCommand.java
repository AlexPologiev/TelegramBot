package com.skillbox.cryptobot.bot.command;

import com.skillbox.cryptobot.model.Subscribers;
import com.skillbox.cryptobot.repository.SubscribersRepository;
import com.skillbox.cryptobot.service.CryptoCurrencyService;
import com.skillbox.cryptobot.utils.TextUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;


import java.math.BigDecimal;


/**
 * Обработка команды подписки на курс валюты
 */
@Service
@AllArgsConstructor
@Slf4j
public class SubscribeCommand implements IBotCommand {

    private final SubscribersRepository repository;
    private final CryptoCurrencyService service;

    @Override
    public String getCommandIdentifier() {
        return "subscribe";
    }

    @Override
    public String getDescription() {
        return "Подписывает пользователя на стоимость биткоина";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        User user = message.getFrom();
        Long userId = user.getId();

        String messageText = message.getText();
        String priceText = messageText.replaceAll("/subscribe", "").trim();
        BigDecimal price = new BigDecimal(priceText);

        Subscribers subscriber = repository.findSubscribersByUserId(userId);
        if (subscriber != null) {
            subscriber.setPrice(price);
            repository.save(subscriber);

            log.info("command: " + messageText + " price: " + price);

            SendMessage answer = new SendMessage();
            answer.setChatId(message.getChatId());
            try {
                answer.setText("Текущая цена биткоина " + TextUtil.toString(service.getBitcoinPrice()) + " USD"
                        + "\nНовая подписка создана на стоимость " + price);
                absSender.execute(answer);
            } catch (Exception e) {
                log.error("Ошибка возникла в /subscribe методе", e);
            }
        }

    }
}