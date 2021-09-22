package com.example.extension;

import com.google.auto.service.AutoService;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.metrics.ConfigurableMetricExporterProvider;
import io.opentelemetry.sdk.metrics.export.MetricExporter;

@AutoService(ConfigurableMetricExporterProvider.class)
public class ExampleConfigurableMetricExporterProvider
    implements ConfigurableMetricExporterProvider {
  @Override
  public MetricExporter createExporter(ConfigProperties config) {
    String endpoint = System.getProperty("otel.exporter.otlp.endpoint");
    if (endpoint == null) {
      System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT");
    }
    if (endpoint == null) {
      endpoint = "http://localhost:4317";
    }

    return OtlpGrpcMetricExporter.builder().setEndpoint(endpoint).build();
  }

  @Override
  public String getName() {
    return "example";
  }
}
