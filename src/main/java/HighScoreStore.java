public interface HighScoreStore {
  int load();

  int saveIfHigher(int score);
}
