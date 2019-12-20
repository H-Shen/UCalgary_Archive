import org.junit.Test;

import static org.junit.Assert.*;

public class TrampolineTestForEclipse extends FormatTester {
    public static final String CLASSNAME = "Trampoline";

    public TrampolineTestForEclipse() {
        super(CLASSNAME, true);
    }

    private void testClassDefinition() {
        String[] instanceVars = {"int weightOfJumper"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        assertFalse("Should not override bounce.", hasMethod("void bounce"));
        assertFalse("Should not override numberOfBounces.", hasMethod("int numberOfBounces"));
        assertFalse("Should not override (or call) setHeight.", hasMethod("void setHeight"));
        assertFalse("Should not override (or call) setBounciness.", hasMethod("void setBounciness"));
    }

    // Testing constructors

    @Test
    public void test_Constructor_weight_49_GoodBouncinessAndHeight() {
        testClassDefinition();
        Trampoline c = new Trampoline(49, .32, 5);
        assertEquals("Created trampoline with invalid weight 49", 140, c.getWeightOfJumper());
        assertEquals("Created trampoline with valid 5 height.", 5, c.getHeight());
        assertEquals("Created trampoline with valid 32% bounciness.", .32, c.getBounciness(), 0.00001);
    }

    @Test
    public void test_Constructor_weight_301_GoodHeightAndBounciness() {
        testClassDefinition();
        Trampoline c = new Trampoline(301, .75, 25);
        assertEquals("Created trampoline with invalid 301 weight", 140, c.getWeightOfJumper());
        assertEquals("Created trampoline with valid 25 height.", 25, c.getHeight());
        assertEquals("Created trampoline with valid 75% bounciness.", .75, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_Constructor_weight_100_HeightTooLow() {
        testClassDefinition();
        Trampoline c = new Trampoline(100, .5, 0);
        assertEquals("Created trampoline with valid 100 weight", 100, c.getWeightOfJumper());
        assertEquals("Created trampoline with invalid 0 height.", 1, c.getHeight());
        assertEquals("Created trampoline with valid 50% bounciness.", .5, c.getBounciness(), 0.000001);
    }


    @Test
    public void test_Constructor_weight_100_InvalidBounciness() {
        testClassDefinition();
        Trampoline c = new Trampoline(110, 1.0, 12);
        assertEquals("Created trampoline with valid 110 weight", 110, c.getWeightOfJumper());
        assertEquals("Created trampoline with valid 12 height.", 12, c.getHeight());
        assertEquals("Created trampoline with invalid 100% bounciness.", .5, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_CopyConstructor() {
        testClassDefinition();
        Trampoline c  = new Trampoline(300, .99, 23);
        Trampoline c2 = new Trampoline(c);
        assertEquals("Copied trampoline with 300 weight", 300, c2.getWeightOfJumper());
        assertEquals("Copied trampoline with 23 height.", 23, c2.getHeight());
        assertEquals("Copied trampoline with 99% bounciness.", .99, c2.getBounciness(), 0.00001);
    }

    @Test
    public void test_CopyConstructor2() {
        testClassDefinition();
        Trampoline c  = new Trampoline(100, .712, 187);
        Trampoline c2 = new Trampoline(c);
        assertEquals("Copied trampoline with 100 weight", 100, c2.getWeightOfJumper());
        assertEquals("Copied trampoline with 187 height.", 187, c2.getHeight());
        assertEquals("Copied trampoline with 71.2% bounciness.", .712, c2.getBounciness(), 0.00001);
    }


// Testing setter and getters

    @Test
    public void test_setter_and_getter_weight_0() {
        testClassDefinition();
        Trampoline c = new Trampoline(100, .45, 5);
        c.setWeightOfJumper(0);
        assertEquals("Set weight to invalid 0%", 140, c.getWeightOfJumper());
    }

    @Test
    public void test_setter_and_getter_weight_50() {
        testClassDefinition();
        Trampoline c = new Trampoline(100, .45, 5);
        c.setWeightOfJumper(50);
        assertEquals("Set weight to lowest valid: 50", 50, c.getWeightOfJumper());
    }

    @Test
    public void test_setter_and_getter_weight_99() {
        testClassDefinition();
        Trampoline c = new Trampoline(100, .45, 5);
        c.setWeightOfJumper(300);
        assertEquals("Set weight to highest valid: 300", 300, c.getWeightOfJumper());
    }

    @Test
    public void test_setter_and_getter_weight_301() {
        testClassDefinition();
        Trampoline c = new Trampoline(100, .45, 5);
        c.setWeightOfJumper(301);
        assertEquals("Set weight to invalid 301", 140, c.getWeightOfJumper());
    }

    // Test overriden method getBounciness

    @Test
    public void test_getBounciness_weight_300() {
        testClassDefinition();
        Trampoline c = new Trampoline(300, .45, 5);
        assertEquals("Expecting overridden getBounciness to adjust bounciness from 45% to 1.2 * 45% due to weight",
                .54, c.getBounciness(), 0.00001);

    }

    @Test
    public void test_getBounciness_weight_201() {
        testClassDefinition();
        Trampoline c = new Trampoline(201, .75, 5);
        assertEquals("Expecting overridden getBounciness to adjust bounciness from 75% to 1.2 * 75% due to weight",
                .9, c.getBounciness(), 0.00001);

    }

    @Test
    public void test_getBounciness_weight_250() {
        testClassDefinition();
        Trampoline c = new Trampoline(250, .90, 5);
        assertEquals("Expecting overridden getBounciness to adjust bounciness from 90% to min(99%,1.2 * 90%) due to weight",
                .99, c.getBounciness(), 0.00001);

    }

    @Test
    public void test_getBounciness_weight_200() {
        testClassDefinition();
        Trampoline c = new Trampoline(200, .45, 5);
        assertEquals("Expecting overridden getBounciness to adjust bounciness from 45% to 1.1 * 45% due to weight",
                .495, c.getBounciness(), 0.00001);

    }

    @Test
    public void test_getBounciness_weight_151() {
        testClassDefinition();
        Trampoline c = new Trampoline(151, .75, 5);
        assertEquals("Expecting overridden getBounciness to adjust bounciness from 75% to 1.1 * 75% due to weight",
                .825, c.getBounciness(), 0.00001);

    }

    @Test
    public void test_getBounciness_weight_175() {
        testClassDefinition();
        Trampoline c = new Trampoline(175, .95, 5);
        assertEquals("Expecting overridden getBounciness to adjust bounciness from 90% to min(99%,1.1 * 95%) due to weight",
                .99, c.getBounciness(), 0.00001);

    }

    @Test
    public void test_getBounciness_weight_99() {
        testClassDefinition();
        Trampoline c = new Trampoline(99, .45, 5);
        assertEquals("Expecting overridden getBounciness to adjust bounciness from 45% to .75 * 45% due to weight",
                .3375, c.getBounciness(), 0.00001);

    }

    @Test
    public void test_getBounciness_weight_50() {
        testClassDefinition();
        Trampoline c = new Trampoline(50, .75, 5);
        assertEquals("Expecting overridden getBounciness to adjust bounciness from 75% to .75 * 75% due to weight",
                .5625, c.getBounciness(), 0.00001);

    }

    // Ensuring bounce and numOfBounces use getBounciness method rather than bounciness instance variable

    @Test
    public void test_bounce_fromHeight5_weight50() {
        testClassDefinition();
        Trampoline c = new Trampoline(50, .75, 5);
        c.bounce();

        assertEquals("Checking height after bounce (weight: 50, height:5, bounciness .75)", 2, c.getHeight());
    }

    @Test
    public void test_bounce_fromHeight98_weight175() {
        testClassDefinition();
        Trampoline c = new Trampoline(175, .75, 98);
        c.bounce();

        assertEquals("Checking height after bounce (weight: 175, height:5, bounciness .75)", 80, c.getHeight());
    }

    @Test
    public void test_bounce_fromHeight98_weight250() {
        testClassDefinition();
        Trampoline c = new Trampoline(250, .75, 98);
        c.bounce();

        assertEquals("Checking height after bounce (weight: 175, height:5, bounciness .75)", 88, c.getHeight());
    }

    @Test
    public void test_numberOfBounces_ManyBounces() {
        testClassDefinition();
        Trampoline c = new Trampoline(300, .84, 12345);
        assertEquals("Trampoline with height 12345 and jumper weight 300, bounciness .8 will bounce 538 times.", 538, c.numberOfBounces());
    }

////////////// End of test methods /////////////////////////////


}
