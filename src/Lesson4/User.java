package Lesson4;

public class User {

    private static int id = 0;
    private String name;

    public User(String name) {
        this.id = ++id;
        this.name = name;
        UserList.add(this);
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

    @Override
    public String toString() {
        return this.name;
    }
}
