package com.step.envocab;

public interface GroupWordsRosterInterface {
    void onItemClick(int position);

    void sendData(String id, String word, String translate, String transcript, Boolean train1);
}
