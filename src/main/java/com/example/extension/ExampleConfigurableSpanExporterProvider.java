package com.example.extension;

import com.google.auto.service.AutoService;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.traces.ConfigurableSpanExporterProvider;
import io.opentelemetry.sdk.trace.export.SpanExporter;

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
@AutoService(ConfigurableSpanExporterProvider.class)
public class ExampleConfigurableSpanExporterProvider implements ConfigurableSpanExporterProvider {
  @Override
  public SpanExporter createExporter(ConfigProperties config) {
    String endpoint = System.getProperty("otel.exporter.otlp.endpoint");
    if (endpoint == null) {
      System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT");
    }
    if (endpoint == null) {
      endpoint = "http://localhost:4317";
    }

    return OtlpGrpcSpanExporter.builder().setEndpoint(endpoint).build();
  }

  @Override
  public String getName() {
    return "example";
  }
}
