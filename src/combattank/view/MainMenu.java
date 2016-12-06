package combattank.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.JTextArea;

import combattank.controller.ControllerMainMenu;

import javax.swing.JList;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class MainMenu {
	private JFrame frmCombatTank;
	public ControllerMainMenu menuController = new ControllerMainMenu(this);
	private ServerSocket servidor;
	private Socket client;
	public JList list;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                            /* Set the Nimbus look and feel */
                    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
                    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
                     * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
                     */
                    try {
                        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                            if ("Windows".equals(info.getName())) {
                                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                break;
                            }
                        }
                    } catch (ClassNotFoundException ex) {
                        java.util.logging.Logger.getLogger(ViewBattlefield.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(ViewBattlefield.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(ViewBattlefield.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(ViewBattlefield.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                    }

                            MainMenu window = new MainMenu();
                            window.frmCombatTank.setVisible(true);
                            window.menuController.checkHosts();
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
                }
            });
	}

	/**
	 * Create the application.
	 */
	public MainMenu() {
            initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
            frmCombatTank = new JFrame();
            frmCombatTank.setTitle("Combat Tank");
            frmCombatTank.setResizable(false);
            frmCombatTank.setBounds(100, 100, 450, 300);
            frmCombatTank.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frmCombatTank.getContentPane().setLayout(null);

            list = new JList();
            list.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent arg0) {
                    if (!arg0.getValueIsAdjusting()){
                        client = (Socket) list.getSelectedValue();
                        client.toString();
                        ViewBattlefield teste = new ViewBattlefield(client);
                        teste.setVisible(true);
                        //menuController.connect(list.getSelectedValue());

                    }
                }
            });

            list.setBounds(12, 13, 420, 178);
            frmCombatTank.getContentPane().add(list);

            JButton btnSair = new JButton("Sair");
            btnSair.setBounds(362, 233, 70, 25);
            frmCombatTank.getContentPane().add(btnSair);

            JButton btnCriarServidor = new JButton("Criar Servidor");
            btnCriarServidor.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        servidor = new ServerSocket(10555,0,InetAddress.getByName(null));
                        //menuController.checkHosts();
                        //list.enableInputMethods(false);
                        ViewBattlefield teste = new ViewBattlefield(servidor);
                        teste.setVisible(true);
                        
                         new Thread() {
                            @Override
                            public void run() {
                                try {
                                    servidor.accept();
                                } catch (IOException ex) {
                                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }.start();
                        
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
            });
            btnCriarServidor.setBounds(225, 233, 125, 25);
            frmCombatTank.getContentPane().add(btnCriarServidor);

            JButton btnRefresh = new JButton("Refresh");
            btnRefresh.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    menuController.checkHosts();
                }
            });
            btnRefresh.setBounds(118, 233, 95, 25);
            frmCombatTank.getContentPane().add(btnRefresh);
            frmCombatTank.setVisible(true);
	}    
}
