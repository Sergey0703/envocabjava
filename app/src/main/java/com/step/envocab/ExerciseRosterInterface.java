package com.step.envocab;

public interface ExerciseRosterInterface {
    void onItemClick(int position) throws ClassNotFoundException;

    void sendData(String id_ex, String name);
}
