package adc;

import edsim51sh.EdSim51sh;

/**
 *
 * @author www.edsim51.com
 */
public class Main {
    
    public static void main(String[] args) {
        EdSim51sh simulator = new EdSim51sh();
        //simulator.launch(new Board("edsim51sh_ADCHardwareSettings.ser"));
        Board tb = new Board("edsim51sh_ADCSettings.ser");
        simulator.launch(tb);
        tb.clearPortPin(3, 2);
    }
    
}