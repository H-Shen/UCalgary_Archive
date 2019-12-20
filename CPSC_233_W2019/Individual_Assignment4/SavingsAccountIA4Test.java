import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class SavingsAccountIA4Test {
    public static final String CLASSNAME = "SavingsAccount";
    public static final String FILENAME  = CLASSNAME + ".java";


    private void testInterface() {
        String[] instanceVars = {"double annualInterestRate"};
        assertTrue("Instance variables should be private with correct name and signature.", instanceVariablesArePrivate(instanceVars));

//		assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        assertFalse("Should not override getBalance.", hasMethod("double getBalance()"));
    }


    // test constructors
    @Test
    public void test_creation_defaultConstructor() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        assertEquals("Expected initial balance to be 0.0", 0.0, s.getBalance(), 0.00001);
        assertEquals("Expected annual interest rate to be 0.05 (5%)", 0.05, s.getAnnualInterestRate(), 0.00001);
    }


    @Test
    public void test_setAnnualInterestRate_zero() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(0.0);
        assertEquals("Changed annual interest rate is 0.0", 0.0, s.getAnnualInterestRate(), 0.000001);
    }

    @Test
    public void test_setAnnualInterestRate_one() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(1.0);
        assertEquals("Changed annual interest rate is 1.0", 1.0, s.getAnnualInterestRate(), 0.000001);
    }

    @Test
    public void test_setAnnualInterestRate_negativeRate() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(-0.25);
        assertEquals("Annual interest rate should not be negative and should have been left unchanged.", 0.05, s.getAnnualInterestRate(), 0.000001);
    }

    @Test
    public void test_setAnnualInterestRate_biggerThanOne() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(1.1);
        assertEquals("Annual interest rate should be at most 1 (100%) and should have been left unchanged.", 0.05, s.getAnnualInterestRate(), 0.000001);
    }

    @Test
    public void test_depositMonthlyInterest_defaultRate() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.deposit(100);
        s.depositMonthlyInterest();
        assertEquals("Balance ($100) should have increased by interest rate/12 * balance", 100.416666667, s.getBalance(), 0.00001);
    }

    @Test
    public void test_depositMonthlyInterest_zeroBalance() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.depositMonthlyInterest();
        assertEquals("Zero Balance should not have interest added to it", 0.0, s.getBalance(), 0.00001);
    }

    @Test
    public void test_depositMonthlyInterest_zeroPercent() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(0.0);
        s.deposit(100.0);
        s.depositMonthlyInterest();
        assertEquals("Zero interest rate should leave balance ($100) unchanged", 100.0, s.getBalance(), 0.00001);
    }

    @Test
    public void test_depositMonthlyInterest_hundredPercent() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(1.0);
        s.deposit(100.0);
        s.depositMonthlyInterest();
        assertEquals("100% interest rate should add monthly interest of balance/12", 108.333333, s.getBalance(), 0.00001);
    }

    @Test
    public void test_withdraw_fundsAvailable() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.deposit(500);
        s.setMinimumBalance(100.0);

        s.withdraw(400);

        assertEquals("Savings account with 500.0 balance and 100 min balance, should be able to withdraw 400.", 100.0, s.getBalance(), 0.000001);
    }

    @Test
    public void test_withdraw_WouldGoBelowMinBalance() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.deposit(500);
        s.setMinimumBalance(100.0);

        s.withdraw(401);

        assertEquals("Savings account with 500.0 balance and 100 min balance, can't withdraw 400.", 500.0, s.getBalance(), 0.000001);
    }

//////////////End of test methods /////////////////////////////


    private boolean hasMethod(String signature) {
        boolean contains = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains(signature)) {
                    contains = true;
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            contains = false;
        } catch (IOException e) {
            contains = false;
        }
        return contains;

    }

    private boolean toStrInvokesParentToStr() {
        boolean callsGetter = false;
        boolean callsParent = false;


        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("toString")) {
                    // This should be start of toString method
                    while (!line.contains("}")) {
                        line = in.readLine();
                        if (line.contains("getHeight")) {
                            callsGetter = true;
                        }
                        if (line.contains("super.toString()")) {
                            callsParent = true;
                        }
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            callsParent = false;
        } catch (IOException e) {
            callsParent = false;
        }
        return callsParent && !callsGetter;
    }

    private boolean hasRequiredAbstractMethods(String[] abstractMethods) {
        boolean[] methodsAbstract = new boolean[abstractMethods.length];
        for (int index = 0; index < abstractMethods.length; index++) {
            methodsAbstract[index] = false;
        }
        boolean classIsAbstract = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("public abstract class " + CLASSNAME)) {
                    classIsAbstract = true;
                } else {
                    for (int index = 0; index < abstractMethods.length; index++) {
                        String stmt = "public abstract " + abstractMethods[index];
                        if (line.contains(stmt)) {
                            methodsAbstract[index] = true;
                        }
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            classIsAbstract = false;
        } catch (IOException e) {
            classIsAbstract = false;
        }

        boolean allAbstract = classIsAbstract;
        for (boolean b : methodsAbstract) {
            allAbstract = allAbstract && b;
        }
        return allAbstract;

    }

    private boolean hasRequiredProtectedMethods(String[] protectedMethods) {
        boolean[] methodsProtected = new boolean[protectedMethods.length];
        for (int index = 0; index < protectedMethods.length; index++) {
            methodsProtected[index] = false;
        }
        boolean allProtected = true;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                for (int index = 0; index < protectedMethods.length; index++) {
                    String stmt = "protected " + protectedMethods[index];
                    if (line.contains(stmt)) {
                        methodsProtected[index] = true;
                    }
                }

                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            allProtected = false;
        } catch (IOException e) {
            allProtected = false;
        }

        for (boolean b : methodsProtected) {
            allProtected = allProtected && b;
        }
        return allProtected;
    }

    private boolean instanceVariablesArePrivate(String[] instanceVars) {
        boolean[] varsPrivate = new boolean[instanceVars.length];
        for (int index = 0; index < instanceVars.length; index++) {
            varsPrivate[index] = false;
        }
        boolean allPrivate = true;


        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("private")) {
                    line = line.trim();
                    for (int index = 0; index < instanceVars.length; index++) {
                        String stmt = "private " + instanceVars[index];
                        if (line.length() >= stmt.length()) {
                            String subline = line.substring(0, stmt.length());
                            if (subline.equals(stmt)) {
                                varsPrivate[index] = true;
                            }
                        }
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            allPrivate = false;
        } catch (IOException e) {
            allPrivate = false;
        }

        for (boolean b : varsPrivate) {
            allPrivate = allPrivate && b;
        }
        return allPrivate;
    }

    private boolean noDefaultConstructor() {
        boolean  noDefault = true;
        String[] versions  = new String[9];
        versions[0] = "public " + CLASSNAME + "()";
        versions[1] = "public " + CLASSNAME + " ()";
        versions[2] = "public " + CLASSNAME + " ( )";
        versions[3] = "protected " + CLASSNAME + "()";
        versions[4] = "protected " + CLASSNAME + " ()";
        versions[5] = "protected " + CLASSNAME + " ( )";
        versions[6] = CLASSNAME + "()";
        versions[7] = CLASSNAME + " ()";
        versions[8] = CLASSNAME + " ( )";

        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                for (String stmt : versions) {
                    if (line.contains(stmt)) {
                        noDefault = false;
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            noDefault = false;
        } catch (IOException e) {
            noDefault = false;
        }
        return noDefault;
    }


}
