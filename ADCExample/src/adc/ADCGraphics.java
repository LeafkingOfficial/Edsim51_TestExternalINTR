package adc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author www.edsim51.com
 */
class ADCGraphics extends JPanel implements ActionListener, ChangeListener {

    private JSlider slider = new JSlider();
    private JTextField inputField = new JTextField();
    private JTextField internalBitField = new JTextField();
    private JLabel adcLabel = new JLabel("ADC");
    private JLabel inputLabel = new JLabel("input");
    private Dimension smallSliderDimension = new Dimension(20, 100);
    private Dimension largeSliderDimension = new Dimension(30, 150);
    private Dimension smallInputFieldDimension = new Dimension(60, 20);
    private Dimension largeInputFieldDimension = new Dimension(90, 30);
    private Dimension smallInternalBitFieldDimension = new Dimension(80, 20);
    private Dimension largeInternaldBitFieldDimension = new Dimension(120, 30);
    private float voltage = 0.0f;

    ADCGraphics() {

        this.setBackground(Color.WHITE);
        this.setLayout(new GridBagLayout());

        Insets insets = new Insets(5, 5, 5, 5);

        GridBagConstraints gbc = new GridBagConstraints();

        slider.setOrientation(SwingConstants.VERTICAL);
        slider.setMinimum(0);
        slider.setMaximum(255);
        slider.setValue(0);
        slider.addChangeListener(this);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = insets;
        this.add(slider, gbc);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridBagLayout());

        inputField.setText("0.0 V");
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(inputField, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(inputLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = insets;
        this.add(panel, gbc);

        internalBitField.setText("11111111");
        internalBitField.setForeground(Color.RED);
        internalBitField.setHorizontalAlignment(JTextField.CENTER);
        internalBitField.setEditable(false);
        internalBitField.setToolTipText("internal register");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = insets;
        this.add(internalBitField, gbc);

        adcLabel.setToolTipText("analogue to digital converter");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = insets;
        this.add(adcLabel, gbc);

    }

    int getVoltage() {
        return slider.getValue();
    }

    void setInternalBitField(boolean[] bits, boolean passedToOutput) {
        String s = "";
        for (int i = 0; i < 8; i++) {
            if (bits[i]) {
                s = "1" + s;
            } else {
                s = "0" + s;
            }
        }
        if (passedToOutput) {
            internalBitField.setForeground(Color.BLACK);
            internalBitField.setToolTipText("internal register (on output pins)");
        } else {
            internalBitField.setForeground(Color.RED);
            internalBitField.setToolTipText("internal register");
        }
        internalBitField.setText(s);
    }

    void setSize(boolean small) {
        Font font = inputField.getFont();;
        if (small) {
            slider.setPreferredSize(smallSliderDimension);
            slider.setMaximumSize(smallSliderDimension);
            slider.setMinimumSize(smallSliderDimension);
            inputField.setPreferredSize(smallInputFieldDimension);
            inputField.setMaximumSize(smallInputFieldDimension);
            inputField.setMinimumSize(smallInputFieldDimension);
            internalBitField.setPreferredSize(smallInternalBitFieldDimension);
            internalBitField.setMaximumSize(smallInternalBitFieldDimension);
            internalBitField.setMinimumSize(smallInternalBitFieldDimension);
            font = new Font(font.getName(), font.PLAIN, 12);
        } else {
            slider.setPreferredSize(largeSliderDimension);
            slider.setMaximumSize(largeSliderDimension);
            slider.setMinimumSize(largeSliderDimension);
            inputField.setPreferredSize(largeInputFieldDimension);
            inputField.setMaximumSize(largeInputFieldDimension);
            inputField.setMinimumSize(largeInputFieldDimension);
            internalBitField.setPreferredSize(largeInternaldBitFieldDimension);
            internalBitField.setMaximumSize(largeInternaldBitFieldDimension);
            internalBitField.setMinimumSize(largeInternaldBitFieldDimension);
            font = new Font(font.getName(), font.PLAIN, 18);
        }
        inputField.setFont(font);
        internalBitField.setFont(font);
        font = new Font(font.getName(), Font.BOLD, font.getSize());
        adcLabel.setFont(font);
        inputLabel.setFont(font);
    }

    private String removeVoltageSymbol() {
        String s = inputField.getText();
        int i = s.indexOf('V');
        if (i < 0) {
            return s;
        }
        return s.substring(0, i).trim();
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == inputField) {
            try {
                voltage = Float.parseFloat(removeVoltageSymbol());
                if (voltage < 0.0) {
                    voltage = 0.0f;
                } else if (voltage > 5.0) {
                    voltage = 5.0f;
                }
            } catch (NumberFormatException ex) {
            }
            slider.setValue(Math.round((voltage * 255.0f) / 5.0f));
            inputField.setText(voltage + " V");
        }
    }

    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == slider) {
            float f = (((float) slider.getValue()) * 5.0f) / 255.0f;
            int i = Math.round(f * 100.0f);
            voltage = ((float) i) / 100.0f;
            inputField.setText(voltage + " V");
        }
    }
}
