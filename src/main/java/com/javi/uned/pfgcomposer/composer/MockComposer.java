package com.javi.uned.pfgcomposer.composer;

import com.javi.uned.melodiacore.model.MelodiaScore;
import com.javi.uned.melodiacore.model.specs.ScoreSpecs;
import com.javi.uned.melodiacore.util.MelodiaRandom;

public class MockComposer implements Composer {

    private MelodiaRandom melodiaRandom;

    public MockComposer() {
        this.melodiaRandom = new MelodiaRandom();
    }

    @Override
    public MelodiaScore composeScore(ScoreSpecs scoreSpecs) {
        return melodiaRandom.randomScore(scoreSpecs);
    }
}
