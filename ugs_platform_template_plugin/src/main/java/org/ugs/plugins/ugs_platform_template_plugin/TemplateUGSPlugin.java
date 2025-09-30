/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ugs.plugins.ugs_platform_template_plugin;

import org.openide.modules.ModuleInstall;
import org.openide.util.lookup.ServiceProvider;
import com.willwinder.universalgcodesender.model.BackendAPI;
import com.willwinder.universalgcodesender.connection.WSConnection;
import com.willwinder.universalgcodesender.connection.JSerialCommConnection;
import com.willwinder.universalgcodesender.connection.JSerialCommConnectionDevice;
/**
 *
 * @author matthew-papesh
 */
@ServiceProvider(service = ModuleInstall.class)
public class TemplateUGSPlugin extends ModuleInstall {

    @Override
    public void restored() {
        // Called when the plugin is loaded
        System.out.println("UGS Plugin loaded!");
        // Here you could register actions, listeners, or commands
    }

    @Override
    public void close() {
        // Called when plugin is unloaded
        System.out.println("UGS Plugin unloaded!");
    }
    
    // Optional helper init method
    private void init_backend() {
        // Get the currently active backend connection
        connection = UGSBackend.getActiveConnection(); // pseudo-code, replace with actual API

        if (connection != null) {
            // Add status listener
            connection.addConnectionListener(new ConnectionListener() {
                @Override
                public void onStatusUpdate(Status status) {
                    System.out.println("Status: " + status);
                }

                @Override
                public void onMessageReceived(String msg) {
                    System.out.println("Message: " + msg);
                }
            });
        } else {
            System.out.println("No active connection found");
        }
    }
}

