import org.junit.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Group25
 * @version 2019-03-20
 */
public class ValidationTest {

    // initialization of all data before testing
    static {
        Database.initializePublicInformation();
    }

    @Test
    public void test_isUuidValid_1() {
        User user0 = RandomUserGenerator.createRandomUser(Constants.ROLE[0]);
        Database.accounts.put(user0.getUuid(), user0);
        assertFalse(Validation.isUuidValid(user0.getUuid()));
    }

    @Test
    public void test_isUuidValid_2() {

        User user0 = RandomUserGenerator.createRandomUser(Constants.ROLE[0]);
        Database.accounts.put(user0.getUuid(), user0);
        String uuid = RandomUserGenerator.createRandomUuid();

        if (user0.getUuid().equals(uuid)) {
            assertFalse(Validation.isUuidValid(uuid));
        } else {
            assertTrue(Validation.isUuidValid(uuid));
        }
    }

    @Test
    public void test_isUsernameValid_0() {
        assertFalse(Validation.isUsernameValid(null));
    }

    @Test
    public void test_isUsernameValid_1() {
        assertFalse(Validation.isUsernameValid("    "));
    }

    @Test
    public void test_isUsernameValid_2() {
        assertFalse(Validation.isUsernameValid(""));
    }

    @Test
    public void test_isUsernameValid_3() {
        assertFalse(Validation.isUsernameValid(" "));
    }

    @Test
    public void test_isUsernameValid_4() {
        for (int i = 1; i <= 1000; ++i) {
            assertTrue(Validation.isUsernameValid(RandomUserGenerator.createRandomUsername()));
        }
    }


    @Test
    public void test_isUsernameValid_5() {
        assertFalse(Validation.isUsernameValid("123456789012345678901"));
        assertTrue(Validation.isUsernameValid("12345678901234567890"));
    }

    @Test
    public void test_isFullNameValid_0() {
        assertFalse(Validation.isFullNameValid("abcd"));
    }

    @Test
    public void test_isFullNameValid_1() {
        assertFalse(Validation.isFullNameValid("AB"));
    }

    @Test
    public void test_isFullNameValid_2() {
        assertFalse(Validation.isFullNameValid(" "));
    }

    @Test
    public void test_isFullNameValid_3() {
        assertFalse(Validation.isFullNameValid("ABC EFa"));
    }

    @Test
    public void test_isFullNameValid_4() {
        assertFalse(Validation.isFullNameValid(""));
    }

    @Test
    public void test_isFullNameValid_5() {
        assertFalse(Validation.isFullNameValid(null));
    }

    @Test
    public void test_isFullNameValid_6() {
        assertFalse(Validation.isFullNameValid("ASDIASHUIDIUASDUIASI IASDHUIHASUIDHIASHDIOASODHOIASHDOI"));
    }

    @Test
    public void test_isFullNameValid_7() {
        assertTrue(Validation.isFullNameValid("ABC"));
    }

    @Test
    public void test_isFullNameValid_8() {
        assertFalse(Validation.isFullNameValid("   "));
    }

    @Test
    public void test_isFullNameValid_9() {
        assertFalse(Validation.isFullNameValid(" A "));
    }

    @Test
    public void test_isFullNameValid_10() {
        assertFalse(Validation.isFullNameValid(" A B"));
    }

    @Test
    public void test_isFullNameValid_11() {
        assertTrue(Validation.isFullNameValid("A B"));
    }

    @Test
    public void test_isRoleValid_0() {
        assertTrue(Validation.isRoleValid(Constants.ROLE[0]));
    }

    @Test
    public void test_isRoleValid_1() {
        assertTrue(Validation.isRoleValid(Constants.ROLE[1]));
    }

    @Test
    public void test_isRoleValid_2() {
        assertTrue(Validation.isRoleValid(Constants.ROLE[2]));
    }

    @Test
    public void test_isRoleValid_3() {
        assertFalse(Validation.isRoleValid(null));
    }

    @Test
    public void test_isRoleValid_4() {
        assertFalse(Validation.isRoleValid("abcd"));
    }

    @Test
    public void test_isRoleValid_5() {
        assertFalse(Validation.isRoleValid(""));
    }

    @Test
    public void test_isGenderValid_0() {
        assertTrue(Validation.isGenderValid(Constants.GENDER[0]));
    }

    @Test
    public void test_isGenderValid_1() {
        assertTrue(Validation.isGenderValid(Constants.GENDER[1]));
    }

    @Test
    public void test_isGenderValid_2() {
        assertFalse(Validation.isGenderValid(null));
    }

