package com.example.extension;

import com.google.auto.service.AutoService;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.autoconfigure.spi.SdkTracerProviderConfigurer;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

import java.lang.reflect.Field;
import java.util.List;

// NOTE: it's recommended for now to disable live metrics in the applicationinsights.json
// configuration file when disabling the application insights span exporter, in order to avoid
// confusion since live metrics won't see span data currently (in the future, capturing live metrics
// will be moved to its own SpanProcessor so that it can be used independently of the application
// insights span exporter)
//
// {
//   "preview": {
//     "liveMetrics": {
//       "enabled": false
//     }
//   }
// }
@AutoService(SdkTracerProviderConfigurer.class)
public class ExampleSdkTracerProviderConfigurer implements SdkTracerProviderConfigurer {
  @Override
  public void configure(SdkTracerProviderBuilder tracerProvider) {

    getSpanProcessors(tracerProvider)
        .removeIf(spanProcessor -> spanProcessor instanceof BatchSpanProcessor);

    // TODO(trask): pass along otel.exporter.* properties so they can be used to configure
    OtlpGrpcSpanExporter otlpExporter =
        OtlpGrpcSpanExporter.builder().setEndpoint("http://localhost:8080").build();
    tracerProvider.addSpanProcessor(BatchSpanProcessor.builder(otlpExporter).build());
  }

  private List<?> getSpanProcessors(SdkTracerProviderBuilder tracerProvider) {
    try {
      Field spanProcessorsField = SdkTracerProviderBuilder.class.getDeclaredField("spanProcessors");
      spanProcessorsField.setAccessible(true);
      return (List<?>) spanProcessorsField.get(tracerProvider);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
