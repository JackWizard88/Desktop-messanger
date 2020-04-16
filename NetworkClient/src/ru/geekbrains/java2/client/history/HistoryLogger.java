package ru.geekbrains.java2.client.history;

import ru.geekbrains.java2.client.controller.fxview.FxChatWindow;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryLogger {

    private File historyFile;
    private FxChatWindow clientChat;

    public HistoryLogger(String login, FxChatWindow clientChat) {
        this.clientChat = clientChat;
        try {
            this.historyFile = new File("history_" + login + ".txt");
            if (this.historyFile.createNewFile())
                System.out.println("No existing history file... New file created");
            else
                System.out.println("History file already exists");
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public void SaveHistory(String msg) {

        if (msg.contains("connected") || msg.contains("disconnected")) return;

        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(historyFile, true);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(msg);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void RetrieveHistory() {
        BufferedReader bufferedReader = null;
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(historyFile);
            bufferedReader = new BufferedReader(fileReader);

            List<String> ls = bufferedReader.lines().collect(Collectors.toList());

            int startCount = ls.size() > 100 ? ls.size()-100:0;
            for (int i = startCount; i < ls.size(); i++) {
                clientChat.appendMessage(ls.get(i));
            }

            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearHistory() {
        historyFile.delete();
    }
}
