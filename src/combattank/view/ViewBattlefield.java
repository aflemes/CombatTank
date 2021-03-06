/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * SQM = Squaremeters;
 */
package combattank.view;

import componente.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import combattank.model.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author skyli
 */
public class ViewBattlefield extends javax.swing.JFrame {

	/**
	 * Creates new form NewJFrame
	 */
	private int matBattleField[][];
	private battleField campoBatalha;
	private lifeBar lifeBarAux;
	private ServerSocket socketServidor;
	private transient Socket socketClient[];
	private int qtdePlayers = 1;
        private gameOver jpGameOver;
        private victory jpVictory;
        private boolean lgEscutou;
        private int idTankReal;
        private boolean playersVivo[];

	public ViewBattlefield(ServerSocket server) {
            System.out.println("Servidor - " + server.toString());
            socketServidor = server;
            
            initComponents();
            initBattleField();
            initServer();
            initGameOver();
            initVictory();
            run();
	}

	public ViewBattlefield(Socket client) {
            System.out.println("Client - " + client.toString());
            
            socketClient = new Socket[4];
            socketClient[1] = client;            
            
            initComponents();
            initBattleField();
            initClient();
            initGameOver(); 
            initVictory();
            run();
	}
        
	private void run() {
            this.setVisible(true);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 475, Short.MAX_VALUE));
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 450, Short.MAX_VALUE));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void initLifeBar() {
            lifeBarAux = new lifeBar();
            lifeBarAux.setQtdeLife(3); // 3 vidas

            lifeBarAux.setLocation(330, 1);
            lifeBarAux.setSize(150, 40);
            this.add(lifeBarAux);
	}

	private void initBattleField() {
            initField();
            initLifeBar();
	}
        
        private void initField(){
            campoBatalha = new battleField();
            campoBatalha.setLocation(50, 100);
            campoBatalha.setSize(500, 500);
            campoBatalha.setVisible(true);
            matBattleField = campoBatalha.getMatBattleField();
            this.add(campoBatalha);
        }

	private void initServer() {
            // controllerTank tank = new controllerTank();

            tank tankTemp;
            JPanel sqmAux;
            tankTemp = new tank();
            tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-leste.png")));
            tankTemp.setSize(25, 25);
            tankTemp.setLocation(2, 2);
            tankTemp.setDirecao("leste");

            sqmAux = getSQM(0, 4);
            matBattleField[0][4] = 1;

            sqmAux.add(tankTemp);
            sqmAux.repaint();

            // CODIGO DO SERVIDOR
            idTankReal  = 1;
            
            new Thread() {
                @Override
                public void run() {
                    moveTank(idTankReal);
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    modelTCPTransf tcpTransfTemp;
                    
                    socketClient = new Socket[4];
                    playersVivo = new boolean[4];
                    
                    while (true){
                        //aceita as conexoes no servidor
                        try {
                            socketClient[qtdePlayers] = socketServidor.accept();
                            playersVivo[qtdePlayers] = true;
                        } catch (IOException ex) {
                            Logger.getLogger(ViewBattlefield.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        qtdePlayers++;
                        System.out.println(" aceitei a conexao ");

                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    listenerServer(qtdePlayers - 1);
                                } catch (IOException ex) {}
                            }
                        }.start();
                        
                        
                        
                    }
                }
            }.start();
	}
        
		private void createPlayer(){
			System.out.println("CREATEOKAYER");
			modelTCPTransf tcpTransfTemp;
			tcpTransfTemp = new modelTCPTransf();
            tcpTransfTemp.setAcao("sync");
            tcpTransfTemp.setIdTank(qtdePlayers);
            //
            sendData(tcpTransfTemp,qtdePlayers - 1);
            
            //
            addTankTo(qtdePlayers);
            
            //SINCRONIZA COM TODAS INSTANCIAS                        
            for (int i = 1; i < qtdePlayers; i++) {
                tcpTransfTemp = new modelTCPTransf();
                tcpTransfTemp.setAcao("add");
                tcpTransfTemp.setIdTank(qtdePlayers);
                
                sendData(tcpTransfTemp,i);                            
            }
		}
        private void listenerServer(int indice) throws IOException{
            modelTCPTransf tcpTemp = null;

            while (true){
                ObjectInputStream clientInputStream;
                clientInputStream = new ObjectInputStream(socketClient[indice].getInputStream());

                try {
                    tcpTemp = (modelTCPTransf)clientInputStream.readObject();                    
                } catch (ClassNotFoundException ex) {
                    //Logger.getLogger(ViewBattlefield.class.getName()).log(Level.SEVERE, null, ex);
                }   
                
                trataRetorno(tcpTemp,1);
            }
            
        }

	private void initClient() {
            int idTank = 0;
            
            tank tankTemp;
            JPanel sqmAux;
            tankTemp = new tank();
            tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-leste.png")));
            tankTemp.setSize(25, 25);
            tankTemp.setLocation(2, 2);
            tankTemp.setDirecao("leste");

            sqmAux = getSQM(0, 4);
            matBattleField[0][4] = 1;

            sqmAux.add(tankTemp);
            sqmAux.repaint();
            //
            playersVivo = new boolean[4];
            modelTCPTransf tcpTransfer = new modelTCPTransf();
            tcpTransfer.setAcao("create");
            sendData(tcpTransfer,1);
            try {
                getIdTank();
            } catch (IOException ex) {
                Logger.getLogger(ViewBattlefield.class.getName()).log(Level.SEVERE, null, ex);
            }

            new Thread() {
                @Override
                public void run() {
                    try {
                        receiveInputs(idTankReal);
                    } catch (IOException e) {
                        System.out.println("Ocorreu um erro esperado, mas nï¿½o tï¿½o esperado assim para tratarmos.");
                        e.printStackTrace();
                    }
                }
            }.start();
	}
        
        private void getIdTank() throws IOException{
            modelTCPTransf tcpTransfTemp = null;
            ObjectInputStream clientInputStream = new ObjectInputStream(socketClient[1].getInputStream());

            try {
                tcpTransfTemp = (modelTCPTransf)clientInputStream.readObject();                    
            } catch (ClassNotFoundException ex) {
                //Logger.getLogger(ViewBattlefield.class.getName()).log(Level.SEVERE, null, ex);
            }                
            
            trataRetorno(tcpTransfTemp, idTankReal);
        }
	
        private void sendData(modelTCPTransf tcpTransfTemp,int indice){
            if (socketClient[indice] == null)
                return;
            
            System.out.println(" [indice] " + String.valueOf(indice));
                
            try {
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(socketClient[indice].getOutputStream());
                clientOutputStream.writeObject(tcpTransfTemp);
            } catch (IOException e1) {                    
                e1.printStackTrace();
            }
	}

	private void moveTank(int idTank) {
            this.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    modelTCPTransf tcpTransfer;
                    
                    if (verificaVidas() <= 0)  
                        return;
                    
                    if (e.getKeyCode() != 32) {
                        moveTankByKeyPress(e.getKeyCode(), idTank);
                    } else
                        shootTank(idTank);

                    
                    tcpTransfer = new modelTCPTransf();
                    tcpTransfer.setAcao("mov");
                    tcpTransfer.setIdTank(idTankReal);
                    System.out.println(" idTank moveTank " + String.valueOf(idTankReal));
                    tcpTransfer.setKeyCode(e.getKeyCode());
                    
                    if (socketServidor == null){
                        //ALWAYS SEND TO THE SERVER
                        sendData(tcpTransfer,1);
                    }
                    else{
                        for (int i = 1; i < qtdePlayers; i++) {
                            System.out.println(" oi ");
                            sendData(tcpTransfer,i);
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
	}
        
        private void receiveInputs(int idTank) throws IOException {
            int keyCode,idTankTemp;
            modelTCPTransf tcpTransfTemp = null;
            ObjectInputStream clientInputStream = null;
            
            while (true) {
                if (this.lifeBarAux.getQtdeLife() <= 0)
                    if (socketServidor == null)
                        break;
                
                clientInputStream = new ObjectInputStream(socketClient[1].getInputStream());                
                
                try {
                    tcpTransfTemp = (modelTCPTransf)clientInputStream.readObject();                    
                } catch (ClassNotFoundException ex) {
                    //Logger.getLogger(ViewBattlefield.class.getName()).log(Level.SEVERE, null, ex);
                }                
                System.out.println(" getAcao() " + String.valueOf(tcpTransfTemp.getAcao()));
                System.out.println(" getIdTank() " + String.valueOf(tcpTransfTemp.getIdTank()));

                trataRetorno(tcpTransfTemp, tcpTransfTemp.getIdTank());
            }
	}
        
        private void addListenerSocket(int indice){
            new Thread() {
                @Override
                public void run() {
                    try {
                        listenerSocket(indice);
                    } catch (IOException ex) {
                        Logger.getLogger(ViewBattlefield.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ViewBattlefield.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
        }
        
        private void listenerSocket(int indice) throws IOException, ClassNotFoundException{
            modelTCPTransf tcpTransfTemp;
            
            ObjectInputStream clientInputStream = new ObjectInputStream(socketClient[indice].getInputStream());
            
            tcpTransfTemp = (modelTCPTransf)clientInputStream.readObject();
            
            trataRetorno(tcpTransfTemp, 2);
        }
        
        private void trataRetorno(modelTCPTransf tcpTransfTemp, int idTank){
            int keyCode;
            
            switch(tcpTransfTemp.getAcao()){
                case "Hit":
                    if (tcpTransfTemp.getIdTank() == idTankReal){
                        this.lifeBarAux.downlife();

                        if (this.lifeBarAux.getQtdeLife() == 0){
                            faleci(idTankReal);
                            this.jpGameOver.setVisible(true);
                        }
                    }
                    break;
                case "add":                    
                    addTankTo(tcpTransfTemp.getIdTank());
                    break;
                case "sync":
                    idTankReal = tcpTransfTemp.getIdTank();
                    //cria thread move tank                    
                    atribuiMoveTank(idTankReal);

                    addTank(tcpTransfTemp);
                    break;
                case "death":
                    playersVivo[tcpTransfTemp.getIdTank()] = false;
                    
                    setDeathTank(tcpTransfTemp);                   
                    verifyWinner();
                    break;
                case "mov":
                    //envia para todas os sockets
                    if (socketServidor != null)
                        for (int i = 1; i < qtdePlayers; i++) {
                            sendData(tcpTransfTemp, i);
                        }
                    if (tcpTransfTemp.getIdTank() == idTankReal)
                        break;
                    
                    keyCode = tcpTransfTemp.getKeyCode();
                    idTank = tcpTransfTemp.getIdTank();
                    
                    if (keyCode != 32)                        
                        moveTankByKeyPress(keyCode, idTank);                        
                    else
                        shootTank(idTank);   
                    break;
                case "create":
                	createPlayer();
                	break;
                default:
                    break;
                    
            }
            
            lgEscutou = true;
            synchronized (this){
                this.notify();
            }
        }
        
        private void atribuiMoveTank(int idTank){
            new Thread() {
                @Override
                public void run() {
                    moveTank(idTank);
                }
            }.start();
        }

	private void moveTankByKeyPress(int keyCode, int idTank) {
            int[] oldSQM = null;
            int xAux, yAux;
            
            // encontra o tank e remove do panel
            oldSQM = getSQMByTank(idTank);
            if (oldSQM != null) {
                if (adicionaTank(oldSQM, keyCode, idTank)) {
                    removeTank(oldSQM);
                }
            }
	}

	// procura no campo de batalha o jpanel com o nome correspondente
	private SQM getSQM(int x, int y) {
            SQM sqmAux = new SQM();

            for (int i = 0; i < campoBatalha.getComponentCount(); i++) {
                try {
                    sqmAux = (SQM) campoBatalha.getComponent(i);
                } catch (Exception E) {
                }

                if (sqmAux.getPosX() == x && sqmAux.getPosY() == y)
                    return (SQM) campoBatalha.getComponent(i);
            }

            return null;
	}
        
        private void addTankTo(int qtdePlayersTemp){
            tank tankTemp = null;
            
            JPanel sqmAux = null;
            
            for (int i = 1; i <= qtdePlayersTemp; i++) {
                
                if (i == idTankReal)
                    continue;
                
                tankTemp = new tank();
                sqmAux = new JPanel();   
            
                tankTemp.setSize(25, 25);
                tankTemp.setLocation(2, 2);                
                System.out.println(" i " + String.valueOf(i));
                
                switch (i) {
                    case 2:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-oeste.png")));
                        tankTemp.setDirecao("oeste");
                        sqmAux = getSQM(11, 4);
                        matBattleField[11][4] = 2;
                        break;
                    case 3:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-tres-sul.png")));
                        tankTemp.setDirecao("sul");
                        sqmAux = getSQM(6, 0);
                        matBattleField[6][0] = 3;
                        break;
                    case 4:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-quatro-norte.png")));
                        tankTemp.setDirecao("norte");
                        sqmAux = getSQM(6, 8);
                        matBattleField[6][8] = 4;
                        break;
                }
                sqmAux.add(tankTemp);
                sqmAux.repaint();
            }
        }

        private void addTank(modelTCPTransf tcpTransfTemp){
            tank tankTempComponente = new tank();
            JPanel sqmAux = null;
            
            tankTempComponente.setSize(25, 25);
            tankTempComponente.setLocation(2, 2);
            
            switch (tcpTransfTemp.getIdTank()){
                case 2:
                    tankTempComponente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-oeste.png")));
                    tankTempComponente.setDirecao("oeste");
                    sqmAux = getSQM(11, 4);
                    matBattleField[11][4] = 2;
                    break;
                case 3:
                    tankTempComponente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-tres-sul.png")));
                    tankTempComponente.setDirecao("sul");
                    sqmAux = getSQM(6, 0);
                    matBattleField[6][0] = 3;
                    break;
                case 4:
                    tankTempComponente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-quatro-norte.png")));
                    tankTempComponente.setDirecao("norte");
                    sqmAux = getSQM(6, 8);
                    matBattleField[6][8] = 4;
                    break;
                }
                sqmAux.add(tankTempComponente);
                sqmAux.repaint();
        }
       
	private int[] getSQMByTank(int idTank) {
            int[] retorno = new int[2];

            for (int i = 0; i < matBattleField.length; i++) {
                for (int j = 0; j < matBattleField[j].length; j++) {
                    if (matBattleField[i][j] == idTank) {
                        retorno[0] = i;
                        retorno[1] = j;

                        return retorno;
                    }
                }
            }
            return null;
	}

	private void removeTank(int[] oldSQM) {
            JPanel jPanelAux;
            jPanelAux = getSQM(oldSQM[0], oldSQM[1]);
            // remove tank do campo de batalha
            matBattleField[oldSQM[0]][oldSQM[1]] = 0;
            jPanelAux.removeAll();
            jPanelAux.repaint();
	}

	private boolean adicionaTank(int[] oldSQM, int keyCode, int idTank) {
            int posX = oldSQM[0];
            int posY = oldSQM[1];
            boolean lgAlterou = false;
            SQM sqmAux = new SQM();

            tank tankTemp = new tank();
            tankTemp.setSize(25, 25);
            tankTemp.setLocation(2, 2);
            
            switch (keyCode) {
            case 38:
            case 87:
                if (posY == 0)
                    return false;
                else
                    if (matBattleField[posX][posY - 1] != 0)
                        return false;

                sqmAux = getSQM(posX, posY - 1);
                matBattleField[posX][posY - 1] = idTank;
                
                switch (idTank){
                    case 1:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-norte.png")));
                        break;
                    case 2:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-norte.png")));
                        break;
                    case 3:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-tres-norte.png")));
                        break;
                    case 4:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-quatro-norte.png")));
                        break;
                }
                
                tankTemp.setDirecao("norte");
                
                lgAlterou = true;
                break;
            case 39:
            case 68:
                if (matBattleField.length == (posX + 1))
                    return false;
                else
                    if (matBattleField[posX + 1][posY] != 0)
                        return false;

                sqmAux = getSQM(posX + 1, posY);
                matBattleField[posX + 1][posY] = idTank;
                switch (idTank){
                    case 1:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-leste.png")));
                        break;
                    case 2:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-leste.png")));
                        break;
                    case 3:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-tres-leste.png")));
                        break;
                    case 4:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-quatro-leste.png")));
                        break;
                }
                
                tankTemp.setDirecao("leste");
                
                lgAlterou = true;
                break;
            case 40:
            case 83:
                if (matBattleField[0].length == (posY + 1))
                    return false;
                else
                    if (matBattleField[posX][posY + 1] != 0)
                        return false;

                sqmAux = getSQM(posX, posY + 1);
                matBattleField[posX][posY + 1] = idTank;
                switch (idTank){
                    case 1:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-sul.png")));
                        break;
                    case 2:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-sul.png")));
                        break;
                    case 3:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-tres-sul.png")));
                        break;
                    case 4:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-quatro-sul.png")));
                        break;
                }
                        
                tankTemp.setDirecao("sul");
                
                lgAlterou = true;
                break;
            case 65:
            case 37:
                if (posX == 0)
                    return false;
                else
                    if (matBattleField[posX - 1][posY] != 0)
                        return false;

                sqmAux = getSQM(posX - 1, posY);
                matBattleField[posX - 1][posY] = idTank;
                
                switch (idTank){
                    case 1:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-oeste.png")));
                        break;
                    case 2:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-oeste.png")));
                        break;
                    case 3:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-tres-oeste.png")));
                        break;
                    case 4:
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-quatro-oeste.png")));
                        break;
                }
                
                tankTemp.setDirecao("oeste");
                
                lgAlterou = true;
                break;
            }

            if (lgAlterou){
                sqmAux.add(tankTemp);
                sqmAux.repaint();               
                
                return true;
            }
            return false;
	}

	private void shootTank(int idTank) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        shoot(idTank);
                    } catch (InterruptedException ex) {
                        System.out.println("exception");
                        Logger.getLogger(ViewBattlefield.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.start();
	}

	private void shoot(int idTank) throws InterruptedException {
            int[] posicao = null;

            // encontra o tank para pegar a direcao
            posicao = getSQMByTank(idTank);
            JPanel sqmAux = getSQM(posicao[0], posicao[1]);
            tank tankTemp = (tank) sqmAux.getComponent(0);            

            if (tankTemp.isShoot())
                return;
            else
                tankTemp.setShoot(true);

            switch (tankTemp.getDirecao()) {
                case "norte":
                    direcionaTiroNorte(sqmAux, posicao);
                    break;
                case "sul":
                    direcionaTiroSul(sqmAux, posicao);
                    break;
                case "leste":
                    direcionaTiroLeste(sqmAux, posicao);
                    break;
                case "oeste":
                    direcionaTiroOeste(sqmAux, posicao);
                    break;
            }
            tankTemp.setShoot(false);
	}

	private void direcionaTiroNorte(JPanel sqmAux, int[] posicao) throws InterruptedException {
            JPanel jShoot = new JPanel();
            jShoot.setBackground(Color.red);
            jShoot.setSize(4, 16);
            jShoot.setLocation(12, 1);

            int aux = 1;

            for (int i = posicao[1]; i > 0; i--) {
                // verifica se o tiro acertou algum tank
                if (verificaAcerto(posicao[0], posicao[1] - aux))
                    break;
                
                sqmAux = getSQM(posicao[0], posicao[1] - aux);
                sqmAux.add(jShoot);
                sqmAux.repaint();

                Thread.sleep(70);

                sqmAux.remove(jShoot);
                sqmAux.repaint();
                aux++;
            }
	}

	private void direcionaTiroSul(JPanel sqmAux, int[] posicao) throws InterruptedException {
            JPanel jShoot = new JPanel();
            jShoot.setBackground(Color.red);
            jShoot.setSize(4, 16);
            jShoot.setLocation(12, 1);

            int aux = 1;

            for (int i = posicao[1]; i < matBattleField[0].length - 1; i++) {
                // verifica se o tiro acertou algum tank
                if (verificaAcerto(posicao[0], posicao[1] + aux))
                    break;
                sqmAux = getSQM(posicao[0], posicao[1] + aux);
                sqmAux.add(jShoot);
                sqmAux.repaint();

                Thread.sleep(70);

                sqmAux.remove(jShoot);
                sqmAux.repaint();
                aux++;
            }
	}

	private void direcionaTiroLeste(JPanel sqmAux, int[] posicao) throws InterruptedException {
            JPanel jShoot = new JPanel();
            jShoot.setBackground(Color.red);
            jShoot.setSize(16, 4);
            jShoot.setLocation(1, 12);

            int aux = 1;
            
            for (int i = posicao[0]; i < matBattleField.length - 1; i++) {
                // verifica se o tiro acertou algum tank
                if (verificaAcerto(posicao[0] + aux, posicao[1]))
                    break;

                sqmAux = getSQM(posicao[0] + aux, posicao[1]);
                sqmAux.add(jShoot);
                sqmAux.repaint();

                Thread.sleep(70);

                sqmAux.remove(jShoot);
                sqmAux.repaint();
                aux++;
            }
	}

	private void direcionaTiroOeste(JPanel sqmAux, int[] posicao) throws InterruptedException {
            JPanel jShoot = new JPanel();
            jShoot.setBackground(Color.red);
            jShoot.setSize(16, 4);
            jShoot.setLocation(1, 12);

            int aux = 1;
            
            for (int i = posicao[0]; i > 0; i--) {
                // verifica se o tiro acertou algum tank
                if (verificaAcerto(posicao[0] - aux, posicao[1]))
                    break;

                sqmAux = getSQM(posicao[0] - aux, posicao[1]);
                sqmAux.add(jShoot);
                sqmAux.repaint();

                Thread.sleep(70);

                sqmAux.remove(jShoot);
                sqmAux.repaint();
                aux++;
            }
	}

	public boolean verificaAcerto(int posX, int posY) {
            modelTCPTransf tcpTransfer;
            
            if (matBattleField[posX][posY] != 0) {
                tcpTransfer = new modelTCPTransf();
                tcpTransfer.setAcao("Hit");
                tcpTransfer.setIdTank(matBattleField[posX][posY]);               
                
                if (matBattleField[posX][posY] == 1)
                    sendData(tcpTransfer,(matBattleField[posX][posY]));
                else
                    sendData(tcpTransfer,(matBattleField[posX][posY]) - 1);
                return true;
            } else
                return false;
	}
        
        private void initGameOver(){
            jpGameOver = new gameOver();
            jpGameOver.setSize(this.getWidth(), this.getHeight());
            jpGameOver.setLocation(0, 0);
            jpGameOver.setVisible(false);            
            jpGameOver.setBackground(new Color(0,0,0,125));
           
            this.add(jpGameOver);
            this.repaint();
            this.setComponentZOrder(jpGameOver, 0);
        }
        
        private void initVictory(){
            jpVictory = new victory();
            jpVictory.setSize(this.getWidth(), this.getHeight());
            jpVictory.setLocation(0, 0);
            jpVictory.setVisible(false);            
            jpVictory.setBackground(new Color(0,0,0,125));
           
            this.add(jpVictory);
            this.repaint();
            this.setComponentZOrder(jpVictory, 0);
        }
        
        private void faleci(int idTank){
            modelTCPTransf tcpTransfer = new modelTCPTransf();
            tcpTransfer.setAcao("death");
            tcpTransfer.setIdTank(idTank);

            if (socketServidor == null)
                sendData(tcpTransfer,1);
            else
                for (int i = 1; i < qtdePlayers; i++) {
                    sendData(tcpTransfer,i);                    
                }
        }
        
        private void setDeathTank(modelTCPTransf tcpTransfTemp){
            JPanel sqmAux;
            tank tankTemp = new tank();
            
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 9; j++) {                       
                    if (matBattleField[i][j] == tcpTransfTemp.getIdTank()){
                        sqmAux = getSQM(i, j);
                        sqmAux.removeAll();
                        //
                        tankTemp.setSize(25, 25);                        
                        tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/death.png")));
                        sqmAux.add(tankTemp);
                        sqmAux.repaint();
                        break;
                    }
                }                
            }                    
        }
        
        private void verifyWinner(){
            for (int i = 1; i <= qtdePlayers; i++) {
                if (i == idTankReal)
                    continue;
                if (playersVivo[i])                    
                    return;
            }
            
            //nao tem nenhum tank vivo
            this.jpVictory.setVisible(true);
        }
        
        private int verificaVidas(){
            return this.lifeBarAux.getQtdeLife();
        }
        
        // Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables
}
