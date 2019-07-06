package me.galaxy.sample;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 19:20
 **/
public class SimpleMessage {

    private int id;

    public SimpleMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SimpleMessage{" +
                "id='" + id + '\'' +
                '}';
    }

}
