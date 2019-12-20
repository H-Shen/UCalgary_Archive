public class Person {
    private String name;

    public Person() {
        name = "No name yet";
    }


    public Person(String initialName) {
        name = initialName;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void writeOutput() {
        System.out.println("Name: " + name);
    }


    public boolean hasSameName(Person otherPerson) {
        return this.name.equalsIgnoreCase(otherPerson.name);
    }
}