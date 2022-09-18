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

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


    @Test
    void happyPath() {
        String planningDate = generateDate(3);
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $x("//form").should(Condition.visible, Duration.ofSeconds(15));

        $x("//*[@data-test-id='notification']").should(Condition.visible, Duration.ofSeconds(50));
        String text = $x("//*[@class='notification__title']").getText();
        assertEquals("Успешно!", text);

        $x("//div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        String planningDateText = $x("//div[@class='notification__content']").getText();
        assertEquals("Встреча успешно забронирована на " + planningDate, planningDateText);

    }
    

    @Test
    void invalidCity() {
        String planningDate = generateDate(3);
        $x("//*[@placeholder='Город']").doubleClick().sendKeys(Keys.DELETE + "Moc");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//*[@data-test-id='city']").getText();
        assertEquals("Доставка в выбранный город недоступна", text);

    }

    @Test
    void invalidNoCity() {
        String planningDate = generateDate(3);
        $x("//*[@placeholder='Город']").setValue("");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//*[@data-test-id='city']").getText();
        assertEquals("Поле обязательно для заполнения", text);

    }

    @Test
    void selectPastDate() {
        String planningDate = generateDate(2);
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").getText();
        assertEquals("Заказ на выбранную дату невозможен", text);

    }

    @Test
    void selectFutureDate() {
        String planningDate = generateDate(4);
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $x("//form").should(Condition.visible, Duration.ofSeconds(15));

        $x("//div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        String planningDateText = $x("//div[@class='notification__content']").getText();
        assertEquals("Встреча успешно забронирована на " + planningDate, planningDateText);

    }

    @Test
    void invalidNoDate() {
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").getText();
        assertEquals("Неверно введена дата", text);

    }

    @Test
    void invalidName() {
        String planningDate = generateDate(3);
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Elena");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text);
    }

    @Test
    void invalidNoName() {
        String planningDate = generateDate(3);
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void invalidPhone() {
        String planningDate = generateDate(3);
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+7999");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@class,'input_invalid')]/span/span[contains(@class,'input__sub')]").getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text);
    }

    @Test
    void invalidNoPhone() {
        String planningDate = generateDate(3);
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        String text = $x("//span[contains(@data-test-id,'phone')]/span/span[@class='input__sub']").getText();
        assertEquals("Поле обязательно для заполнения", text);
    }

    @Test
    void invalidNoClikCheckBox() {
        String planningDate = generateDate(3);
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@class='button__text']").click();

        String text = $x("//label[contains(@class,'input_invalid')]/span[contains(@role,'presentation')]").getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных", text);


    }


}
