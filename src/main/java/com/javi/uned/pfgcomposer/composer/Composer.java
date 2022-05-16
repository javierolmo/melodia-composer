package com.javi.uned.pfgcomposer.composer;

import com.javi.uned.melodiacore.model.Estructura;
import com.javi.uned.melodiacore.model.MelodiaScore;
import com.javi.uned.melodiacore.model.specs.ScoreSpecs;

public interface Composer {

    MelodiaScore composeScore(ScoreSpecs scoreSpecs);

    Estructura composeStructure(ScoreSpecs scoreSpecs);

}
