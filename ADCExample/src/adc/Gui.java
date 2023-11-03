package adc;

import java.awt.Color;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author www.edsim51.com
 */
class Gui extends JPanel {
    
    private ADC adc;

    private boolean p32set;
    
    Gui(boolean small, Board board) {
        adc = new ADC(board);

        //Action Listener of Checkbox
        ActionListener cblisten = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                if(p32set){
                    board.clearPortPin(3, 2);
                    p32set = false;
                }else{
                    board.setPortPin(3, 2);
                    p32set = true;
                }
            }
        };
     
        
        this.setBackground(new Color(204, 255, 102));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        JCheckBox checkBox = new JCheckBox("P3.2");
        checkBox.addActionListener(cblisten);
        JPanel sp = new JPanel();
        sp.add(checkBox);
        this.add(sp);

   
    }
      
    void setSize(boolean small) {
        adc.getGraphics().setSize(small);
    }
    
    ADC getADC() {
        return adc;
    }
    
}