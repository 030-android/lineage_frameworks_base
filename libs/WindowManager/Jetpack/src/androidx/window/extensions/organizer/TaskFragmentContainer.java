/*
 * Copyright (C) 2021 The Android Open Source Project
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

package androidx.window.extensions.organizer;

import android.annotation.NonNull;
import android.annotation.Nullable;
import android.app.Activity;
import android.app.ActivityThread;
import android.os.Binder;
import android.os.IBinder;
import android.window.TaskFragmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-side container for a stack of activities. Corresponds to an instance of TaskFragment
 * on the server side.
 */
class TaskFragmentContainer {
    /**
     * Client-created token that uniquely identifies the task fragment container instance.
     */
    @NonNull
    private final IBinder mToken;

    /**
     * Server-provided task fragment information.
     */
    private TaskFragmentInfo mInfo;

    /**
     * Activity that is being reparented to this container, but haven't been added to {@link #mInfo}
     * yet.
     */
    private Activity mReparentingActivity;

    /** Containers that are dependent on this one and should be completely destroyed on exit. */
    private final List<TaskFragmentContainer> mContainersToFinishOnExit =
            new ArrayList<>();

    /** Individual associated activities in different containers that should be finished on exit. */
    private final List<Activity> mActivitiesToFinishOnExit = new ArrayList<>();

    /** Indicates whether the container was cleaned up after the last activity was removed. */
    private boolean mIsFinished;

    /**
     * Creates a container with an existing activity that will be re-parented to it in a window
     * container transaction.
     */
    TaskFragmentContainer(@Nullable Activity activity) {
        mToken = new Binder("TaskFragmentContainer");
        mReparentingActivity = activity;
    }

    /**
     * Returns the client-created token that uniquely identifies this container.
     */
    @NonNull
    IBinder getTaskFragmentToken() {
        return mToken;
    }

    /** List of activities that belong to this container and live in this process. */
    @NonNull
    List<Activity> collectActivities() {
        // Add the re-parenting activity, in case the server has not yet reported the task
        // fragment info update with it placed in this container. We still want to apply rules
        // in this intermediate state.
        List<Activity> allActivities = new ArrayList<>();
        if (mReparentingActivity != null) {
            allActivities.add(mReparentingActivity);
        }
        // Add activities reported from the server.
        if (mInfo == null) {
            return allActivities;
        }
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        for (IBinder token : mInfo.getActivities()) {
            Activity activity = activityThread.getActivity(token);
            if (activity != null && !allActivities.contains(activity)) {
                allActivities.add(activity);
            }
        }
        return allActivities;
    }

    boolean hasActivity(@NonNull IBinder token) {
        if (mInfo != null && mInfo.getActivities().contains(token)) {
            return true;
        }
        return mReparentingActivity != null
                && mReparentingActivity.getActivityToken().equals(token);
    }

    @Nullable
    TaskFragmentInfo getInfo() {
        return mInfo;
    }

    void setInfo(@Nullable TaskFragmentInfo info) {
        mInfo = info;
        if (mInfo == null || mReparentingActivity == null) {
            return;
        }
        // Cleanup activities that were being re-parented
        for (IBinder activityToken : mInfo.getActivities()) {
            if (mReparentingActivity.getActivityToken().equals(activityToken)) {
                mReparentingActivity = null;
                break;
            }
        }
    }

    @Nullable
    Activity getTopNonFinishingActivity() {
        List<Activity> activities = collectActivities();
        if (activities.isEmpty()) {
            return null;
        }
        int i = activities.size() - 1;
        while (i >= 0 && activities.get(i).isFinishing()) {
            i--;
        }
        return i >= 0 ? activities.get(i) : null;
    }

    boolean isEmpty() {
        return mReparentingActivity == null && (mInfo == null || mInfo.isEmpty());
    }

    /**
     * Adds a container that should be finished when this container is finished.
     */
    void addContainerToFinishOnExit(@NonNull TaskFragmentContainer containerToFinish) {
        mContainersToFinishOnExit.add(containerToFinish);
    }

    /**
     * Adds an activity that should be finished when this container is finished.
     */
    void addActivityToFinishOnExit(@NonNull Activity activityToFinish) {
        mActivitiesToFinishOnExit.add(activityToFinish);
    }

    /**
     * Removes all activities that belong to this process and finishes other containers/activities
     * configured to finish together.
     */
    void finish(boolean shouldFinishDependent) {
        if (mIsFinished) {
            return;
        }
        mIsFinished = true;

        // Finish own activities
        for (Activity activity : collectActivities()) {
            activity.finish();
        }

        if (!shouldFinishDependent) {
            return;
        }

        // Finish dependent containers
        for (TaskFragmentContainer container : mContainersToFinishOnExit) {
            container.finish(true /* shouldFinishDependent */);
        }
        mContainersToFinishOnExit.clear();

        // Finish associated activities
        for (Activity activity : mActivitiesToFinishOnExit) {
            activity.finish();
        }
        mActivitiesToFinishOnExit.clear();

        // Finish activities that were being re-parented to this container.
        if (mReparentingActivity != null) {
            mReparentingActivity.finish();
            mReparentingActivity = null;
        }
    }

    boolean isFinished() {
        return mIsFinished;
    }
}
