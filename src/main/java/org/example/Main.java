package org.example;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.File;

public class Main extends TelegramLongPollingBot {
    private Map<Long, Integer> levels = new HashMap<>();
    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new Main());

    }

    @Override
    public String getBotUsername() {
        return "BanderogusakItGoItBot";
    }

    @Override
    public String getBotToken() {
        return "6192513273:AAGTFLsjdRP_pn_27XQdNJubrTnjYEpez0w";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId =  getChatId(update);

        if (update.hasMessage() && update.getMessage().getText().equals("/start")) {
            sendImage("level-1", chatId);
        }


//////        if (update.hasCallbackQuery()) {
//////            if (update.getCallbackQuery().getData().equals("glory_for_Ukraine")) {
//////                SendMessage message = createMessage("Героям Слава!");
//////                attachButtons(message, Map.of(
//////                        "Слава Нації!", "glory_for_nation"
//////                ));
//////                message.setChatId(chatId);
//////                sendApiMethodAsync(message);
//////            }
//////
//////            if (update.getCallbackQuery().getData().equals("glory_for_nation")) {
//////                SendMessage message = createMessage("Смерть ворогам!");
//////                message.setChatId(chatId);
//////                sendApiMethodAsync(message);
////            }
//        }
    }

    public Long getChatId(Update update) {
        if (update.hasMessage()) {
            return update.getMessage().getFrom().getId();
        }

        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom().getId();
        }

        return null;
    }

    public SendMessage createMessage(String text) {
        SendMessage message  = new SendMessage();
        message.setText(new String(text.getBytes(), StandardCharsets.UTF_8));
        message.setParseMode("markdown");
        return message;
    }

    public void attachButtons(SendMessage message, Map<String, String> buttons) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        for (String buttonName : buttons.keySet()) {
            String buttonValue = buttons.get(buttonName);

            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(new String(buttonName.getBytes(), StandardCharsets.UTF_8));
            button.setCallbackData(buttonValue);
            keyboard.add(Arrays.asList(button));
        }

        markup.setKeyboard(keyboard);
        message.setReplyMarkup(markup);
    }

    public void sendImage(String name, Long chatId) {
        SendAnimation animation = new SendAnimation();

        InputFile inputFile = new InputFile();
        inputFile.setMedia(new File("images/" + name + ".gif"));

        animation.setAnimation(inputFile);
        animation.setChatId(chatId);

        executeAsync(animation);
    }

    public int getLevel(Long chatId) {
        return levels.getOrDefault(chatId, 1);
    }

    public void setLevels(Long chatId, int level) {
        levels.put(chatId, level);
    }
}