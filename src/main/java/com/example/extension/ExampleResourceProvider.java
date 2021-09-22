package com.example.extension;

import com.google.auto.service.AutoService;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ResourceProvider;
import io.opentelemetry.sdk.resources.Resource;

@AutoService(ResourceProvider.class)
public class ExampleResourceProvider implements ResourceProvider {
  @Override
  public Resource createResource(ConfigProperties config) {

    String serviceName = System.getProperty("otel.service.name");
    if (serviceName == null) {
      System.getenv("OTEL_SERVICE_NAME");
    }
    if (serviceName == null) {
      return Resource.empty();
    }

    return Resource.builder().put("service.name", serviceName).build();
  }
}
