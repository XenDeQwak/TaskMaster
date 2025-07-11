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
        this.questData = questData;
    }

    public QuestData getQuestData() {
        return questData;
    }

    public void setQuestData(QuestData questData) {
        this.questData = questData;
    }

    public QuestBox getQuestBox() {
        return questBox;
    }

    public void setQuestBox(QuestBox questBox) {
        this.questBox = questBox;
    }

    public QuestBoxPreview getQuestBoxPreview() {
        return questBoxPreview;
    }

    public void setQuestBoxPreview(QuestBoxPreview questBoxPreview) {
        this.questBoxPreview = questBoxPreview;
    }


}
