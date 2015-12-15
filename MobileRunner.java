package com.shengpay.fund.mobile.webconsole;

import java.io.File;

import org.apache.log4j.xml.DOMConfigurator;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.plus.webapp.EnvConfiguration;
import org.mortbay.jetty.webapp.Configuration;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;

public class MobileRunner {

	private static Server server = new Server();

	public static void main(String[] args) throws Exception {
        System.setProperty("log.basedir", "c:/logs");
        System.setProperty("log.appender", "STDOUT");
		QueuedThreadPool boundedThreadPool = new QueuedThreadPool();
		boundedThreadPool.setMaxThreads(5);
		server.setThreadPool(boundedThreadPool);
		DOMConfigurator.configure(MobileRunner.class.getResource("/log4j.xml"));
		Connector connector = new SelectChannelConnector();
		connector.setPort(8082);
		server.addConnector(connector);

		WebAppContext context = new WebAppContext("src/main/webapp", "/fund-mobile");
		EnvConfiguration envConfiguration = new EnvConfiguration();
        envConfiguration.setJettyEnvXml(new File("src/test/resources/jetty-env.xml").toURI()
            .toURL());
        Configuration[] configurations = new Configuration[] {
                new org.mortbay.jetty.webapp.WebInfConfiguration(), envConfiguration,
                new org.mortbay.jetty.plus.webapp.Configuration(),
                new org.mortbay.jetty.webapp.JettyWebXmlConfiguration(),
                new org.mortbay.jetty.webapp.TagLibConfiguration() };
        context.setConfigurations(configurations);
        server.setHandler(context);

        WebAppContext testContext = new WebAppContext("src/test/webapp", "/fund-mobile-test");
		EnvConfiguration testEnvConfiguration = new EnvConfiguration();
		testEnvConfiguration.setJettyEnvXml(new File("src/test/resources/jetty-env.xml").toURI()
            .toURL());
        Configuration[] testConfigurations = new Configuration[] {
                new org.mortbay.jetty.webapp.WebInfConfiguration(), testEnvConfiguration,
                new org.mortbay.jetty.plus.webapp.Configuration(),
                new org.mortbay.jetty.webapp.JettyWebXmlConfiguration(),
                new org.mortbay.jetty.webapp.TagLibConfiguration() };
        testContext.setConfigurations(testConfigurations);
        server.addHandler(testContext);

		server.setStopAtShutdown(true);
		server.setSendServerVersion(true);

		server.start();
		System.out.println("===========================server started=========================");
		server.join();

	}
}