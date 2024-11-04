import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginTest {
    private WebDriver driver;  
    private final String baseUrl = "https://the-internet.herokuapp.com/login";

    @BeforeEach
    public void iniciar() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(baseUrl);
    } 

    @AfterEach
    public void finalizar() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginSuccess() {
        performLogin("tomsmith", "SuperSecretPassword!");
        WebElement successMessage = driver.findElement(By.cssSelector(".flash.success"));
        assertTrue(successMessage.isDisplayed(), "Login com sucesso falhou.");
        assertTrue(successMessage.getText().contains("You logged into a secure area!"), 
            "Mensagem de sucesso não corresponde ao esperado.");
    }       

    @Test
    public void testInvalidUsername() {
        performLogin("invalidUser", "SuperSecretPassword!");
        checkErrorMessage("Your username is invalid!");
    }

    @Test
    public void testInvalidPassword() {
        performLogin("tomsmith", "senhaErrada");
        checkErrorMessage("Your password is invalid!");
    }

    @Test
    public void testBlankUsername() {
        performLogin("", "SuperSecretPassword!");
        checkErrorMessage("Your username is invalid!");
    }

    @Test
    public void testBlankPassword() {
        performLogin("tomsmith", "");
        checkErrorMessage("Your password is invalid!");
    }

    @Test
    public void testBlankUsernameAndPassword() {
        performLogin("", "");
        checkErrorMessage("Your username is invalid!");
    }

    @Test
    public void testInvalidUsernameAndPassword() {
        performLogin("invalidUser", "wrongPassword");
        checkErrorMessage("Your username is invalid!");
    }

    @Test
    public void testBlankUsernameInvalidPassword() {
        performLogin("", "wrongPassword");
        checkErrorMessage("Your username is invalid!");
    }

    @Test
    public void testInvalidUsernameBlankPassword() {
        performLogin("invalidUser", "");
        checkErrorMessage("Your username is invalid!");
    }

    private void performLogin(String username, String password) {
        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        
        usernameField.clear();
        usernameField.sendKeys(username);
        passwordField.clear();
        passwordField.sendKeys(password);
        
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    private void checkErrorMessage(String expectedMessage) {
        WebElement errorMessage = driver.findElement(By.cssSelector(".flash.error"));
        assertTrue(errorMessage.isDisplayed(), "A mensagem de erro não foi exibida.");
        assertTrue(errorMessage.getText().contains(expectedMessage),
            "Mensagem de erro não corresponde ao esperado: " + expectedMessage);
    }
}