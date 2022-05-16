package com.javi.uned.pfgcomposer.composer.composers.structural;

import com.javi.uned.melodiacore.model.constants.Tonalidades;
import com.javi.uned.melodiacore.model.structure.PhraseStructure;
import com.javi.uned.melodiacore.model.structure.ScoreStructure;

import java.util.Map;

public class Structures {

    public static ScoreStructure ragtimeClasico() {
        ScoreStructure scoreStructure = new ScoreStructure();
        Map<String, PhraseStructure> phraseMap = scoreStructure.getPhraseMap();
        phraseMap.put("INTRO", new PhraseStructure(Tonalidades.DO_M, 4));
        phraseMap.put("A", new PhraseStructure(Tonalidades.DO_M, 8));
        phraseMap.put("A", new PhraseStructure(Tonalidades.DO_M, 8));
        phraseMap.put("B", new PhraseStructure(Tonalidades.DO_M, 8));
        phraseMap.put("B", new PhraseStructure(Tonalidades.DO_M, 8));
        phraseMap.put("A", new PhraseStructure(Tonalidades.DO_M, 8));
        phraseMap.put("C", new PhraseStructure(Tonalidades.DO_M, 8));
        phraseMap.put("C", new PhraseStructure(Tonalidades.DO_M, 8));
        phraseMap.put("CODA", new PhraseStructure(Tonalidades.DO_M, 8));
        phraseMap.put("D", new PhraseStructure(Tonalidades.DO_M, 16));
        phraseMap.put("D", new PhraseStructure(Tonalidades.DO_M, 16));
        return scoreStructure;
    }
}
