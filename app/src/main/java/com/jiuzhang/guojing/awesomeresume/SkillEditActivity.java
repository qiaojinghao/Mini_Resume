package com.jiuzhang.guojing.awesomeresume;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jiuzhang.guojing.awesomeresume.model.Skill;

import java.util.Arrays;

public class SkillEditActivity extends EditBaseActivity<Skill> {
    public static final String KEY_SKILL = "skill";
    public static final String KEY_SKILL_ID = "skill_id";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_skill_edit;
    }

    @Override
    protected void setupUIForCreate() {
        findViewById(R.id.skill_edit_delete).setVisibility(View.GONE);
    }

    @Override
    protected void setupUIForEdit(@NonNull final Skill data) {
        ((EditText) findViewById(R.id.skill_edit_type))
                .setText(data.type);
        ((EditText) findViewById(R.id.skill_edit_name))
                .setText(TextUtils.join("\n", data.names));
        findViewById(R.id.skill_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(KEY_SKILL_ID, data.id);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void saveAndExit(@Nullable Skill data) {
        if (data == null) {
            data = new Skill();
        }

        data.type = ((EditText) findViewById(R.id.skill_edit_type)).getText().toString();
        data.names = Arrays.asList(TextUtils.split(((EditText) findViewById(R.id.skill_edit_name)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_SKILL, data);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    @Override
    protected Skill initializeData() {
        return getIntent().getParcelableExtra(KEY_SKILL);
    }
}
