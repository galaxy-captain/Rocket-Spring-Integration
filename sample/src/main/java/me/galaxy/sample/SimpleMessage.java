package me.galaxy.sample;

/**
 * @Description
 * @Author galaxy-captain
 * @Date 2019-06-18 19:20
 **/
public class SimpleMessage {

    private String id;

    public SimpleMessage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SimpleMessage{" +
                "id='" + id + '\'' +
                '}';
    }

}
