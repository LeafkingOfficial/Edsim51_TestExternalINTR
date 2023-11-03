package adc;

import edsim51sh.TargetBoard;

/**
 *
 * @author www.edsim51.com
 */
class Board extends TargetBoard {
    
    private Gui gui;
    
    Board(String hardwareSettingsFilename) {
        
        super(hardwareSettingsFilename);
             
        gui = new Gui(true, this);  
        this.init(gui, "ADC", "1.0.4");
        
        Mapping[] mappings = gui.getADC().getDataLineMappings();
        for (int i = 0; i < mappings.length; i++) {
            this.setPortPinDescription(mappings[i].portNumber, mappings[i].pinNumber, mappings[i].description);
        }
        
        Mapping mapping = gui.getADC().getRDMapping();
        this.setPortPinDescription(mapping.portNumber, mapping.pinNumber, mapping.description);
        
        mapping = gui.getADC().getWRMapping();
        this.setPortPinDescription(mapping.portNumber, mapping.pinNumber, mapping.description);
        
        mapping = gui.getADC().getINTRMapping();
        this.setPortPinDescription(mapping.portNumber, mapping.pinNumber, mapping.description);
        
        mapping = gui.getADC().getCSMapping();
        this.setPortPinDescription(mapping.portNumber, mapping.pinNumber, mapping.description);
        
    }
    
    public void reset() {
        gui.getADC().foredReset();
    }
    
    public void setTargetBoardGraphicsSize(boolean small) {
        gui.setSize(small);
    }

    public void updatePortPins() {
        gui.getADC().cycle(this.getInstructionElapsedTime());
    }
    
    public void updateTargetBoardGraphics() {
        gui.getADC().refreshGraphics();
    }
    
}