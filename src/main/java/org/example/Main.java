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

            SendMessage message = createMessage("Ґа-ґа-ґа!\n" +
                    "Вітаємо у боті біолабораторії «Батько наш Бандера».\n" +
                    "\n" +
                    "Ти отримуєш гусака №71\n" +
                    "\n" +
                    "Цей бот ми створили для того, щоб твій гусак прокачався з рівня звичайної свійської худоби до рівня біологічної зброї, здатної нищити ворога. \n" +
                    "\n" +
                    "Щоб звичайний гусак перетворився на бандерогусака, тобі необхідно:\n" +
                    "✔\uFE0Fвиконувати завдання\n" +
                    "✔\uFE0Fпереходити на наступні рівні\n" +
                    "✔\uFE0Fзаробити достатню кількість монет, щоб придбати Джавеліну і зробити свєрхтра-та-та.\n" +
                    "\n" +
                    "*Гусак звичайний.* Стартовий рівень.\n" +
                    "Бонус: 5 монет.\n" +
                    "Обери завдання, щоб перейти на наступний рівень");
            message.setChatId(chatId);

            attachButtons(message, Map.of(
                    "Сплести маскувальну сітку (+15 монет)", "level-1-task",
                    "Зібрати кошти патріотичними піснями (+15 монет)", "level-1-task",
                    "Вступити в Міністерство Мемів України (+15 монет)", "level-1-task"
            ));

            sendApiMethodAsync(message);
        }

        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().equals("level-1-task") && getLevel(chatId) == 1) {
                setLevel(chatId, 2);
                sendImage("level-2", chatId);

                SendMessage message = createMessage("*Вітаємо на другому рівні! Твій гусак - біогусак.*\n" +
                        "Баланс: 20 монет. \n" +
                        "Обери завдання, щоб перейти на наступний рівень");
                message.setChatId(chatId);

                attachButtons(message, Map.of(
                        "Зібрати комарів для нової біологічної зброї (+15 монет)", "level-2-task",
                                "Пройти курс молодого бійця (+15 монет)", "level-2-task",
                                "Задонатити на ЗСУ (+15 монет)", "level-2-task"
                ));
                sendApiMethodAsync(message);
            }

            if (update.getCallbackQuery().getData().equals("level-2-task") && getLevel(chatId) == 2) {
                setLevel(chatId, 3);
                sendImage("level-3", chatId);

                SendMessage message = createMessage("*Вітаємо на третьому рівні! Твій гусак - бандеростажер.*\n" +
                        "Баланс: 35 монет. \n" +
                        "Обери завдання, щоб перейти на наступний рівень");
                message.setChatId(chatId);
                attachButtons(message, Map.of(
                        "Злітати на тестовий рейд по чотирьох позиціях (+15 монет)", "level-3-task",
                        "Відвезти гуманітарку на передок (+15 монет)", "level-3-task",
                        "Знайти зрадника та здати в СБУ (+15 монет)", "level-3-task"
                ));
                sendApiMethodAsync(message);
            }
        }
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

    public void setLevel(Long chatId, int level) {
        levels.put(chatId, level);
    }
}