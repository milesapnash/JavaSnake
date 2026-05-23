package com.mapna.snake;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHighScoreStore implements HighScoreStore {
  private final Path scorePath;

  public FileHighScoreStore(String fileName) {
    this.scorePath = Path.of(fileName);
  }

  @Override
  public int load() {
    if (!Files.exists(scorePath)) {
      return -1;
    }
    try {
      String value = Files.readString(scorePath).trim();
      if (value.isEmpty()) {
        return -1;
      }
      return Integer.parseInt(value);
    } catch (IOException | NumberFormatException e) {
      System.err.println("Failed to load high score: " + e.getMessage());
      return -1;
    }
  }

  @Override
  public int saveIfHigher(int score) {
    int current = load();
    int next = Math.max(current, score);
    if (next != current) {
      try {
        Files.writeString(scorePath, Integer.toString(next));
      } catch (IOException e) {
        System.err.println("Failed to save high score: " + e.getMessage());
        return current;
      }
    }
    return next;
  }
}
