package com.ndtphong.simpletetrisgame.domain.strategy;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public final class ClassicScoreStrategyTest {
    private ClassicScoreStrategy strategy;

    @Before
    public void setUp() {
        strategy = new ClassicScoreStrategy();
    }

    @Test
    public void oneLine_levelOne_returns100() {
        assertEquals(100, strategy.calculateLineScore(1, 1));
    }

    @Test
    public void fourLines_levelThree_returns2400() {
        assertEquals(2400, strategy.calculateLineScore(4, 3));
    }

    @Test
    public void dropScore_appliesSoftAndHardPoints() {
        assertEquals(17, strategy.calculateDropScore(5, 6));
    }

    @Test
    public void invalidLineCount_returnsZero() {
        assertEquals(1000, strategy.calculateLineScore(5, 1));
    }
}
