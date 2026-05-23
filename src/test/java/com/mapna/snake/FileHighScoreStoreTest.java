package com.mapna.snake;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileHighScoreStoreTest {

  @TempDir
  Path tempDir;

  private FileHighScoreStore storeAt(String filename) {
    return new FileHighScoreStore(tempDir.resolve(filename).toString());
  }

  @Test
  void loadReturnsNegativeOneWhenFileDoesNotExist() {
    assertEquals(-1, storeAt("missing.txt").load());
  }

  @Test
  void loadReturnsNegativeOneWhenFileIsEmpty() throws IOException {
    Files.writeString(tempDir.resolve("empty.txt"), "");
    assertEquals(-1, storeAt("empty.txt").load());
  }

  @Test
  void loadReturnsNegativeOneWhenFileContainsNonNumeric() throws IOException {
    Files.writeString(tempDir.resolve("corrupt.txt"), "not_a_number");
    assertEquals(-1, storeAt("corrupt.txt").load());
  }

  @Test
  void loadParsesValidScore() throws IOException {
    Files.writeString(tempDir.resolve("score.txt"), "42");
    assertEquals(42, storeAt("score.txt").load());
  }

  @Test
  void loadTrimsWhitespace() throws IOException {
    Files.writeString(tempDir.resolve("padded.txt"), "  17\n");
    assertEquals(17, storeAt("padded.txt").load());
  }

  @Test
  void saveIfHigherWritesScoreWhenNoFileExists() {
    FileHighScoreStore store = storeAt("new.txt");

    int result = store.saveIfHigher(10);

    assertEquals(10, result);
    assertEquals(10, store.load());
  }

  @Test
  void saveIfHigherUpdatesWhenScoreIsHigher() throws IOException {
    Files.writeString(tempDir.resolve("hs.txt"), "5");
    FileHighScoreStore store = storeAt("hs.txt");

    int result = store.saveIfHigher(20);

    assertEquals(20, result);
    assertEquals(20, store.load());
  }

  @Test
  void saveIfHigherKeepsExistingWhenScoreIsLower() throws IOException {
    Files.writeString(tempDir.resolve("hs.txt"), "50");
    FileHighScoreStore store = storeAt("hs.txt");

    int result = store.saveIfHigher(10);

    assertEquals(50, result);
    assertEquals(50, store.load());
  }

  @Test
  void saveIfHigherKeepsExistingWhenScoreIsEqual() throws IOException {
    Files.writeString(tempDir.resolve("hs.txt"), "25");
    FileHighScoreStore store = storeAt("hs.txt");

    int result = store.saveIfHigher(25);

    assertEquals(25, result);
    assertEquals(25, store.load());
  }

  @Test
  void saveIfHigherHandlesCorruptFileAsNoScore() throws IOException {
    Files.writeString(tempDir.resolve("bad.txt"), "garbage");
    FileHighScoreStore store = storeAt("bad.txt");

    int result = store.saveIfHigher(7);

    assertEquals(7, result);
    assertEquals(7, store.load());
  }
}
