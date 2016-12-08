/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package combattank.model;

import java.io.Serializable;

/**
 *
 * @author allan.lemes
 */
public class modelTCPTransf implements Serializable{
    private String acao;
    private int idTank;   
    private int keyCode;

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public int getIdTank() {
        return idTank;
    }

    public void setIdTank(int idTank) {
        this.idTank = idTank;
    }
    
}
