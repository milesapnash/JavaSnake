package com.mapna.snake;

public interface HighScoreStore {
  int load();

  int saveIfHigher(int score);
}
