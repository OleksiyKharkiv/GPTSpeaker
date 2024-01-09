package org.example.gptspeaker.service;

public interface SpeechRecognitionService {
    String recognizeSpeech(byte[] audioData);
}