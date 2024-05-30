package com.ruoyi.web.bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class ExecBot extends TelegramLongPollingBot {

    private String token = "6934016186:AAFQcxetjswv3IZJ6B2SuzXSxLE6ey6rvC0";
    private String botUsername = "TopRechargeNotifier";

    public static final String CHAIN_ID = "@topioRechargeManager";

    public ExecBot() {
        this(new DefaultBotOptions());
    }

    public ExecBot(DefaultBotOptions options) {
        super(options);
    }


    @Override
    public String getBotUsername() {
        return this.botUsername;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage message = SendMessage.builder()
                .text("hello!")
                .chatId(update.getMessage().getChatId().toString())
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }

    //回复普通文本消息
    public void sendMsg(String text, String chatId) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        try {
            execute(response);
        } catch (TelegramApiException e) {
        }
    }

    public void sendRechargeMessage(String msg){
        sendMsg(msg,CHAIN_ID);
    }

    public static ExecBot genExecBotByProxy() {
        //梯子在自己电脑上就写127.0.0.1  软路由就写路由器的地址
        String proxyHost = "127.0.0.1";
        //端口根据实际情况填写，说明在上面，自己看
        int proxyPort = 10080;

        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyHost(proxyHost);
        botOptions.setProxyPort(proxyPort);
        //注意一下这里，ProxyType是个枚举，看源码你就知道有NO_PROXY,HTTP,SOCKS4,SOCKS5;
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);

        DefaultBotSession defaultBotSession = new DefaultBotSession();
        defaultBotSession.setOptions(botOptions);
        //需要代理
        ExecBot bot = new ExecBot(botOptions);
        return bot;
    }

    public static ExecBot genExecBot() {
        ExecBot bot2 = new ExecBot();
        return bot2;
    }

    public static void main(String[] args) {

        //梯子在自己电脑上就写127.0.0.1  软路由就写路由器的地址
        String proxyHost = "127.0.0.1";
        //端口根据实际情况填写，说明在上面，自己看
        int proxyPort = 10080;

        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyHost(proxyHost);
        botOptions.setProxyPort(proxyPort);
        //注意一下这里，ProxyType是个枚举，看源码你就知道有NO_PROXY,HTTP,SOCKS4,SOCKS5;
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);

        DefaultBotSession defaultBotSession = new DefaultBotSession();
        defaultBotSession.setOptions(botOptions);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(defaultBotSession.getClass());


            //需要代理
            ExecBot bot = new ExecBot(botOptions);
            telegramBotsApi.registerBot(bot);
            //不需代理
//            ExecBot bot2 = new ExecBot();
//            telegramBotsApi.registerBot(bot2);
            bot.sendMsg("你好啊", "@topioRechargeManager");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
