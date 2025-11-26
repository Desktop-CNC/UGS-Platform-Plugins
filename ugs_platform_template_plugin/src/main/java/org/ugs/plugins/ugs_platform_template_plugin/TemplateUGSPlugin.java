/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ugs.plugins.ugs_platform_template_plugin;

import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

import com.willwinder.ugs.nbp.lib.lookup.CentralLookup;
import com.willwinder.universalgcodesender.model.BackendAPI;
import com.willwinder.universalgcodesender.model.events.ControllerStatusEvent;
import com.willwinder.universalgcodesender.model.UGSEvent;
import com.willwinder.universalgcodesender.listeners.ControllerStatus;
import com.willwinder.universalgcodesender.listeners.MessageType;
import com.willwinder.universalgcodesender.listeners.UGSEventListener;

public class TemplateUGSPlugin extends ModuleInstall implements UGSEventListener {

    // These are the UGS backend objects for interacting with the backend.
    private final BackendAPI backend_api;
    
    public TemplateUGSPlugin() {
        // This is how to access the UGS backend and register the listener.
        // CentralLookup is used to get singleton instances of the UGS
        // Settings and BackendAPI objects.
        backend_api = CentralLookup.getDefault().lookup(BackendAPI.class);
        backend_api.addUGSEventListener(this);        
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
        backend_api.dispatchMessage(MessageType.INFO, "UGS Event Change to: " + event.toString());
    }
}
