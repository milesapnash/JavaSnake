package com.mapna.snake;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardConfigTest {

  @Test
  void tickRateUnchangedBelowThreshold() {
    for (int g = 0; g <= BoardConfig.SPEEDUP_THRESHOLD; g++) {
      assertEquals(BoardConfig.TICK_RATE_MS, BoardConfig.tickRateMs(g));
    }
  }

  @Test
  void tickRateDecreasesAboveThreshold() {
    int g = BoardConfig.SPEEDUP_THRESHOLD + 1;
    assertEquals(BoardConfig.TICK_RATE_MS - BoardConfig.SPEED_STEP_MS, BoardConfig.tickRateMs(g));
  }

  @Test
  void tickRateFloorsAtMinimum() {
    assertEquals(BoardConfig.MIN_TICK_RATE_MS, BoardConfig.tickRateMs(1000));
  }
}
