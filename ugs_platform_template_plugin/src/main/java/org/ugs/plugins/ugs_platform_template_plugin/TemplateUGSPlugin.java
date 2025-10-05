/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ugs.plugins.ugs_platform_template_plugin;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

import com.willwinder.universalgcodesender.model.BackendAPI;
import com.willwinder.universalgcodesender.model.events.ControllerStatusEvent;
import com.willwinder.universalgcodesender.model.UGSEvent;
import com.willwinder.universalgcodesender.listeners.ControllerStatus;
import com.willwinder.universalgcodesender.listeners.UGSEventListener;

public class TemplateUGSPlugin extends ModuleInstall implements UGSEventListener {

    // UGS Platform API Java object 
    protected BackendAPI backend = null;
    
    /**
     * Handler called once to initialize the plugin upon loading or starting. 
     */
    @Override
    public void restored() {
        // get the backend upon start-up
        backend = Lookup.getDefault().lookup(BackendAPI.class);
        backend.addUGSEventListener(this); // attach this plugin to listen to UGS
    }
    
    /**
     * Handler called once upon unloading or ending the running plugin.
     */
    @Override
    public void close() {
        System.out.print("TemplateUGSPlugin closed.");
    }
    
    /**
     * Handler called once everytime a significant event happens in UGS. There
     * are many event types. Look them up online or in UGS documentation. 
     * @param event The event from UGS Platform
     */
    @Override 
    public void UGSEvent(UGSEvent event) {
        /**
         * Events can be tested to see what type. For ex:
         * `event instanceof com.willwinder.universalgcodesender.model.events.GCodeSentEvent`
         * is True if `event` is the type to send a gcode command. 
         * 
         * Popular UGS Events Are:
         * - GCodeSentEvent           : Represents sending GCode command to CNC 
         * - ConsoleMessageEvent      : Represents UGS System messages / custom messages
         * - ControllerStatusEvent    : Represents periodic CNC status updates
         * 
         * Handling by event type allows plugins to do stuff based on the type of event seen.
         * Based on the type of event, we can send different commands to the CNC, some can be custom commands.
         */
        System.out.print("UGS Event Change to: " + event.toString());
    }
}
