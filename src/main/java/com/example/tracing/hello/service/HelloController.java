package com.example.tracing.hello.service;

import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.propagation.*;
import io.opentracing.util.GlobalTracer;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping ("/hello")
    String hello(@RequestHeader HttpHeaders headers, @RequestParam String name) {
        Tracer tracer = GlobalTracer.get();

        //extract span context from headers
        Map<String, String> spanContextCarrier = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            for (String headerValue : entry.getValue()) {
                spanContextCarrier.put(headerName, headerValue);
            }
        }

        SpanContext spanContext = tracer.extract(Format.Builtin.HTTP_HEADERS,
                new TextMapExtractAdapter(spanContextCarrier));

        //start hello-service span as a child of the extracted span context
        Span span = tracer.buildSpan("hello-service").asChildOf(spanContext).start();

        String response = "{ 'response' : 'hello " + name + "'  }";

        //finish span
        span.finish();
        return response;
    }
}
