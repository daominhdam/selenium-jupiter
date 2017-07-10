/*
 * (C) Copyright 2017 Boni Garcia (http://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia.test.advance;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.grid.selenium.GridLauncherV3;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.Capability;
import io.github.bonigarcia.DriverCapabilities;
import io.github.bonigarcia.DriverUrl;
import io.github.bonigarcia.SeleniumExtension;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;

/**
 * Test with Remote WebDriver.
 *
 * @author Boni Garcia (boni.gg@gmail.com)
 * @since 1.0.0
 */
@ExtendWith(SeleniumExtension.class)
public class RemoteJupiterTest {

    @BeforeAll
    static void setup() throws Exception {
        // Start hub
        GridLauncherV3.main(new String[] { "-role", "hub", "-port", "4444" });

        // Register Chrome in hub
        ChromeDriverManager.getInstance().setup();
        GridLauncherV3.main(new String[] { "-role", "node", "-hub",
                "http://localhost:4444/grid/register", "-browser",
                "browserName=chrome,version=59", "-port", "5555" });

        // Register Firefox in hub
        FirefoxDriverManager.getInstance().setup();
        GridLauncherV3.main(new String[] { "-role", "node", "-hub",
                "http://localhost:4444/grid/register", "-browser",
                "browserName=firefox,version=54", "-port", "5556" });
    }

    @Test
    void testWithoutUrl(RemoteWebDriver remoteWebDriver) {
        assertThrows(NullPointerException.class, () -> {
            exercise(remoteWebDriver);
        });
    }

    @Test
    void testWithChrome(
            @DriverUrl("http://localhost:4444/wd/hub") @DriverCapabilities(capability = {
                    @Capability(name = "browserName", value = "chrome"),
                    @Capability(name = "version", value = "59") }) RemoteWebDriver remoteChrome)
            throws InterruptedException {
        exercise(remoteChrome);
    }

    @Test
    void testWithFirefox(
            @DriverUrl("http://localhost:4444/wd/hub") @DriverCapabilities(capability = {
                    @Capability(name = "browserName", value = "firefox") }) RemoteWebDriver remoteFirefox)
            throws InterruptedException {
        exercise(remoteFirefox);
    }

    @Test
    void testWithChromeAndFirefox(
            @DriverUrl("http://localhost:4444/wd/hub") @DriverCapabilities(capability = {
                    @Capability(name = "browserName", value = "chrome") }) RemoteWebDriver remoteChrome,
            @DriverUrl("http://localhost:4444/wd/hub") @DriverCapabilities(capability = {
                    @Capability(name = "browserName", value = "firefox") }) RemoteWebDriver remoteFirefox)
            throws InterruptedException {
        exercise(remoteChrome);
        exercise(remoteFirefox);
    }

    void exercise(WebDriver webdriver) {
        webdriver.get("http://www.seleniumhq.org/");
        String title = webdriver.getTitle();

        assertTrue(title.equals("Selenium - Web Browser Automation"));
    }

}
