package com.javi.uned.pfgcomposer.composer;

import com.javi.uned.melodiacore.model.MelodiaScore;
import com.javi.uned.melodiacore.model.specs.ScoreSpecs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MockComposerTest {

    private Composer mockComposer;

    @BeforeEach
    void setUp() {
        this.mockComposer = new MockComposer();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void componer() {
        MelodiaScore melodiaScore = mockComposer.composeScore(ScoreSpecs.builder().build());
        System.out.println("Composed");
    }
}