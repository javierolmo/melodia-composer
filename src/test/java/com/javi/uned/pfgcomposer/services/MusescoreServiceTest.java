package com.javi.uned.pfgcomposer.services;

import com.javi.uned.melodiacore.exceptions.ExportException;
import com.javi.uned.melodiacore.io.export.MelodiaExporter;
import com.javi.uned.melodiacore.model.Instrumento;
import com.javi.uned.melodiacore.model.MelodiaScore;
import com.javi.uned.melodiacore.model.constants.Instrumentos;
import com.javi.uned.melodiacore.model.specs.ScoreSpecs;
import com.javi.uned.pfgcomposer.exceptions.MusescoreException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MusescoreServiceTest {

    @Autowired
    private MusescoreService musescoreService;
    private String xmlPath = "test.musicxml";  //TODO: Meter en resources
    private String pdfPath = "test.pdf";   //TODO: Meter en resources
    private String notExistingFilePath = "notexistingfile.file";

    @BeforeAll
    void setUp() throws ExportException {
        ScoreSpecs specs = new ScoreSpecs();
        specs.setMeasures(1);
        specs.setInstrumentos(new Instrumento[]{Instrumentos.VIOLIN});
        specs.setAuthors(Arrays.asList("Javier Olmo Injerto"));
        MelodiaScore melodiaScore = MelodiaScore.fromSpecs(specs);
        MelodiaExporter.toXML(melodiaScore, xmlPath);

        // Ensure file does not exist
        File file = new File(notExistingFilePath);
        file.delete();
    }

    @Test
    void FileExport_XMLToPDF_Success() throws MusescoreException {
        String resultPDFPath = musescoreService.convertXMLToPDF(xmlPath, pdfPath);
        File pdfFile = new File(resultPDFPath);
        assertTrue(pdfFile.exists());
        assertEquals(pdfPath, resultPDFPath);
    }

    @Test
    void FileExport_XMLToPDF_MissingXML() {

        try{
            File filePDF = new File(pdfPath);
            musescoreService.convertXMLToPDF(notExistingFilePath, filePDF.getAbsolutePath());
        } catch (MusescoreException me) {
            assertTrue(true);
        }
    }

    @AfterAll
    void tearDown() {
        assert new File(xmlPath).delete();
        assert new File(pdfPath).delete();
    }

}