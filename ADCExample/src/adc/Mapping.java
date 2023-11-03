package adc;

/**
 *
 * @author www.edsim51.com
 */
class Mapping {

    int portNumber;
    int pinNumber;
    String description;

    Mapping(int portNumber, int pinNumber, String description) {
        this.portNumber = portNumber;
        this.pinNumber = pinNumber;
        this.description = description;
    }
    
}
