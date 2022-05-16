package com.javi.uned.pfgcomposer.kafka.consumers;

import com.javi.uned.melodiacore.config.KafkaTopics;
import com.javi.uned.melodiacore.exceptions.ExportException;
import com.javi.uned.melodiacore.io.export.MelodiaExporter;
import com.javi.uned.melodiacore.model.MelodiaScore;
import com.javi.uned.melodiacore.model.specs.ScoreSpecs;
import com.javi.uned.pfgcomposer.composer.Composer;
import com.javi.uned.pfgcomposer.composer.MockComposer;
import com.javi.uned.pfgcomposer.exceptions.MusescoreException;
import com.javi.uned.pfgcomposer.exceptions.SpecsConsumerException;
import com.javi.uned.pfgcomposer.kafka.producers.FileProducer;
import com.javi.uned.pfgcomposer.services.MusescoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class RetryXMLConsumer {


    private final Logger logger = LoggerFactory.getLogger(RetryXMLConsumer.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private FileProducer fileProducer;
    @Autowired
    private MusescoreService musescoreService;
    private Composer composer = new MockComposer(); //TODO: Reemplazar

    @KafkaListener(topics = KafkaTopics.TOPIC_WEBSERVICE_RETRYXML, groupId = "0", containerFactory = "specsKafkaListenerFactory")
    public void consume(ScoreSpecs scoreSpecs, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        File xmlFile = null;
        File pdfFile = null;
        try{
            kafkaTemplate.send(KafkaTopics.TOPIC_WEBSERVICE_RETRYXML, key, "Componiendo");
            MelodiaScore melodiaScore = composer.composeScore(scoreSpecs);
            String xmlPath = generarXML(melodiaScore, key);
            kafkaTemplate.send(KafkaTopics.TOPIC_WEBSERVICE_RETRYXML, key, "Terminado!");
            kafkaTemplate.send(KafkaTopics.TOPIC_WEBSERVICE_RETRYPDF, key, "Porfa reintenta");
            logger.info("Composición finalizada. ID={}", key);
        } catch (SpecsConsumerException e) {
            logger.error("SpecsConsumer.consume: Error al generar partitura", e);
            kafkaTemplate.send(KafkaTopics.TOPIC_WEBSERVICE_RETRYXML, key, e.getMessage());
        } finally { // Clean up
            try {
                if (xmlFile != null && xmlFile.exists()) Files.delete(xmlFile.toPath());
                if (pdfFile != null && pdfFile.exists()) Files.delete(pdfFile.toPath());
            } catch (IOException ioe) {
                logger.warn("No se ha podido borrar un archivo temporal", ioe);
            }
        }
    }

    private String generarXML(MelodiaScore melodiaScore, String key) throws SpecsConsumerException {
        String xmlPath = String.format("%s.musicxml");
        try{
            kafkaTemplate.send(KafkaTopics.TOPIC_WEBSERVICE_RETRYXML, key, "Generando fichero .musicxml");
            MelodiaExporter.toXML(melodiaScore, xmlPath);
            fileProducer.sendXML(key, xmlPath);
            return xmlPath;
        } catch (IOException | ExportException e) {
            throw new SpecsConsumerException("SpecsConsumer.generarXML: Error al generar XML", e);
        }
    }

    /**
     *
     * @param xmlPath
     * @param key
     * @return PDF file path
     * @throws SpecsConsumerException
     */
    private String generarPDF(String xmlPath, String key) throws SpecsConsumerException {
        File xmlFile = new File(xmlPath);

        if(!xmlFile.exists()) { // Check file exists
            throw new SpecsConsumerException("SpecsConsumer.generarPDF: No se ha podido crear el PDF. No existe el archivo " + xmlFile.getAbsolutePath());
        }

        String pdfPath = String.format("%s.musicxml");

        try{
            kafkaTemplate.send(KafkaTopics.TOPIC_WEBSERVICE_RETRYXML, key, "Generando fichero .pdf");
            pdfPath = musescoreService.convertXMLToPDF(xmlFile.getAbsolutePath(), pdfPath);
            fileProducer.sendPDF(key, pdfPath);
            return pdfPath;
        } catch (MusescoreException e) {
            throw new SpecsConsumerException("SpecsConsumer.generarPDF: error al generar PDF", e);
        } catch (IOException e) {
            throw new SpecsConsumerException("SpecsConsumer.generarPDF: error al guardar PDF", e);
        }
    }

}
