package com.step.envocab;

public interface WordRosterInterface {
    void onItemClick(int position, String act);

    void sendData(String id, String word, String translate, String transcript, Boolean train1);
}
