package adc;

/**
 *
 * @author www.edsim51.com
 */
class ADC {

    /*
     * The ADC pins are mapped here. The data lines are on P2. RD is on P3.4,
     * WR is on P3.3, INTR is on P3.2 and CS is on P3.1
     */
                                 //DB0  DB1  DB2  DB3  DB4  DB5  DB6  DB7
    private final double data[] = {2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7}; // data lines on P2
    private final double RD = 3.4;
    private final double WR = 3.3;
    private final double INTR = 3.2;
    private final double CS = 3.1;
    
    /*
     * End of port pin mapping.
     */
    
    private int rdPort;
    private int rdPin;
    private int wrPort;
    private int wrPin;
    private int intrPort;
    private int intrPin;
    private int csPort;
    private int csPin;
    
    private ADCGraphics adcGraphics = new ADCGraphics();
    private Board board;
    private Mapping[] dataLinesMapping = new Mapping[data.length];
    private final int CONVERSION_TIME = 20; // in microseconds
    private boolean cs = true;
    private boolean rd = true;
    private boolean wr = true;
    private boolean previousWr = true;
    private boolean intr = true;
    private boolean[] internalBits = new boolean[8];
    private int analogueInput = 0;
    private int conversionState = CONVERSION_TIME;
    private boolean converting = false;

    ADC(Board board) {
        this.board = board;
        initDataLineMappings();
        rdPort = getPortNumber(RD);
        rdPin = getPinNumber(RD);
        wrPort = getPortNumber(WR);
        wrPin = getPinNumber(WR);
        intrPort = getPortNumber(INTR);
        intrPin = getPinNumber(INTR);
        csPort = getPortNumber(CS);
        csPin = getPinNumber(CS);
        foredReset();
    }

    Mapping[] getDataLineMappings() {
        return dataLinesMapping;
    }

    Mapping getRDMapping() {
        return new Mapping(rdPort, rdPin, "RD");
    }

    Mapping getWRMapping() {
        return new Mapping(wrPort, wrPin, "WR");
    }

    Mapping getINTRMapping() {
        return new Mapping(intrPort, intrPin, "INTR");
    }

    Mapping getCSMapping() {
        return new Mapping(csPort, csPin, "CS");
    }

    ADCGraphics getGraphics() {
        return adcGraphics;
    }

    void cycle(int time) { // time is in microseconds

        cs = (board.readPortPin(csPort, csPin) == 1);

        if (cs) {
            foredReset();
            return;
        }

        if (converting) {
            if (conversionState <= CONVERSION_TIME) {
                conversionState = conversionState + time;
            }
            if (conversionState >= CONVERSION_TIME) {
                conversionState = CONVERSION_TIME;
                intr = false;
                board.clearPortPin(intrPort, intrPin);
                converting = false;
            }

            // simulating a successive approximation conversion
            int numberOfBitsConverted = (8 * conversionState) / CONVERSION_TIME;
            for (int i = 7; i >= (8 - numberOfBitsConverted); i--) {
                internalBits[i] = ((analogueInput >> i) & 1) == 1;
            }

            if (!converting) {
                conversionState = 0;
            }
        }

        rd = (board.readPortPin(rdPort, rdPin) == 1);
        if (rd) {
            for (int i = 0; i < 8; i++) {
                board.setPortPin(getDataLinePortNumber(i), getDataLinePinNumber(i));
            }
        } else {
            for (int i = 0; i < 8; i++) {
                if (internalBits[i]) {
                    board.setPortPin(getDataLinePortNumber(i), getDataLinePinNumber(i));
                } else {
                    board.clearPortPin(getDataLinePortNumber(i), getDataLinePinNumber(i));
                }
            }
        }

        wr = (board.readPortPin(wrPort, wrPin) == 1);
        if (wr && !previousWr) {
            startConversion();
        }
        previousWr = wr;

    }

    void refreshGraphics() {
        adcGraphics.setInternalBitField(internalBits, !rd);
    }

    void foredReset() {
        cs = (board.readPortLatch(csPort, csPin) == 1);
        rd = (board.readPortLatch(rdPort, rdPin) == 1);
        wr = (board.readPortLatch(wrPort, wrPin) == 1);
        if (!intr) {
            board.setPortPin(intrPort, intrPin);
        }
        previousWr = true;
        reset();
        refreshGraphics();
    }

    private void startConversion() {
        reset();
        intr = true;
        board.setPortPin(intrPort, intrPin);
        analogueInput = adcGraphics.getVoltage();
        converting = true;
    }
    
    private void reset() {
        conversionState = 0;
        converting = false;
        for (int i = 0; i < 8; i++) {
            internalBits[i] = true;
        }
    }

    private void initDataLineMappings() {
        for (int i = 0; i < dataLinesMapping.length; i++) {
            dataLinesMapping[i] = new Mapping(getDataLinePortNumber(i), getDataLinePinNumber(i), "DB" + i);
        }
    }

    private int getDataLinePortNumber(int number) {
        return getPortNumber(data[number]);
    }

    private int getDataLinePinNumber(int pinNumber) {
        return getPinNumber(data[pinNumber]);
    }

    private int getPortNumber(double d) {
        return ((int) d) & 3;
    }

    private int getPinNumber(double d) {
        return ((int) ((d * 10.0) - (getPortNumber(d) * 10))) & 7;
    }
}
