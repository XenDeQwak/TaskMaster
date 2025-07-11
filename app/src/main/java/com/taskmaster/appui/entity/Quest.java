package com.taskmaster.appui.entity;

import android.content.Context;

import com.taskmaster.appui.data.QuestData;
import com.taskmaster.appui.view.uimodule.QuestBox;
import com.taskmaster.appui.view.uimodule.QuestBoxPreview;

public class Quest {

    private QuestData questData;
    private QuestBox questBox;
    private QuestBoxPreview questBoxPreview;

    public Quest (QuestData questData) {
        this.questBox = questData;
    }

}
