/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * SQM = Squaremeters;
 */
package combattank.view;

import combattank.controller.*;
import componente.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
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
	private Socket socketClient;
	private boolean ready;
        private gameOver jpGameOver;
        private victory jpVictory;

	public ViewBattlefield(ServerSocket server) {
            System.out.println("Servidor - " + server.toString());
            socketServidor = server;
            initComponents();
            initLifeBar();
            initBattleField();
            initServer();
            initGameOver();
            initVictory();
            run();
	}

	public ViewBattlefield(Socket client) {
            System.out.println("Client - " + client.toString());
            socketClient = client;            
            initComponents();
            initLifeBar();
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

	/**
	 * @param args
	 *            the command line arguments
	 */
	// public static void main(String args[]) {
	// /* Set the Nimbus look and feel */
	// //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code
	// (optional) ">
	// /* If Nimbus (introduced in Java SE 6) is not available, stay with the
	// default look and feel.
	// * For details see
	// http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
	// */
	// try {
	// for (javax.swing.UIManager.LookAndFeelInfo info :
	// javax.swing.UIManager.getInstalledLookAndFeels()) {
	// if ("Nimbus".equals(info.getName())) {
	// javax.swing.UIManager.setLookAndFeel(info.getClassName());
	// break;
	// }
	// }
	// } catch (ClassNotFoundException ex) {
	// java.util.logging.Logger.getLogger(ViewBattlefield.class.getName()).log(java.util.logging.Level.SEVERE,
	// null, ex);
	// } catch (InstantiationException ex) {
	// java.util.logging.Logger.getLogger(ViewBattlefield.class.getName()).log(java.util.logging.Level.SEVERE,
	// null, ex);
	// } catch (IllegalAccessException ex) {
	// java.util.logging.Logger.getLogger(ViewBattlefield.class.getName()).log(java.util.logging.Level.SEVERE,
	// null, ex);
	// } catch (javax.swing.UnsupportedLookAndFeelException ex) {
	// java.util.logging.Logger.getLogger(ViewBattlefield.class.getName()).log(java.util.logging.Level.SEVERE,
	// null, ex);
	// }
	// //</editor-fold>
	// //</editor-fold>
	//
	// /* Create and display the form */
	// java.awt.EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// new ViewBattlefield().setVisible(true);
	// }
	// });
	// }

	private void initLifeBar() {
            lifeBarAux = new lifeBar();
            lifeBarAux.setQtdeLife(3); // 3 vidas

            lifeBarAux.setLocation(330, 1);
            lifeBarAux.setSize(150, 40);
            this.add(lifeBarAux);
	}

	private void initBattleField() {
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

            tankTemp = new tank();
            tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-oeste.png")));
            tankTemp.setSize(25, 25);
            tankTemp.setLocation(2, 2);

            sqmAux = getSQM(11, 4);
            matBattleField[11][4] = 2;

            sqmAux.add(tankTemp);
            sqmAux.repaint();

            new Thread() {
                @Override
                public void run() {
                    moveTank(1);
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    try {
                        receiveInputs(2);
                    } catch (IOException e) {
                        System.out.println("Ocorreu um erro esperado, mas n�o t�o esperado assim para tratarmos.");
                        e.printStackTrace();
                    }
                }
            }.start();
	}

	private void initClient() {
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

            tankTemp = new tank();
            tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-oeste.png")));
            tankTemp.setSize(25, 25);
            tankTemp.setLocation(2, 2);

            sqmAux = getSQM(11, 4);
            matBattleField[11][4] = 2;

            sqmAux.add(tankTemp);
            sqmAux.repaint();


            new Thread() {
                @Override
                public void run() {
                    try {
                        receiveInputs(1);
                    } catch (IOException e) {
                        System.out.println("Ocorreu um erro esperado, mas n�o t�o esperado assim para tratarmos.");
                        e.printStackTrace();
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    moveTank(2);
                }
            }.start();

	}


	private void sendData(modelTCPTransf tcpTransfTemp){
            if (socketClient == null)
                return;
                
            try {
                ObjectOutputStream clientOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
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
                                   
                    if (verificaVidas() == 0)  
                        return;
                    
                    if (e.getKeyCode() != 32) {
                        moveTankByKeyPress(e.getKeyCode(), idTank);
                    } else
                        shootTank(idTank);

                    tcpTransfer = new modelTCPTransf();
                    tcpTransfer.setAcao("Mov");
                    tcpTransfer.setIdTank(idTank);
                    tcpTransfer.setKeyCode(e.getKeyCode());
                    
                    sendData(tcpTransfer);
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
	}
        
        private int verificaVidas(){
            return this.lifeBarAux.getQtdeLife();
        }

	private void receiveInputs(int idTank) throws IOException {
            int keyCode;
            modelTCPTransf tcpTransfTemp = null;
        
            if (idTank != 1)
                socketClient = socketServidor.accept();            
            
            while (true) {
                ObjectInputStream clientInputStream = new ObjectInputStream(socketClient.getInputStream());
                
                try {
                    tcpTransfTemp = (modelTCPTransf)clientInputStream.readObject();                    
                } catch (ClassNotFoundException ex) {
                    //Logger.getLogger(ViewBattlefield.class.getName()).log(Level.SEVERE, null, ex);
                }                
                
                if (tcpTransfTemp.getAcao().equals("Hit")){
                    if (tcpTransfTemp.getIdTank() != idTank){
                        this.lifeBarAux.downlife();
                    
                        if (this.lifeBarAux.getQtdeLife() == 0){
                            faleci(idTank);
                            this.jpGameOver.setVisible(true);
                        }
                    }
                }                                        
                else{ 
                    keyCode = tcpTransfTemp.getKeyCode();

                    if (keyCode != 32)                        
                        moveTankByKeyPress(keyCode, idTank);                        
                    else
                        shootTank(idTank);   
                }
                
            }
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

                sqmAux = getSQM(posX, posY - 1);
                matBattleField[posX][posY - 1] = idTank;
                if(idTank == 1)
                    tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-norte.png")));
                else tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-norte.png")));
                tankTemp.setDirecao("norte");
                
                lgAlterou = true;
                break;
            case 39:
            case 68:
                if (matBattleField.length == (posX + 1))
                    return false;

                sqmAux = getSQM(posX + 1, posY);
                matBattleField[posX + 1][posY] = idTank;
                if(idTank == 1)
                    tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-leste.png")));
                else tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-leste.png")));
                tankTemp.setDirecao("leste");
                
                lgAlterou = true;
                break;
            case 40:
            case 83:
                if (matBattleField[0].length == (posY + 1))
                    return false;

                sqmAux = getSQM(posX, posY + 1);
                matBattleField[posX][posY + 1] = idTank;
                if(idTank == 1)
                    tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-sul.png")));
                else tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-sul.png")));
                tankTemp.setDirecao("sul");
                
                lgAlterou = true;
                break;
            case 65:
            case 37:
                if (posX == 0)
                    return false;

                sqmAux = getSQM(posX - 1, posY);
                matBattleField[posX - 1][posY] = idTank;
                if(idTank == 1)
                    tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-um-oeste.png")));
                else tankTemp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/combattank/img/tank-dois-oeste.png")));
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
            tank tankTemp;
            tankTemp = (tank) sqmAux.getComponent(0);

            tankTemp.getDirecao();

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
                
                sendData(tcpTransfer);
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
            tcpTransfer.setAcao("Death");
            tcpTransfer.setIdTank(idTank);            

            sendData(tcpTransfer);
        }

	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables
}
