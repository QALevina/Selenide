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

        $x("//*[@class='notification__title']").shouldHave(Condition.text("Успешно!"), Duration.ofSeconds(15)).shouldBe(Condition.visible);
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);


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

        $x("//*[@data-test-id='city']").shouldHave(Condition.text("Доставка в выбранный город недоступна"), Duration.ofSeconds(15)).shouldBe(Condition.visible);


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

        $x("//*[@data-test-id='city']").shouldHave(Condition.text("Поле обязательно для заполнения"), Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").shouldHave(Condition.text("Заказ на выбранную дату невозможен"), Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

    }

    @Test
    void invalidNoDate() {
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();

        $x("//span[contains(@data-test-id,'date')]//span[@class='input__sub']").shouldHave(Condition.text("Неверно введена дата"), Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").shouldHave(Condition.text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."), Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@data-test-id,'name')]/span/span[contains(@class,'input__sub')]").shouldHave(Condition.text("Поле обязательно для заполнения"), Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@class,'input_invalid')]/span/span[contains(@class,'input__sub')]").shouldHave(Condition.text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."), Duration.ofSeconds(15)).shouldBe(Condition.visible);

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

        $x("//span[contains(@data-test-id,'phone')]/span/span[@class='input__sub']").shouldHave(Condition.text("Поле обязательно для заполнения"), Duration.ofSeconds(15)).shouldBe(Condition.visible);

    }

    @Test
    void invalidNoClikCheckBox() {
        String planningDate = generateDate(3);
        $x("//*[@placeholder='Город']").setValue("Москва");
        $x("//*[@data-test-id='date']//self::input").doubleClick().sendKeys(Keys.DELETE + planningDate);
        $x("//*[@name='name']").setValue("Елена Левина-Иванова");
        $x("//*[@name='phone']").setValue("+79998887766");
        $x("//*[@class='button__text']").click();

        $x("//label[contains(@class,'input_invalid')]/span[contains(@role,'presentation')]").shouldHave(Condition.text("Я соглашаюсь с условиями обработки и использования моих персональных данных"), Duration.ofSeconds(15)).shouldBe(Condition.visible);

    }


}