    @Test
    public void test_isGenderValid_3() {
        assertFalse(Validation.isGenderValid(""));
    }

    @Test
    public void test_isGenderValid_4() {
        assertFalse(Validation.isGenderValid("abcd"));
    }


    @Test
    public void test_isYearLeap_0() {
        assertFalse(Validation.isYearLeap("-1"));
    }

    @Test
    public void test_isYearLeap_1() {
        assertFalse(Validation.isYearLeap("0"));
    }

    @Test
    public void test_isYearLeap_2() {
        assertFalse(Validation.isYearLeap("1700"));
    }

    @Test
    public void test_isYearLeap_3() {
        assertFalse(Validation.isYearLeap("1900"));
    }

    @Test
    public void test_isYearLeap_4() {
        assertFalse(Validation.isYearLeap("123897111"));
    }

    @Test
    public void test_isYearLeap_5() {
        assertTrue(Validation.isYearLeap("1200404"));
    }

    @Test
    public void test_isYearValid_0() {
        assertFalse(Validation.isYearValid("-1123"));
    }

    @Test
    public void test_isYearValid_1() {
        assertFalse(Validation.isYearValid("0"));
    }

    @Test
    public void test_isYearValid_2() {
        assertTrue(Validation.isYearValid(Constants.CURRENT_YEAR.toString()));
    }

    @Test
    public void test_isYearValid_3() {
        int randomYear = ThreadLocalRandom.current().nextInt(Constants.MIN_YEAR.intValue(),
                Constants.CURRENT_YEAR.intValue() + 1);
        assertTrue(Validation.isYearValid(String.valueOf(randomYear)));
    }

    @Test
    public void test_isYearValid_4() {
        String temp = Constants.CURRENT_YEAR.add(BigInteger.ONE).toString();
        assertFalse(Validation.isYearValid(temp));
    }

    @Test
    public void test_isYearValid_5() {
        String temp = Constants.MIN_YEAR.subtract(BigInteger.ONE).toString();
        assertFalse(Validation.isYearValid(temp));
    }

    @Test
    public void test_isMonthValid_0() {
        assertTrue(Validation.isMonthValid("09"));
    }

    @Test
    public void test_isMonthValid_1() {
        assertTrue(Validation.isMonthValid("12"));
    }

    @Test
    public void test_isMonthValid_2() {
        assertFalse(Validation.isMonthValid("-1"));
    }

    @Test
    public void test_isMonthValid_4() {
        assertFalse(Validation.isMonthValid("0"));
    }

    @Test
    public void test_isMonthValid_5() {
        assertFalse(Validation.isMonthValid("-12"));
    }

    @Test
    public void test_isMonthValid_6() {
        assertFalse(Validation.isMonthValid(""));
    }

    @Test
    public void test_isMonthValid_7() {
        assertFalse(Validation.isMonthValid(null));
    }

    @Test
    public void test_isMonthValid_8() {
        assertFalse(Validation.isMonthValid("142"));
    }

    @Test
    public void test_isMonthValid_9() {
        assertFalse(Validation.isMonthValid("asdiuasd"));
    }

    @Test
    public void test_isMonthValid_10() {
        assertFalse(Validation.isMonthValid(" 2"));
    }

    @Test
    public void test_isMonthValid_11() {
        assertTrue(Validation.isMonthValid("02"));
    }

    @Test
    public void test_isDayValid_0() {
        assertFalse(Validation.isDayValid("2012", "1", "1"));
    }

    @Test
    public void test_isDayValid_1() {
        assertFalse(Validation.isDayValid("1800", "1", "1"));
    }

    @Test
    public void test_isDayValid_2() {
        assertFalse(Validation.isDayValid("1900", "13", "1"));
    }

    @Test
    public void test_isDayValid_3() {
        assertFalse(Validation.isDayValid("1900", "1", "32"));
    }

    @Test
    public void test_isDayValid_4() {
        assertFalse(Validation.isDayValid("3000", "1", "1"));
    }

    @Test
    public void test_isDayValid_5() {
        assertTrue(Validation.isDayValid("1985", "12", "31"));
    }

    @Test
    public void test_isDayValid_7() {
        assertFalse(Validation.isDayValid("2001", "02", "29"));
    }

    @Test
    public void test_isDayValid_8() {
        assertTrue(Validation.isDayValid("2000", "02", "29"));
    }

    @Test
    public void test_isYearValid_6() {
        assertFalse(Validation.isYearValid(null));
    }

