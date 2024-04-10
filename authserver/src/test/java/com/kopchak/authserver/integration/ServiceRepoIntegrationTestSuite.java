package com.kopchak.authserver.integration;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"com.kopchak.authserver.integration.repository", "com.kopchak.authserver.integration.service"})
public class ServiceRepoIntegrationTestSuite {
}
