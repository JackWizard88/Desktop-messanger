package Lesson4;

public class User {

    private static int id = 0;
    private String name;

    public User(String name) {
        this.id = ++id;
        this.name = name;
        UserList.add(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getId() {
        return id;
    }
}
