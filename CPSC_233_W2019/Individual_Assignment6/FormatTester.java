import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public abstract class FormatTester {
    private String classname;
    private String filename;

    public FormatTester(String classname, boolean forEclipse) {
        this.classname = classname;
        if (forEclipse) {
            this.filename = "src/" + classname + ".java";
        } else {
            this.filename = classname + ".java";
        }
    }

    protected boolean hasMethod(String signature) {
        boolean contains = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
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

    protected boolean toStrInvokesParentToStr() {
        boolean callsGetter = false;
        boolean callsParent = false;


        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
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

    protected boolean hasRequiredAbstractMethods(String[] abstractMethods) {
        boolean[] methodsAbstract = new boolean[abstractMethods.length];
        for (int index = 0; index < abstractMethods.length; index++) {
            methodsAbstract[index] = false;
        }
        boolean classIsAbstract = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("public abstract class " + classname)) {
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

    protected boolean hasRequiredAbstractMethods(String[] abstractMethods, String accessModifier) {
        boolean[] methodsAbstract = new boolean[abstractMethods.length];
        for (int index = 0; index < abstractMethods.length; index++) {
            methodsAbstract[index] = false;
        }
        boolean classIsAbstract = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("public abstract class " + classname)) {
                    classIsAbstract = true;
                } else {
                    for (int index = 0; index < abstractMethods.length; index++) {
                        String stmt = accessModifier + " abstract " + abstractMethods[index];
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

    protected boolean hasRequiredProtectedMethods(String[] protectedMethods) {
        boolean[] methodsProtected = new boolean[protectedMethods.length];
        for (int index = 0; index < protectedMethods.length; index++) {
            methodsProtected[index] = false;
        }
        boolean allProtected = true;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
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

    protected boolean instanceVariablesArePrivate(String[] instanceVars) {
        boolean[] varsPrivate = new boolean[instanceVars.length];
        for (int index = 0; index < instanceVars.length; index++) {
            varsPrivate[index] = false;
        }
        boolean allPrivate = true;


        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
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

    protected boolean noDefaultConstructor() {
        boolean  noDefault = true;
        String[] versions  = new String[9];
        versions[0] = "public " + classname + "()";
        versions[1] = "public " + classname + " ()";
        versions[2] = "public " + classname + " ( )";
        versions[3] = "protected " + classname + "()";
        versions[4] = "protected " + classname + " ()";
        versions[5] = "protected " + classname + " ( )";
        versions[6] = classname + "()";
        versions[7] = classname + " ()";
        versions[8] = classname + " ( )";

        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
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