    @Test
    public void test_isYearValid_7() {
        assertFalse(Validation.isYearValid(""));
    }

    @Test
    public void test_isYearValid_8() {
        assertTrue(Validation.isYearValid("1993"));
    }

    @Test
    public void test_isYearValid_9() {
        assertFalse(Validation.isYearValid("3000"));
    }

    @Test
    public void test_isYearValid_10() {
        assertFalse(Validation.isYearValid(" 2000"));
    }

    @Test
    public void test_isYearValid_11() {
        assertFalse(Validation.isYearValid("2000 "));
    }


    @Test
    public void test_isDayValidAsAString_0() {
        assertFalse(Validation.isDayValid("1899", "12", "1"));
    }

    @Test
    public void test_isDayValidAsAString_1() {
        assertFalse(Validation.isDayValid("1900", "12", "1"));
    }

    @Test
    public void test_isDayValidAsAString_1_0() {
        assertTrue(Validation.isDayValid("1900", "12", "01"));
    }

    @Test
    public void test_isDayValidAsAString_2() {
        assertFalse(Validation.isDayValid("1900", "2", " 1"));
    }

    @Test
    public void test_isDayValidAsAString_3() {
        assertFalse(Validation.isDayValid("1900", "13", "1"));
    }

    @Test
    public void test_isDayValidAsAString_4() {
        assertFalse(Validation.isDayValid("1900", "-1", "1"));
    }

    @Test
    public void test_isDayValidAsAString_5() {
        assertFalse(Validation.isDayValid("1900", "0", "1"));
    }

    @Test
    public void test_isDayValidAsAString_6() {
        assertFalse(Validation.isDayValid("1900", "01", "1 "));
    }

    @Test
    public void test_isDayValidAsAString_7() {
        assertFalse(Validation.isDayValid("1900", "01", null));
    }

    @Test
    public void test_isDayValidAsAString_8() {
        assertFalse(Validation.isDayValid("3000", "01", "01"));
    }

    @Test
    public void test_isDayValidAsAString_9() {
        assertFalse(Validation.isDayValid("1900", "1", ""));
    }

    @Test
    public void test_isDateOfBirthValid_0() {
        assertFalse(Validation.isDayValid("1900", "12", "1"));
        //assertFalse(Validation.isDateOfBirthValid(new DateOfBirth("1900", "12", "1")));
    }

    @Test
    public void test_isDateOfBirthValid_0_1() {
        assertTrue(Validation.isDateOfBirthValid(new DateOfBirth("1900", "12", "01")));
    }

    @Test
    public void test_isDateOfBirthValid_1() {
        assertFalse(Validation.isDateOfBirthValid(null));
    }

    @Test
    public void test_isDateOfBirthValid_2() {
        assertFalse(Validation.isDateOfBirthValid(new DateOfBirth("1899", "12", "1")));
    }

    @Test
    public void test_isDateOfBirthValid_3() {
        assertFalse(Validation.isDateOfBirthValid(new DateOfBirth("1900", "13", "1")));
    }

    @Test
    public void test_isDateOfBirthValid_4() {
        assertTrue(Validation.isDateOfBirthValid(new DateOfBirth("2000", "12", "31")));
    }

    @Test
    public void test_isDateOfBirthValid_5() {
        assertTrue(Validation.isDateOfBirthValid(new DateOfBirth("2000-12-31")));
    }


    @Test
    public void test_isDateOfBirthValid_6() {
        assertFalse(Validation.isDateOfBirthValid(new DateOfBirth("200 0-12-31")));
    }

    @Test
    public void test_isDateOfBirthValid_7() {
        assertFalse(Validation.isDateOfBirthValid(new DateOfBirth("")));
    }

    @Test
    public void test_isDateOfBirthValid_8() {
        assertFalse(Validation.isDateOfBirthValid(new DateOfBirth("2000-012-31")));
    }

    @Test
    public void test_isEmailAddressValid_0() {
        assertFalse(Validation.isEmailAddressValid(null));
    }

    @Test
    public void test_isEmailAddressValid_1() {
        assertFalse(Validation.isEmailAddressValid(""));
    }

    @Test
    public void test_isEmailAddressValid_2() {
        assertFalse(Validation.isEmailAddressValid("    "));
    }

    @Test
    public void test_isEmailAddressValid_3() {
        assertFalse(Validation.isEmailAddressValid("a"));
    }

    @Test
    public void test_isEmailAddressValid_4() {
        assertFalse(Validation.isEmailAddressValid("plainaddress"));
    }

