package com.example.tracing.hello.service;

import io.jaegertracing.Configuration;
import io.jaegertracing.internal.samplers.ConstSampler;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

public class InitTracer {

    public void init() {
        Configuration.SamplerConfiguration samplerConfiguration = new Configuration.SamplerConfiguration()
                .withType(ConstSampler.TYPE).withParam(1);
        Configuration.SenderConfiguration senderConfiguration = new Configuration.SenderConfiguration()
                .withAgentHost("localhost").withAgentPort(5775);
        Configuration.ReporterConfiguration reporterConfiguration = new Configuration.ReporterConfiguration()
                .withMaxQueueSize(1000).withFlushInterval(1000).withSender(senderConfiguration);

        Tracer tracer = new Configuration("apim-backend").withSampler(samplerConfiguration)
                .withReporter(reporterConfiguration).getTracer();
        GlobalTracer.register(tracer);

    }
}
