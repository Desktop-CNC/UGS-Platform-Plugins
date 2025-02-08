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
package com.cchraplak.ugs.platform.plugin.toolchanger;

import com.willwinder.ugs.nbp.lib.Mode;
import com.willwinder.ugs.nbp.lib.lookup.CentralLookup;
import com.willwinder.ugs.nbp.lib.services.LocalizingService;
import com.willwinder.universalgcodesender.i18n.Localization;
import com.willwinder.universalgcodesender.listeners.UGSEventListener;
import com.willwinder.universalgcodesender.model.BackendAPI;
import com.willwinder.universalgcodesender.model.UGSEvent;
import com.willwinder.universalgcodesender.model.events.ControllerStateEvent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;

@TopComponent.Description(
        preferredID = "MyModuleTopComponent"
)
@TopComponent.Registration(
        mode = Mode.LEFT_BOTTOM,
        openAtStartup = false)
@ActionID(
        category = LocalizingService.CATEGORY_WINDOW,
        id = testClass.ACTION_ID)
@ActionReference(
        path = LocalizingService.MENU_WINDOW_PLUGIN)
@TopComponent.OpenActionRegistration(
        displayName = "My module",
        preferredID = "MyModuleTopComponent"
)

/**
 *
 * @author camren-chraplak
 */
public final class testClass extends TopComponent implements UGSEventListener {
    public static final String ACTION_ID = "com.willwinder.ugs.nbp.mymodule.MyModuleTopComponent";
    private final transient BackendAPI backend;

    private final JLabel status = new JLabel();

    public testClass() {
        backend = CentralLookup.getDefault().lookup(BackendAPI.class);
    }

    @Override
    protected void componentClosed() {
        super.componentClosed();
        backend.removeUGSEventListener(this);
    }

    @Override
    protected void componentOpened() {
        super.componentOpened();
        initComponents();

        setName(Localization.getString("platform.plugin.mymodule.name"));
        setToolTipText(Localization.getString("platform.plugin.mymodule.tooltip"));
        backend.addUGSEventListener(this);
    }

    private void initComponents() {
        removeAll();
        setLayout(new BorderLayout());

        status.setHorizontalAlignment(SwingConstants.CENTER);
        status.setText(backend.getControllerState().name());
        add(status, BorderLayout.CENTER);
    }

    @Override
    public void UGSEvent(UGSEvent event) {
        if (event instanceof ControllerStateEvent controllerStateEvent) {
            status.setText(controllerStateEvent.getState().name());
        }
    }
}
