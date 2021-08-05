package com.example.extension;

import com.google.auto.service.AutoService;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.autoconfigure.spi.SdkTracerProviderConfigurer;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

@AutoService(SdkTracerProviderConfigurer.class)
public class ExampleSdkTracerProviderConfigurer implements SdkTracerProviderConfigurer {
  @Override
  public void configure(SdkTracerProviderBuilder tracerProvider) {
    OtlpGrpcSpanExporter otlpExporter = OtlpGrpcSpanExporter.builder().setEndpoint("http://localhost:8080").build();
    tracerProvider
        .addSpanProcessor(BatchSpanProcessor.builder(otlpExporter).build());
  }
}
