/*
 * Copyright (C) 2025 camren-chraplak
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cchraplak.ugs.platform.plugin.logger;

import com.willwinder.ugs.nbp.lib.lookup.CentralLookup;
import com.willwinder.universalgcodesender.i18n.Localization;
import com.willwinder.universalgcodesender.listeners.MessageListener;
import com.willwinder.universalgcodesender.listeners.MessageType;
import com.willwinder.universalgcodesender.listeners.UGSEventListener;
import com.willwinder.universalgcodesender.model.BackendAPI;
import com.willwinder.universalgcodesender.model.UGSEvent;
import com.willwinder.universalgcodesender.model.events.ControllerStateEvent;
import com.willwinder.universalgcodesender.utils.Settings;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileSystemView;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.cchraplak.ugs.platform.plugin.logger//Logger//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "LoggerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "com.cchraplak.ugs.platform.plugin.logger.LoggerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_LoggerAction",
        preferredID = "LoggerTopComponent"
)
@Messages({
    "CTL_LoggerAction=Machine Logger",
    "CTL_LoggerTopComponent=Machine Logger",
    "HINT_LoggerTopComponent=This is a Logger window"
})
public final class LoggerTopComponent extends TopComponent implements MessageListener, UGSEventListener {
    
    // UGS overide values
    public static final String ACTION_ID = "com.cchraplak.ugs.platform.plugin.logger.LoggerTopComponent";
    private final JLabel status = new JLabel();
    private final transient BackendAPI backend;
    private final Settings settings;
    
    //@Override;
    public void UGSEvent(UGSEvent event) {
        if (event instanceof ControllerStateEvent controllerStateEvent) {
            status.setText(controllerStateEvent.getState().name());
        }
    }
    
    private String directoryString = FileSystemView.getFileSystemView().getHomeDirectory().toString();
    private final String dirFile = "root_directory.csv";
    
    private FileWriter positionWriter = null;
    private FileWriter gcodeWriter = null;
    private float cwo[] = {0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private int numAxis = 0;
    
    @Override
    public void onMessage(MessageType messageType, String message) {
        //System.out.println(message);
        
        String time = LocalTime.now().toString();
        time = time.substring(0, time.length() - 6);
        
        try {
            if (numAxis == 0) {
                if (message.contains("<")) {
                    String inputs[] = message.split("\\|");
                    for (int i = 0; i < inputs.length; i++) {
                        String input = inputs[i];
                        if (input.contains("MPos:")) {
                            input = input.replace("MPos:", "");
                            String filtered[] = input.split(",");
                            numAxis = filtered.length;
                            i = inputs.length;
                        }
                    }
                }
            }
            else if (message.contains("Connection closed")) {
                stopRecording();
                numAxis = 0;
            }
            else if (message.contains(">>> ")) {
                String input = message.replace(">>> ", "");
                input = input.replace("\n", "");
                if (input.length() > 0) {
                    gcodeWriter.write(time + "," + input + ",\n");
                }
            }
            else if (message.contains("<")) {
                String inputs[] = message.split("\\|");
                
                String outputs[] = new String[2*numAxis + 3];
                for (int i = 0; i < inputs.length; i++) {
                    String input = inputs[i];
                    if (input.contains("MPos:")) {
                        input = input.replace("MPos:", "");
                        String filtered[] = input.split(",");
                        for (int j = 0; j < filtered.length; j++) {
                            float value = Float.parseFloat(filtered[j]);
                            outputs[j] = filtered[j];
                            outputs[j + numAxis] = Float.toString(value - cwo[j]);
                        }
                    }
                    else if (input.contains("Ln:")) {
                        input = input.replace("Ln:", "");
                        outputs[2*numAxis + 2] = input;
                    }
                    else if (input.contains("FS:")) {
                        input = input.replace("FS:", "");
                        String filtered[] = input.split(",");
                        for (int j = 0; j < filtered.length; j++) {
                            outputs[j + 2*numAxis] = filtered[j];
                        }
                    }
                    else if (input.contains("WCO:")) {
                        input = input.replace("WCO:", "");
                        String filtered[] = input.split(",");
                        for (int j = 0; j < filtered.length; j++) {
                            float value = Float.parseFloat(filtered[j]);
                            cwo[j] = value;
                        }
                    }
                }
                
                String resultString = time + ",";
                for (int i = 0; i < outputs.length; i++) {
                    if (outputs[i] == null) {
                        outputs[i] = "";
                    }
                    resultString += outputs[i] + ",";
                }
                resultString += "\n";
                positionWriter.write(resultString);
            }
        }
        catch (Exception e) {
            
        }
        
        
    }

    public LoggerTopComponent() {
        initComponents();
        setName(Bundle.CTL_LoggerTopComponent());
        setToolTipText(Bundle.HINT_LoggerTopComponent());

        // UGS backend
        settings = CentralLookup.getDefault().lookup(Settings.class);
        backend = CentralLookup.getDefault().lookup(BackendAPI.class);
        backend.addUGSEventListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        startRecord = new javax.swing.JButton();
        endRecord = new javax.swing.JButton();
        selDir = new javax.swing.JButton();
        recordStatus = new javax.swing.JLabel();
        dirLoaded = new javax.swing.JLabel();
        openDir = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(startRecord, org.openide.util.NbBundle.getMessage(LoggerTopComponent.class, "LoggerTopComponent.startRecord.text")); // NOI18N
        startRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startRecordActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(endRecord, org.openide.util.NbBundle.getMessage(LoggerTopComponent.class, "LoggerTopComponent.endRecord.text")); // NOI18N
        endRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endRecordActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(selDir, org.openide.util.NbBundle.getMessage(LoggerTopComponent.class, "LoggerTopComponent.selDir.text")); // NOI18N
        selDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selDirActionPerformed(evt);
            }
        });

        recordStatus.setForeground(new java.awt.Color(255, 0, 0));
        recordStatus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(recordStatus, org.openide.util.NbBundle.getMessage(LoggerTopComponent.class, "LoggerTopComponent.recordStatus.text")); // NOI18N
        recordStatus.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        dirLoaded.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(dirLoaded, org.openide.util.NbBundle.getMessage(LoggerTopComponent.class, "LoggerTopComponent.dirLoaded.text")); // NOI18N
        dirLoaded.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        org.openide.awt.Mnemonics.setLocalizedText(openDir, org.openide.util.NbBundle.getMessage(LoggerTopComponent.class, "LoggerTopComponent.openDir.text")); // NOI18N
        openDir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openDirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startRecord)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endRecord))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(selDir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(openDir)))
                        .addGap(0, 111, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(dirLoaded, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(recordStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startRecord)
                    .addComponent(endRecord))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recordStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selDir)
                    .addComponent(openDir))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dirLoaded, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void startRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startRecordActionPerformed
        // TODO add your handling code here:
        
        String time = LocalTime.now().toString();
        time = time.substring(0, time.length() - 6);
        time = time.replace(":", ".");
        
        try {
            if (positionWriter == null && gcodeWriter == null && numAxis != 0) {
                positionWriter = new FileWriter(directoryString + "/position_" + time + ".csv");
                gcodeWriter = new FileWriter(directoryString + "/gcode_" + time + ".csv");
                
                String positionHeader = "Time,";
                String axis[] = {"X", "Y", "Z", "A", "B", "C"};
                for (int i = 0; i < numAxis; i++) {
                    positionHeader += "Machine " + axis[i] + ",";
                }
                for (int i = 0; i < numAxis; i++) {
                    positionHeader += "Work " + axis[i] + ",";
                }
                positionHeader += "Feed Rate,Spindle Speed,Line Number,\n";
                positionWriter.write(positionHeader);
                gcodeWriter.write("Time,Command,\n");
                
                recordStatus.setText("Recording");
                recordStatus.setForeground(Color.green);
            }
            else {
                System.out.println("Files already recording!");
            }
        }
        catch (Exception e) {
            System.out.println("Error starting files!");
            recordStatus.setText("ERROR");
            recordStatus.setForeground(Color.orange);
        }
    }//GEN-LAST:event_startRecordActionPerformed

    private void selDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selDirActionPerformed

        //String directory = "/media/Files/WPI/CS_4241/a4-Chraplak/a4-chraplak/node_modules/@eslint/config-array/dist/cjs/std__path";

        JFileChooser fileChooser = new JFileChooser(directoryString);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int chooseCode = fileChooser.showSaveDialog(null);
        if (chooseCode == JFileChooser.APPROVE_OPTION) {
            directoryString = fileChooser.getSelectedFile().getAbsolutePath();
            writeRoot();
            dirLoaded.setText(outDir());
        }
        else {
            
        }
    }//GEN-LAST:event_selDirActionPerformed

    
    private void stopRecording() {
        try {
            if (positionWriter != null && gcodeWriter != null) {
                positionWriter.close();
                gcodeWriter.close();
                positionWriter = null;
                gcodeWriter = null;
                recordStatus.setText("Not Recording");
                recordStatus.setForeground(Color.red);
            }
            else {
                System.out.println("Files not started!");
            }
        }
        catch (Exception e) {
            System.out.println("Error ending files!");
            recordStatus.setText("ERROR");
            recordStatus.setForeground(Color.orange);
        }
    }
    
    private void endRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endRecordActionPerformed
        // TODO add your handling code here:
        stopRecording();
    }//GEN-LAST:event_endRecordActionPerformed

    private void openDirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openDirActionPerformed
        // TODO add your handling code here:
        try {
            Desktop.getDesktop().open(new File(directoryString));
        }
        catch (Exception e) {
            System.out.println("Couldn't open directory!");
        }
    }//GEN-LAST:event_openDirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dirLoaded;
    private javax.swing.JButton endRecord;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JButton openDir;
    private javax.swing.JLabel recordStatus;
    private javax.swing.JButton selDir;
    private javax.swing.JButton startRecord;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
        
        // UGS overides
        super.componentOpened();
        setName(Localization.getString("Logger"));
        setToolTipText(Localization.getString("logger"));
        backend.addUGSEventListener(this);
        backend.addMessageListener(this);
        
        try {
        
            File saveDir = new File(dirFile);

            if (saveDir.exists()) {
                Scanner saveScanner = new Scanner(saveDir);

                while (saveScanner.hasNextLine()) {
                    String data = saveScanner.nextLine();
                    directoryString = data;
                    System.out.println(data);
                }
                saveScanner.close();
            }
            else {
                writeRoot();
            }
        }
        catch (IOException e) {
            System.out.println("=====================================================");
            System.out.println("ERROR NO FILE FOUND!!!");
            System.out.println("=====================================================");
            e.printStackTrace();
        }
        
        dirLoaded.setText(outDir());
    }

    @Override
    public void componentClosed() {
        // UGS overrides
        super.componentClosed();
        backend.removeUGSEventListener(this);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
    private String outDir() {
        return "<html>Path: " + directoryString.replace("/", " / ") + "</html>";
    }
    
    private void writeRoot() {
        try {
            FileWriter rootWriter = new FileWriter(dirFile);
            rootWriter.write(directoryString);
            rootWriter.close();
        }
        catch (IOException e) {
            System.out.println("ERROR WRITING ROOT DIRECTORY!!!");
            e.printStackTrace();
        }
    }
}
