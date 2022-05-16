package com.javi.uned.pfgcomposer.kafka.producers;

import com.javi.uned.melodiacore.config.KafkaTopics;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class FileProducer {

    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;


    public void sendXML(String key, String xmlPath) throws IOException {
        File file = new File(xmlPath);
        byte[] rawFile = FileUtils.readFileToByteArray(file);
        kafkaTemplate.send(KafkaTopics.TOPIC_COMPOSER_XML, key, rawFile);
    }

    public void sendPDF(String key, String pdfPath) throws IOException {
        File file = new File(pdfPath);
        byte[] rawFile = FileUtils.readFileToByteArray(file);
        kafkaTemplate.send(KafkaTopics.TOPIC_COMPOSER_PDF, key, rawFile);
    }



}
