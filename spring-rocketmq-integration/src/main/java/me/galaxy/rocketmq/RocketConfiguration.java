package me.galaxy.rocketmq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description
 * @Author duanxiaolei@bytedance.com
 * @Date 2019-06-17 14:21
 **/
public class RocketConfiguration {

    private List<String> nameServer = new LinkedList<String>();

    public void setNameServer(String... nameServer) {
        this.nameServer = Arrays.asList(nameServer);
    }

    public void setNameServer(List<String> nameServer) {
        this.nameServer = new ArrayList<String>(nameServer);
    }

    public List<String> getNameServer() {
        return nameServer;
    }
    
}
