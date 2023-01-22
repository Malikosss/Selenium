package ru.netologe.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestOrderCard {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:7777");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void shouldTest() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мария Кислицина");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79030248197");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expected = "Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";

        Assertions.assertEquals(expected, actual);


    }

    @Test
    public void wrongEnglishName() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Fhdeyug");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79030248197");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        Assertions.assertEquals(expected, actual);


    }

    @Test
    public void wrongNameWithNumber() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Елизавета 2");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79030248197");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        Assertions.assertEquals(expected, actual);


    }

    @Test
    public void withoutName() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79030248197");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void nameWithSpaces() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("  ");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79030248197");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void withoutNumber() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мария Кислицина");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("");
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Поле обязательно для заполнения";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void moreNumber() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мария Кислицина");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+7903024819754");
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void lessNumber() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мария Кислицина");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79030");
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void numberWithEight() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мария Кислицина");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("89030223123");
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void withoutClickCheckbox() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мария Кислицина");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79030248197");
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText().trim();
        String expected = "Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void twoFail() {

        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("FFF");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79030");
        driver.findElement(By.className("button__content")).click();

        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";

        Assertions.assertEquals(expected, actual);
    }
}

