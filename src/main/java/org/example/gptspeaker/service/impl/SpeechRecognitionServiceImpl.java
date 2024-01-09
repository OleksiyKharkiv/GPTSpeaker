package org.example.gptspeaker.service.impl;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import org.example.gptspeaker.service.SpeechRecognitionService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class SpeechRecognitionServiceImpl implements SpeechRecognitionService {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(SpeechRecognitionServiceImpl.class);

    @Override
    public String recognizeSpeech(byte[] audioData) {
        try (SpeechClient speechClient = SpeechClient.create()) {
            ByteString audioBytes = ByteString.copyFrom(audioData);

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            SpeechRecognitionResult result = speechClient.recognize(config, audio).getResults(0);
            StringBuilder recognizedText = new StringBuilder();

            for (SpeechRecognitionAlternative alternative : result.getAlternativesList()) {
                recognizedText.append(alternative.getTranscript()).append(" ");
            }

            return recognizedText.toString().trim();
        } catch (Exception e) {
            LOGGER.warning("Error during speech recognition");
            return "Ошибка при распознавании речи";
        }
    }
}