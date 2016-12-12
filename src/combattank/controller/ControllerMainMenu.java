package combattank.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import combattank.view.MainMenu;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ControllerMainMenu {
    static ArrayList<Socket> serverList = new ArrayList<Socket>();
    private MainMenu menuView;

    public ControllerMainMenu(MainMenu menuView){
        this.menuView = menuView;
    }

    public void checkHosts() {
        String host = null;
        serverList.clear();
        
        ExecutorService executor = Executors.newCachedThreadPool();	
        
        // TESTA LOCALHOST
        try {
            if (InetAddress.getByName(null).isReachable(3)) {
                Socket newSock = new Socket(host, 10555);
                serverList.add(newSock);                
            } else
                System.out.println(" is NOT reachable");
        } catch (UnknownHostException ex) {
            Logger.getLogger(ipChecker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ipChecker.class.getName()).log(Level.SEVERE, null, ex);
        }                  
        
        for (int i = 0; i <= 25; i++) {
            ipChecker r = new ipChecker("192.168.0", i * 10 + 1, i * 10 + 10);
            r.setList(serverList);
            executor.submit(r);
        }
        /*ipChecker r = new ipChecker("192.168.0", 3, 3);
        r.setList(serverList);
                executor.submit(r);*/
        try {
            executor.awaitTermination(1, TimeUnit.SECONDS);
            menuView.list.setListData(serverList.toArray());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

class ipChecker implements Runnable {
	private int startingIp, endingIp;
	private String subnet = "192.168.0";
	private ArrayList<Socket> serverList;
	public ipChecker(String subnet, int startingIp, int endingIp) {
            this.subnet = subnet;
            this.startingIp = startingIp;

            this.endingIp = endingIp;
            if (endingIp > 255) this.endingIp = 255;
	}
	public void setList(ArrayList<Socket> list){
		serverList = list;
	}
	@Override
	public void run() {
            String host = null;
            
            for (int ip = startingIp; ip <= endingIp; ip++) {
                host = subnet + "." + ip;
                try {
                    if (InetAddress.getByName(host).isReachable(3)) {
                        System.out.println(host + " is reachable");
                        Socket newSock = new Socket(host, 10555);
                        serverList.add(newSock);
                        //Socket socketClient = new Socket(host, 10555);
                    }
                } catch (UnknownHostException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
            }
	}
}
