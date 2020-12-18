/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.dagger;

import com.android.systemui.wmshell.WMShellModule;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellInit;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;

import java.util.Optional;

import dagger.Subcomponent;

/**
 * Dagger Subcomponent for WindowManager.
 */
@WMSingleton
@Subcomponent(modules = {WMShellModule.class})
public interface WMComponent {

    /**
     * Builder for a WMComponent.
     */
    @Subcomponent.Builder
    interface Builder {
        WMComponent build();
    }

    /**
     * Initializes all the WMShell components before starting any of the SystemUI components.
     */
    default void init() {
        getShellInit().init();
    }

    // Gets the Shell init instance
    @WMSingleton
    ShellInit getShellInit();

    // Gets the Shell dump instance
    @WMSingleton
    Optional<ShellCommandHandler> getShellCommandHandler();

    // TODO(b/162923491): We currently pass the instances through to SysUI, but that may change
    //                    depending on the threading mechanism we go with
    @WMSingleton
    Optional<OneHanded> getOneHanded();

    @WMSingleton
    Optional<Pip> getPip();

    @WMSingleton
    Optional<LegacySplitScreen> getLegacySplitScreen();

    @WMSingleton
    Optional<AppPairs> getAppPairs();

    @WMSingleton
    Optional<Bubbles> getBubbles();

    @WMSingleton
    Optional<HideDisplayCutout> getHideDisplayCutout();

    @WMSingleton
    Optional<TaskViewFactory> getTaskViewFactory();
}
