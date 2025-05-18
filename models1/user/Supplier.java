
package models1.user;

public class Supplier extends User {
    public Supplier(String username, String password) {
        super(username, password);
    }

    @Override
    public String toString() {
        return getUsername();
    }
}