    @Test
    public void test_isEmailAddressValid_5() {
        assertFalse(Validation.isEmailAddressValid("#@%^%#$@!#&^#$@#.net"));
    }

    @Test
    public void test_isEmailAddressValid_6() {
        assertFalse(Validation.isEmailAddressValid("@domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_7() {
        assertFalse(Validation.isEmailAddressValid("Jack Jones <jackjones@domain.com>"));
    }

    @Test
    public void test_isEmailAddressValid_8() {
        assertFalse(Validation.isEmailAddressValid("jackjones.domain.com>"));
    }

    @Test
    public void test_isEmailAddressValid_9() {
        assertFalse(Validation.isEmailAddressValid("email@domain@domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_10() {
        assertFalse(Validation.isEmailAddressValid("終了しました@domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_11() {
        assertFalse(Validation.isEmailAddressValid("结束了_結娕孒_結束了_Закончилось@a.com"));
    }

    @Test
    public void test_isEmailAddressValid_12() {
        assertFalse(Validation.isEmailAddressValid("email@domain.com (Jack Barrow)"));
    }

    @Test
    public void test_isEmailAddressValid_13() {
        assertFalse(Validation.isEmailAddressValid("email@domain"));
    }

    @Test
    public void test_isEmailAddressValid_14() {
        assertFalse(Validation.isEmailAddressValid("email@-domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_15() {
        assertTrue(Validation.isEmailAddressValid("email@domain.web"));
    }

    @Test
    public void test_isEmailAddressValid_16() {
        assertTrue(Validation.isEmailAddressValid("email@111.222.111.222"));
    }

    @Test
    public void test_isEmailAddressValid_17() {
        assertFalse(Validation.isEmailAddressValid("email@domain..com"));
    }

    @Test
    public void test_isEmailAddressValid_18() {
        assertFalse(Validation.isEmailAddressValid("emailemailemailemailemailemailemailemailemailemailemailemail@domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_19() {
        assertTrue(Validation.isEmailAddressValid("a@a.com"));
    }

    @Test
    public void test_isEmailAddressValid_20() {
        assertTrue(Validation.isEmailAddressValid("a@a.com"));
    }

    @Test
    public void test_isEmailAddressValid_21() {
        assertTrue(Validation.isEmailAddressValid("james.lee@domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_22() {
        assertTrue(Validation.isEmailAddressValid("email@subdomain.domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_23() {
        assertTrue(Validation.isEmailAddressValid("firstname_lastname@domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_24() {
        assertFalse(Validation.isEmailAddressValid("email@[123.123.123.123]"));
    }

    @Test
    public void test_isEmailAddressValid_25() {
        assertFalse(Validation.isEmailAddressValid("\"umo\"@domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_26() {
        assertTrue(Validation.isEmailAddressValid("41278937192101@domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_27() {
        assertTrue(Validation.isEmailAddressValid("email@domain-domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_28() {
        assertTrue(Validation.isEmailAddressValid("__________@domain.com"));
    }

    @Test
    public void test_isEmailAddressValid_29() {
        assertTrue(Validation.isEmailAddressValid("email@domain.name.name2.name3"));
    }

    @Test
    public void test_isEmailAddressValid_30() {
        assertTrue(Validation.isEmailAddressValid("firstname-lastname@domain.com"));
    }

    @Test
    public void test_isPhoneNumberValid_0() {
        assertFalse(Validation.isPhoneNumberValid(null));
    }

    @Test
    public void test_isPhoneNumberValid_1() {
        assertFalse(Validation.isPhoneNumberValid(""));
    }

    @Test
    public void test_isPhoneNumberValid_2() {
        assertFalse(Validation.isPhoneNumberValid(" "));
    }

    @Test
    public void test_isPhoneNumberValid_3() {
        assertFalse(Validation.isPhoneNumberValid("111-1111111"));
    }

    @Test
    public void test_isPhoneNumberValid_4() {
        assertFalse(Validation.isPhoneNumberValid("1111111111"));
    }

    @Test
    public void test_isPhoneNumberValid_5() {
        assertTrue(Validation.isPhoneNumberValid("111-111-1111"));
    }

    @Test
    public void test_isAddressValid_0() {
        assertFalse(Validation.isAddressValid(null));
    }

    @Test
    public void test_isAddressValid_1() {
        assertFalse(Validation.isAddressValid(""));
    }

    @Test
    public void test_isAddressValid_2() {
        assertFalse(Validation.isAddressValid(
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void test_isAddressValid_3() {
        assertTrue(Validation.isAddressValid("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
    }

    @Test
    public void test_isAddressValid_4() {
        assertTrue(Validation.isAddressValid("                                                                                "));
    }

    @Test
    public void test_isPasswordValid_0() {
        assertFalse(Validation.isPasswordValid(null));
    }

    @Test
    public void test_isPasswordValid_1() {
        assertFalse(Validation.isPasswordValid(""));
    }

    @Test
    public void test_isPasswordValid_2() {
        assertFalse(Validation.isPasswordValid("abcde"));
    }

    @Test
    public void test_isPasswordValid_3() {
        assertTrue(Validation.isPasswordValid("abcdef"));
    }

    @Test
    public void test_isPasswordValid_4() {
        assertTrue(Validation.isPasswordValid("      "));
    }

    @Test
    public void test_isPasswordValid_5() {
        assertTrue(Validation.isPasswordValid("  a    "));
    }

    @Test
    public void test_isCourseIdValid_0() {
        assertFalse(Validation.isCourseIdValid(null));
    }

    @Test
    public void test_isCourseIdValid_1() {
        assertFalse(Validation.isCourseIdValid(""));
    }

    @Test
    public void test_isCourseIdValid_2() {
        assertFalse(Validation.isCourseIdValid("CPSC231"));
    }

    @Test
    public void test_isCourseIdValid_3() {
        assertFalse(Validation.isCourseIdValid("CPSC 231"));
    }

    @Test
    public void test_isCourseIdValid_4() {
        assertTrue(Validation.isCourseIdValid("CPSC237"));
    }

    @Test
    public void test_isCourseIdValid_5() {
        assertFalse(Validation.isCourseIdValid("CPSC231 "));
    }

    @Test
    public void test_isCourseIdValid_6() {
        assertFalse(Validation.isCourseIdValid("12345678901"));
    }

    @Test
    public void test_isCourseIdValid_7() {
        assertFalse(Validation.isCourseIdValid("1234567890"));
    }

    @Test
    public void test_isCourseIdValid_8() {
        assertTrue(Validation.isCourseIdValid("IOL243"));
    }

    @Test
    public void test_isCourseNameValid_0() {
        assertFalse(Validation.isCourseNameValid(""));
    }

    @Test
    public void test_isCourseNameValid_1() {
        assertFalse(Validation.isCourseNameValid(null));
    }

    @Test
    public void test_isCourseNameValid_2() {
        assertTrue(Validation.isCourseNameValid(" "));
    }

    @Test
    public void test_isCourseNameValid_3() {
        assertTrue(Validation.isCourseNameValid("A"));
    }

    @Test
    public void test_isCourseDescriptionValid_0() {
        assertFalse(Validation.isCourseDescriptionValid(null));
    }

    @Test
    public void test_isCourseDescriptionValid_1() {
        assertFalse(Validation.isCourseDescriptionValid(""));
    }

    @Test
    public void test_isCourseDescriptionValid_2() {
        assertTrue(Validation.isCourseDescriptionValid(" "));
    }

    @Test
    public void test_isCourseDescriptionValid_3() {
        assertTrue(Validation.isCourseDescriptionValid("a"));
    }

    @Test
    public void test_isInteger_0() {
        assertFalse(Validation.isInteger(null));
    }

    @Test
    public void test_isInteger_1() {
        assertFalse(Validation.isInteger(""));
    }

    @Test
    public void test_isInteger_2() {
        assertFalse(Validation.isInteger("aads1"));
    }

    @Test
    public void test_isInteger_3() {
        assertFalse(Validation.isInteger("0001"));
    }

    @Test
    public void test_isInteger_4() {
        assertFalse(Validation.isInteger("1.000"));
    }

    @Test
    public void test_isInteger_5() {
        assertFalse(Validation.isInteger("-0001"));
    }

    @Test
    public void test_isInteger_6() {
        assertFalse(Validation.isInteger(" 1"));
    }

    @Test
    public void test_isInteger_7() {
        assertTrue(Validation.isInteger("12"));
    }

    @Test
    public void test_isInteger_8() {
        assertTrue(Validation.isInteger("-12"));
    }

    @Test
    public void test_isInteger_9() {
        assertFalse(Validation.isInteger("--12"));
    }

    @Test
    public void test_isInteger_10() {
        assertFalse(Validation.isInteger("+12"));
    }

    @Test
    public void test_isInteger_11() {
        assertTrue(Validation.isInteger("120000"));
    }

    @Test
    public void test_isInteger_12() {
        assertFalse(Validation.isInteger("00"));
    }

    @Test
    public void test_isDouble_0() {
        assertFalse(Validation.isDouble(null));
    }

    @Test
    public void test_isDouble_1() {
        assertFalse(Validation.isDouble(""));
    }

    @Test
    public void test_isDouble_2() {
        assertFalse(Validation.isDouble("asd1.5"));
    }

    @Test
    public void test_isDouble_3() {
        assertFalse(Validation.isDouble("+1.5"));
    }

    @Test
    public void test_isDouble_4() {
        assertFalse(Validation.isDouble("+00.5"));
    }

    @Test
    public void test_isDouble_5() {
        assertFalse(Validation.isDouble("+0.5"));
    }

    @Test
    public void test_isDouble_6() {
        assertTrue(Validation.isDouble("0.5"));
    }

    @Test
    public void test_isDouble_7() {
        assertTrue(Validation.isDouble("0.0000000000000000005"));
    }

    @Test
    public void test_isDouble_8() {
        assertFalse(Validation.isDouble("0.000000000000000000"));
    }

    @Test
    public void test_isDouble_9() {
        assertFalse(Validation.isDouble("0."));
    }

    @Test
    public void test_isDouble_10() {
        assertFalse(Validation.isDouble("1.0"));
    }

    @Test
    public void test_isDouble_11() {
        assertTrue(Validation.isDouble("0.127389612469126491412241"));
    }

    @Test
    public void test_isDouble_12() {
        assertFalse(Validation.isDouble("0.1273896124691264914122410"));
    }

    @Test
    public void test_isDouble_13() {
        assertFalse(Validation.isDouble(".127389612469126491412241"));
    }

    @Test
    public void test_isDouble_14() {
        assertFalse(Validation.isDouble("00.127389612469126491412241"));
    }

    @Test
    public void test_isCourseUnitsValid_0() {
        assertFalse(Validation.isCourseUnitsValid(null));
    }

    @Test
    public void test_isCourseUnitsValid_1() {
        assertFalse(Validation.isCourseUnitsValid(""));
    }

    @Test
    public void test_isCourseUnitsValid_2() {
        assertFalse(Validation.isCourseUnitsValid(" "));
    }

    @Test
    public void test_isCourseUnitsValid_3() {
        assertFalse(Validation.isCourseUnitsValid(" 1.0"));
    }

    @Test
    public void test_isCourseUnitsValid_4() {
        assertFalse(Validation.isCourseUnitsValid("-1.0"));
    }

    @Test
    public void test_isCourseUnitsValid_5() {
        assertTrue(Validation.isCourseUnitsValid("0.1"));
    }

    @Test
    public void test_isCourseUnitsValid_6() {
        assertFalse(Validation.isCourseUnitsValid("00.1"));
    }

    @Test
    public void test_isCourseUnitsValid_7() {
        assertFalse(Validation.isCourseUnitsValid("1.000000"));
    }

    @Test
    public void test_isCourseUnitsValid_8() {
        assertTrue(Validation.isCourseUnitsValid("1.18238123"));
    }

    @Test
    public void test_isCourseUnitsValid_9() {
        assertFalse(Validation.isCourseUnitsValid("120.0000000001"));
    }

    @Test
    public void test_isCourseUnitsValid_10() {
        assertTrue(Validation.isCourseUnitsValid("4"));
    }

    @Test
    public void test_isCourseUnitsValid_11() {
        assertFalse(Validation.isCourseUnitsValid("01.1"));
    }

    @Test
    public void test_isCourseUnitsValid_12() {
        assertFalse(Validation.isCourseUnitsValid("1.0"));
    }

    @Test
    public void test_isCourseUnitsValid_13() {
        assertFalse(Validation.isCourseUnitsValid("0.010"));
    }

    @Test
    public void test_isCourseUnitsValid_14() {
        assertTrue(Validation.isCourseUnitsValid("1.1"));
    }

    @Test
    public void test_isCourseUnitsValid_15() {
        assertFalse(Validation.isCourseUnitsValid("+1.1"));
    }

    @Test
    public void test_isCourseUnitsValid_16() {
        assertFalse(Validation.isCourseUnitsValid("-1.1"));
    }

    @Test
    public void test_isGradeValid_0() {
        assertFalse(Validation.isGradeValid(null));
    }

    @Test
    public void test_isGradeValid_1() {
        assertFalse(Validation.isGradeValid("1.0"));
    }

    @Test
    public void test_isGradeValid_2() {
        assertFalse(Validation.isGradeValid("0.010"));
    }

    @Test
    public void test_isGradeValid_3() {
        assertTrue(Validation.isGradeValid("1.1"));
        assertTrue(Validation.isGradeValid("3.9"));
        assertTrue(Validation.isGradeValid("0.9"));
        assertFalse(Validation.isGradeValid("3.91"));
        assertFalse(Validation.isGradeValid("3.911231273198231"));
        assertFalse(Validation.isGradeValid("0.0979812983172931"));
    }

    @Test
    public void test_isGradeValid_4() {
        assertTrue(Validation.isGradeValid("0"));
    }

    @Test
    public void test_isGradeValid_5() {
        assertFalse(Validation.isGradeValid("0.0"));
    }

    @Test
    public void test_isGradeValid_6() {
        assertFalse(Validation.isGradeValid("120"));
    }

    @Test
    public void test_isGradeValid_7() {
        assertFalse(Validation.isGradeValid("121"));
    }

    @Test
    public void test_isGradeValid_8() {
        assertFalse(Validation.isGradeValid("120.000000000000000001"));
    }

    @Test
    public void test_isGradeValid_9() {
        assertFalse(Validation.isGradeValid("4.000000000000001"));
    }

    @Test
    public void test_isGradeValid_10() {
        assertTrue(Validation.isGradeValid("4"));
    }

    @Test
    public void test_isGradeValid_11() {
        assertFalse(Validation.isGradeValid("4.0"));
    }

    @Test
    public void test_isPrerequisitesValid_0() {
        assertFalse(Validation.isPrerequisitesValid(null));
    }

    @Test
    public void test_isPrerequisitesValid_1() {
        assertTrue(Validation.isPrerequisitesValid(""));
    }

    @Test
    public void test_isPrerequisitesValid_2() {
        assertFalse(Validation.isPrerequisitesValid(" "));
    }

    @Test
    public void test_isPrerequisitesValid_3() {
        assertFalse(Validation.isPrerequisitesValid("CPSC231 CPSC231"));
    }

    @Test
    public void test_isPrerequisitesValid_4() {
        assertFalse(Validation.isPrerequisitesValid("CPSC931"));
    }

    @Test
    public void test_isPrerequisitesValid_5() {
        assertTrue(Validation.isPrerequisitesValid("CPSC231 CPSC233"));
    }

    @Test
    public void test_isPrerequisitesValid_6() {
        assertFalse(Validation.isPrerequisitesValid(" CPSC231 CPSC233"));
    }

    @Test
    public void test_isPrerequisitesValid_7() {
        assertFalse(Validation.isPrerequisitesValid("CPSC231 CPSC233 CPSC239"));
    }

    @Test
    public void test_isAntirequisitesValid_0() {
        assertFalse(Validation.isAntirequisitesValid(null));
    }

    @Test
    public void test_isAntirequisitesValid_1() {
        HashSet<String> test = new HashSet<>();
        test.add("CPSC231");
        test.add("CPSC233");
        assertTrue(Validation.isAntirequisitesValid(test));
    }

    @Test
    public void test_isAntirequisitesValid_2() {
        HashSet<String> test = new HashSet<>();
        assertTrue(Validation.isAntirequisitesValid(test));
    }

    @Test
    public void test_isAntirequisitesValid_3() {
        HashSet<String> test = new HashSet<>();
        test.add("CPSC231231");
        assertFalse(Validation.isAntirequisitesValid(test));
    }

    @Test
    public void test_isAntirequisitesValid_4() {
        HashSet<String> test = new HashSet<>();
        test.add("CPSC231231");
        test.add("CPSC231");
        assertFalse(Validation.isAntirequisitesValid(test));
    }

    @Test
    public void test_isAntirequisitesValidAsAString_0() {
        assertFalse(Validation.isAntirequisitesValidAsAString(null));
    }

    @Test
    public void test_isAntirequisitesValidAsAString_1() {
        assertFalse(Validation.isAntirequisitesValidAsAString(" "));
    }

    @Test
    public void test_isAntirequisitesValidAsAString_2() {
        assertTrue(Validation.isAntirequisitesValidAsAString(""));
    }

    @Test
    public void test_isAntirequisitesValidAsAString_3() {
        assertTrue(Validation.isAntirequisitesValidAsAString("CPSC231"));
    }

    @Test
    public void test_isAntirequisitesValidAsAString_4() {
        assertTrue(Validation.isAntirequisitesValidAsAString("CPSC231 CPSC233"));
    }

    @Test
    public void test_isAntirequisitesValidAsAString_5() {
        assertFalse(Validation.isAntirequisitesValidAsAString("CPSC231 CPSC231"));
    }

    @Test
    public void test_isAntirequisitesValidAsAString_6() {
        assertFalse(Validation.isAntirequisitesValidAsAString("CPSC231 CPSC231231"));
    }

    @Test
    public void test_isAntirequisitesValidAsAString_7() {
        assertFalse(Validation.isAntirequisitesValidAsAString("CPSC231 "));
    }

    @Test
    public void test_isCanBeRepeatedValid_0() {
        assertFalse(Validation.isCanBeRepeatedValid(null));
    }

    @Test
    public void test_isCanBeRepeatedValid_1() {
        assertFalse(Validation.isCanBeRepeatedValid(""));
    }

    @Test
    public void test_isCanBeRepeatedValid_2() {
        assertFalse(Validation.isCanBeRepeatedValid("Yes"));
    }

    @Test
    public void test_isCanBeRepeatedValid_3() {
        assertTrue(Validation.isCanBeRepeatedValid("YES"));
    }

    @Test
    public void test_isCanBeRepeatedValid_4() {
        assertFalse(Validation.isCanBeRepeatedValid("NO "));
    }

    @Test
    public void test_isCanBeRepeatedValid_5() {
        assertTrue(Validation.isCanBeRepeatedValid("NO"));
    }

    @Test
    public void test_satisfyPrerequisites_2() {
        Student testStudent  = new Student();
        String  testCourseId = "CPSC231";
        assertTrue(Validation.satisfyPrerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyPrerequisites_3() {
        Student testStudent  = new Student();
        String  testCourseId = "CPSC233X";
        assertFalse(Validation.satisfyPrerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyPrerequisites_4() {
        Student testStudent  = new Student();
        String  testCourseId = "CPSC219";
        testStudent.addTakenCourses(new CourseAndGrade("CPSC217", "4.0"));
        assertTrue(Validation.satisfyPrerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyPrerequisites_5() {
        Student testStudent  = new Student();
        String  testCourseId = "CPSC219";
        assertFalse(Validation.satisfyPrerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyPrerequisites_6() {
        Student testStudent = new Student();
        testStudent.addTakenCourses(new CourseAndGrade("CPSC231", "4.0"));
        testStudent.addTakenCourses(new CourseAndGrade("CPSC233", "4.0"));
        testStudent.addTakenCourses(new CourseAndGrade("MATH267", "4.0"));
        String testCourseId = "MATH271";
        assertFalse(Validation.satisfyPrerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyPrerequisites_7() {
        Student testStudent = new Student();
        testStudent.addTakenCourses(new CourseAndGrade("CPSC231", "4.0"));
        testStudent.addTakenCourses(new CourseAndGrade("CPSC233", "4.0"));
        testStudent.addTakenCourses(new CourseAndGrade("MATH267", "4.0"));
        testStudent.addTakenCourses(new CourseAndGrade("MATH211", "4.0"));
        String testCourseId = "MATH271";
        assertTrue(Validation.satisfyPrerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyPrerequisites_8() {
        Student testStudent = new Student();
        testStudent.addTakenCourses(new CourseAndGrade("CPSC231", "4.0"));
        testStudent.addTakenCourses(new CourseAndGrade("CPSC233", "4.0"));
        testStudent.addTakenCourses(new CourseAndGrade("MATH267", "4.0"));
        testStudent.addTakenCourses(new CourseAndGrade("MATH211", "4.0"));
        String testCourseId = "MATH271";
        assertTrue(Validation.satisfyPrerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyAntirerequisites_2() {
        Student testStudent  = new Student();
        String  testCourseId = "CPSC233X";
        assertFalse(Validation.satisfyAntirerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyAntirerequisites_3() {
        Student testStudent = new Student();
        testStudent.addTakenCourses(new CourseAndGrade("CPSC217", "4"));
        String testCourseId = "CPSC217";
        assertFalse(Validation.satisfyAntirerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyAntirerequisites_4() {
        Student testStudent = new Student();
        testStudent.addTakenCourses(new CourseAndGrade("DATA211", "4"));
        String testCourseId = "CPSC217";
        assertFalse(Validation.satisfyAntirerequisites(testStudent, testCourseId));
    }

    @Test
    public void test_satisfyAntirerequisites_5() {
        Student testStudent = new Student();
        testStudent.addTakenCourses(new CourseAndGrade("MATH318", "4"));
        String testCourseId = "CPSC418";
        assertFalse(Validation.satisfyAntirerequisites(testStudent, testCourseId));
    }
}