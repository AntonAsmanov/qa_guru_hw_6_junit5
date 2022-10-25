package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class ThomannTests {

    @BeforeAll
    static void setUp(){
        Configuration.baseUrl = "https://www.thomann.de";
    }

    @CsvSource(value = {
            "Deutschland, DE",
            "United Kingdom, EN",
            "Suomi, Fi"})
    @ParameterizedTest(name = "Проверка смены языка при выборе страны {0}")
    void thomannChangeCountry(String country, String expectedLang){
        open("/de/index.html");
        executeJavaScript("$('.js-accept-all-cookies').click();");
        $(".user-navigation__item").click();
        $$(".country-item").findBy(text(country)).click();
        $(".shop-selection-button").click();

        $(".shop-country__stats").shouldHave(text(expectedLang));
    }

    @ValueSource(strings = {"Gui\u00ADtars and Basses", "Drums and Per\u00ADcus\u00ADsion", "Keys", "Stu\u00ADdio and Re\u00ADcord\u00ADing Equip\u00ADment"})
    @ParameterizedTest(name = "Проверка выбора категории {0}")
    void thomannOpenCategory(String testData){
        open("/gb/cat.html");
        executeJavaScript("$('.js-accept-all-cookies').click();");
        $$("h2.headline__title").findBy(text(testData)).scrollTo().click();

        $("h1.category-header__title").shouldHave(text(testData));
    }

    @MethodSource("categoriesDataProvider")
    @ParameterizedTest(name = "Проверка отображения подкатегорий инструментов: {0}")
    void thomannOpenSubCategory(String Category, List<String> subCategories) {
        open("/gb/guitars_and_basses.html");
        executeJavaScript("$('.js-accept-all-cookies').click();");
        $$(".fx-category-grid__label").findBy(text(Category)).click();

        $("h1.category-header__title").shouldHave(text(Category));
        $$(".fx-category-grid__title").shouldHave(CollectionCondition.texts(subCategories));

    }

    static Stream<Arguments> categoriesDataProvider() {
        return Stream.of(
                Arguments.of("Elec\u00ADtric Gui\u00ADtars", List.of("Elec\u00ADtric Gui\u00ADtar Sets", "ST Style Gui\u00ADtars",
                        "T Style Gui\u00ADtars", "Single Cut Gui\u00ADtars", "Double Cut Gui\u00ADtars", "Semiac\u00ADous\u00ADtic Gui\u00ADtars",
                        "Heavy Gui\u00ADtars", "Lefthan\u00ADded Gui\u00ADtars", "Premi\u00ADum Gui\u00ADtars", "Jazz Gui\u00ADtars", "7 String Gui\u00ADtars",
                        "8 String Gui\u00ADtars", "Fan\u00ADfret Gui\u00ADtars", "Head\u00ADless Gui\u00ADtars", "12 String Gui\u00ADtars", "Bari\u00ADtone Gui\u00ADtars",
                        "Shorts\u00ADcale Gui\u00ADtars", "MIDI, Di\u00ADgit\u00ADal & Mod\u00ADel\u00ADling Gui\u00ADtars", "Elec\u00ADtric Gui\u00ADtars w/ Piezo Pickups",
                        "Al\u00ADtern\u00ADat\u00ADive Design Gui\u00ADtars", "Sig\u00ADna\u00ADture Gui\u00ADtars")),
                Arguments.of("Clas\u00ADsic\u00ADal Gui\u00ADtars", List.of("1/8 Size Clas\u00ADsic\u00ADal Gui\u00ADtars",
                        "1/4 Size Clas\u00ADsic\u00ADal Gui\u00ADtars", "1/2 Size Clas\u00ADsic\u00ADal Gui\u00ADtars", "3/4 Size Clas\u00ADsic\u00ADal Gui\u00ADtars",
                        "7/8 Size Clas\u00ADsic\u00ADal Gui\u00ADtars", "4/4 Size Clas\u00ADsic\u00ADal Gui\u00ADtars", "Lefthan\u00ADded Clas\u00ADsic\u00ADal Gui\u00ADtars",
                        "Mis\u00ADcel\u00ADlaneous Clas\u00ADsic\u00ADal Gui\u00ADtars", "Fla\u00ADmenco Gui\u00ADtars", "Mas\u00ADter / Mas\u00ADter\u00ADclass Gui\u00ADtars",
                        "Clas\u00ADsic\u00ADal Gui\u00ADtar Sets"))
        );
    }
}
