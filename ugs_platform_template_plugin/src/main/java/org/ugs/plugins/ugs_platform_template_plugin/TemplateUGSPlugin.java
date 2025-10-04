/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ugs.plugins.ugs_platform_template_plugin;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import com.willwinder.universalgcodesender.model.BackendAPI;

public class TemplateUGSPlugin extends ModuleInstall {

    @Override
    public void restored() {
        // When UGS starts, you can grab the BackendAPI like this:
        BackendAPI backend = Lookup.getDefault().lookup(BackendAPI.class);
        try {
            if (backend != null) {
                // Example: queue a move command
                backend.sendGcodeCommand("G0 X10 Y10");
            } else {
                System.err.println("BackendAPI not available.");
            }
        } catch(Exception e) {
            System.err.println("UGS Plugin Error: " + e.getMessage());
        }
    }
}
