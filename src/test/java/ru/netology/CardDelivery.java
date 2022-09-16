package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDelivery {

    @BeforeEach
    void setupAll() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }


    @Test
    void happyPath() {
        $x("//*[@placeholder='Город']").setValue("Москва");
        String minData = String.valueOf(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + minData);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $x("//form").should(Condition.visible, Duration.ofSeconds(15));

        $x("//*[@data-test-id='notification']").should(Condition.visible, Duration.ofSeconds(50));
        String text = $x("//*[@class='notification__title']").getText();
        assertEquals("Успешно!", text);

    }

    @Test
    void invalidCity() {
        $x("//*[@placeholder='Город']").setValue("Мос");
        $x("//*[@class='button__text']").click();
        String text = $x("//*[@data-test-id='city']").getText();
        assertEquals("Доставка в выбранный город недоступна", text);

    }

    @Test
    void invalidNoCity() {
        $x("//*[@placeholder='Город']").setValue("");
        $x("//*[@class='button__text']").click();
        String text = $x("//*[@data-test-id='city']").getText();
        assertEquals("Поле обязательно для заполнения", text);

    }

    @Test
    void selectPastDate() {
        $x("//*[@placeholder='Город']").setValue("Москва");
        String minData = String.valueOf(LocalDate.now().minusDays(30).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + minData);
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").getText();
        assertEquals("Заказ на выбранную дату невозможен", text);

    }

    @Test
    void invalidNoDate() {
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE);
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").getText();
        assertEquals("Неверно введена дата", text);

    }

    @Test
    void invalidName() {
        $x("//*[@placeholder='Город']").setValue("Москва");
        String minData = String.valueOf(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + minData);
        $x("//*[@name='name']").setValue("Elena");
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void invalidNoName() {
        $x("//*[@placeholder='Город']").setValue("Москва");
        String minData = String.valueOf(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + minData);
        $x("//*[@name='name']").setValue("");
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void invalidPhone() {
        $x("//*[@placeholder='Город']").setValue("Москва");
        String minData = String.valueOf(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + minData);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+7999");
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@class,'input_invalid')]/span/span[contains(@class,'input__sub')]").getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void invalidNoPhone() {
        $x("//*[@placeholder='Город']").setValue("Москва");
        String minData = String.valueOf(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + minData);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("");
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'phone')]/span/span[@class='input__sub']").getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void invalidNoClikCheckBox() {

        $x("//*[@placeholder='Город']").setValue("Москва");
        String minData = String.valueOf(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + minData);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");

        $x("//*[@class='button__text']").click();

        String text = $x("//label[contains(@class,'input_invalid')]/span[contains(@role,'presentation')]").getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных", text);


    }


}
