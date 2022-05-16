package com.javi.uned.pfgcomposer.composer;


import com.javi.uned.melodiacore.model.Instrumento;
import com.javi.uned.melodiacore.model.MelodiaScore;
import com.javi.uned.melodiacore.model.parts.MelodiaPart;
import com.javi.uned.melodiacore.model.specs.ScoreSpecs;

import java.util.ArrayList;
import java.util.List;

public class GeneticComposer implements Composer {

    @Override
    public MelodiaScore composeScore(ScoreSpecs scoreSpecs) {

        List<MelodiaPart> parts = new ArrayList<>();

        for (Instrumento instrumento : scoreSpecs.getInstrumentos()) {
            MelodiaPart melodiaPart = composePart(instrumento, scoreSpecs);
            parts.add(melodiaPart);
        }

        MelodiaScore melodiaScore = new MelodiaScore(
                parts,
                scoreSpecs.getMovementTitle(),
                scoreSpecs.getMovementNumber(),
                scoreSpecs.getAuthors()
        );
        return melodiaScore;
    }

    public MelodiaPart composePart(Instrumento instrumento, ScoreSpecs scoreSpecs) {

    }
}
