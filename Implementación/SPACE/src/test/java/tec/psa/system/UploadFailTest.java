package tec.psa.system;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UploadFailTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    System.setProperty("webdriver.gecko.driver", "test_files/geckodriver.exe");
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8080/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testUploadFail() throws Exception {
    final WebDriverWait wait = new WebDriverWait(driver, 10);
    driver.get(baseUrl + "/");
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("usertest");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("usertest");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Subir lote")));
    driver.findElement(By.linkText("Subir lote")).click();
    driver.findElement(By.id("loteNombre")).clear();
    driver.findElement(By.id("loteNombre")).sendKeys("Lote Nuevo");
    driver.findElement(By.id("uploadButton")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upload_res")));
    
    assertEquals("No hay archivos para subir", 
        driver.findElement(By.id("upload_res")).getText());    
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
