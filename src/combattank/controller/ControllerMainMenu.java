package combattank.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import combattank.view.MainMenu;
import combattank.view.ViewBattlefield;

public class ControllerMainMenu {
	static ArrayList<Socket> serverList = new ArrayList<Socket>();
	private MainMenu menuView;
	
	public ControllerMainMenu(MainMenu menuView){
		this.menuView = menuView;
	}

	public void checkHosts() {
		serverList.clear();
		/*new Thread(){
            @Override
            public void run(){*/
            	ExecutorService executor = Executors.newCachedThreadPool();	
        		for (int i = 0; i <= 25; i++) {
        			ipChecker r = new ipChecker("192.168.0", i * 10 + 1, i * 10 + 10);
        			r.setList(serverList);
        			executor.submit(r);
        		}
            	/*ipChecker r = new ipChecker("192.168.0", 3, 3);
            	r.setList(serverList);
    			executor.submit(r);*/
        		try {
        			executor.awaitTermination(10, TimeUnit.SECONDS);
        			menuView.list.setListData(serverList.toArray());
        		} catch (InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
         /*   }
        }.start();
		*/
		
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
		for (int ip = startingIp; ip <= endingIp; ip++) {
			String host = subnet + "." + ip;
			try {
				if (InetAddress.getByName(host).isReachable(3)) {
					System.out.println(host + " is reachable");
					Socket newSock = new Socket(host, 10555);
					serverList.add(newSock);
					//Socket socketClient = new Socket(host, 10555);
				} else
					System.out.println(host + " is NOT reachable");
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
