package com.jiuzhang.guojing.awesomeresume;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.jiuzhang.guojing.awesomeresume.model.BasicInfo;
import com.jiuzhang.guojing.awesomeresume.model.Education;
import com.jiuzhang.guojing.awesomeresume.model.Experience;
import com.jiuzhang.guojing.awesomeresume.model.Project;
import com.jiuzhang.guojing.awesomeresume.model.Skill;
import com.jiuzhang.guojing.awesomeresume.util.BirthUtils;
import com.jiuzhang.guojing.awesomeresume.util.DateUtils;
import com.jiuzhang.guojing.awesomeresume.util.ImageUtils;
import com.jiuzhang.guojing.awesomeresume.util.ModelUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_EDIT_EDUCATION = 100;
    private static final int REQ_CODE_EDIT_EXPERIENCE = 101;
    private static final int REQ_CODE_EDIT_PROJECT = 102;
    private static final int REQ_CODE_EDIT_BASIC_INFO = 103;
    private static final int REQ_CODE_EDIT_SKILL = 104;

    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";
    private static final String MODEL_BASIC_INFO = "basic_info";
    private static final String MODEL_SKILLS = "skills";

    private BasicInfo basicInfo;
    private List<Education> educations;
    private List<Experience> experiences;
    private List<Project> projects;
    private List<Skill> skills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        setupUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_EDIT_BASIC_INFO:
                    BasicInfo basicInfo = data.getParcelableExtra(BasicInfoEditActivity.KEY_BASIC_INFO);
                    updateBasicInfo(basicInfo);
                    break;
                case REQ_CODE_EDIT_EDUCATION:
                    String educationId = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);
                    if (educationId != null) {
                        deleteEducation(educationId);
                    } else {
                        Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
                        updateEducation(education);
                    }
                    break;
                case REQ_CODE_EDIT_EXPERIENCE:
                    String experienceId = data.getStringExtra(ExperienceEditActivity.KEY_EXPERIENCE_ID);
                    if (experienceId != null) {
                        deleteExperience(experienceId);
                    } else {
                        Experience experience = data.getParcelableExtra(ExperienceEditActivity.KEY_EXPERIENCE);
                        updateExperience(experience);
                    }
                    break;
                case REQ_CODE_EDIT_PROJECT:
                    String projectId = data.getStringExtra(ProjectEditActivity.KEY_PROJECT_ID);
                    if (projectId != null) {
                        deleteProject(projectId);
                    } else {
                        Project project = data.getParcelableExtra(ProjectEditActivity.KEY_PROJECT);
                        updateProject(project);
                    }
                    break;
                case REQ_CODE_EDIT_SKILL:
                    String skillID = data.getStringExtra(SkillEditActivity.KEY_SKILL_ID);
                    if(skillID != null){
                        deleteSkill(skillID);
                    }else {
                        Skill skill = data.getParcelableExtra(SkillEditActivity.KEY_SKILL);
                        updateSkill(skill);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        ImageButton addEducationBtn = (ImageButton) findViewById(R.id.add_education_btn);
        addEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });

        ImageButton addExperienceBtn = (ImageButton) findViewById(R.id.add_experience_btn);
        addExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });

        ImageButton addProjectBtn = (ImageButton) findViewById(R.id.add_project_btn);
        addProjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });
        ImageButton addSkillBtn = (ImageButton) findViewById(R.id.add_skill_btn);
        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SkillEditActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_SKILL);
            }
        });

        setupBasicInfo();
        setupEducations();
        setupExperiences();
        setupProjects();
        setupSkills();
    }

    private void setupBasicInfo() {
        ((TextView) findViewById(R.id.name)).setText(TextUtils.isEmpty(basicInfo.name)
                ? "Your name"
                : basicInfo.name);

        ((TextView) findViewById(R.id.user_birth)).setText(basicInfo.birth == null
                ? "Birth"
                : BirthUtils.birthToString(basicInfo.birth));

        ((TextView) findViewById(R.id.user_phone_number)).setText(TextUtils.isEmpty(basicInfo.phoneNum)
                ? "Phone num"
                : basicInfo.phoneNum);

        ((TextView) findViewById(R.id.email)).setText(TextUtils.isEmpty(basicInfo.email)
                ? "Email"
                : basicInfo.email);

        ImageView userPicture = (ImageView) findViewById(R.id.user_picture);
        if (basicInfo.imageUri != null) {
            ImageUtils.loadImage(this, basicInfo.imageUri, userPicture);
        } else {
            userPicture.setImageResource(R.drawable.user_ghost);
        }

        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasicInfoEditActivity.class);
                intent.putExtra(BasicInfoEditActivity.KEY_BASIC_INFO, basicInfo);
                startActivityForResult(intent, REQ_CODE_EDIT_BASIC_INFO);
            }
        });
    }

    private void setupEducations() {
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
        educationsLayout.removeAllViews();
        for (Education education : educations) {
            View educationView = getLayoutInflater().inflate(R.layout.education_item, null);
            setupEducation(educationView, education);
            educationsLayout.addView(educationView);
        }
    }

    private void setupEducation(View educationView, final Education education) {
        ((TextView)educationView.findViewById(R.id.education_school)).setText(education.school+" ("+
                DateUtils.dateToString(education.startDate)+"-"+DateUtils.dateToString(education.endDate)+")");
        ((TextView)educationView.findViewById(R.id.education_major)).setText(education.major);
        if(!education.gpa.equals(""))
            ((TextView)educationView.findViewById(R.id.education_GPA)).setText("GPA: "+education.gpa);
        ((TextView)educationView.findViewById(R.id.education_courses)).setText(formatItems(education.courses));

        ImageButton editEducationBtn = (ImageButton) educationView.findViewById(R.id.education_edit);
        editEducationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
                startActivityForResult(intent, REQ_CODE_EDIT_EDUCATION);
            }
        });
    }

    private void setupExperiences() {
        LinearLayout experiencesLayout = (LinearLayout) findViewById(R.id.experience_list);
        experiencesLayout.removeAllViews();
        for (Experience experience : experiences) {
            View experienceView = getLayoutInflater().inflate(R.layout.experience_item, null);
            setupExperience(experienceView, experience);
            experiencesLayout.addView(experienceView);
        }
    }

    private void setupExperience(View experienceView, final Experience experience) {
        String dateString;
        if(!experience.startDate.equals(experience.endDate)){
            dateString = DateUtils.dateToString(experience.startDate)
                + " ~ " + DateUtils.dateToString(experience.endDate);
        }else{
            dateString = DateUtils.dateToString(experience.startDate);
        }
        ((TextView) experienceView.findViewById(R.id.experience_company))
                .setText(experience.company + " (" + dateString + ")");
        ((TextView) experienceView.findViewById(R.id.experience_title)).setText(experience.title);
        ((TextView) experienceView.findViewById(R.id.experience_details))
                .setText(formatItems(experience.details));

        ImageButton editExperienceBtn = (ImageButton) experienceView.findViewById(R.id.edit_experience_btn);
        editExperienceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                intent.putExtra(ExperienceEditActivity.KEY_EXPERIENCE, experience);
                startActivityForResult(intent, REQ_CODE_EDIT_EXPERIENCE);
            }
        });
    }

    private void setupProjects() {
        LinearLayout projectListLayout = (LinearLayout) findViewById(R.id.project_list);
        projectListLayout.removeAllViews();
        for (Project project : projects) {
            View projectView = getLayoutInflater().inflate(R.layout.project_item, null);
            setupProject(projectView, project);
            projectListLayout.addView(projectView);
        }
    }

    private void setupProject(@NonNull View projectView, final Project project) {
        ((TextView) projectView.findViewById(R.id.project_name))
                .setText(project.name);
        ((TextView) projectView.findViewById(R.id.project_details))
                .setText(formatItems(project.details));
        projectView.findViewById(R.id.edit_project_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                intent.putExtra(ProjectEditActivity.KEY_PROJECT, project);
                startActivityForResult(intent, REQ_CODE_EDIT_PROJECT);
            }
        });
    }

    private void setupSkills(){
        LinearLayout skillListLayout = (LinearLayout) findViewById(R.id.skills_list);
        skillListLayout.removeAllViews();
        for (Skill skill : skills) {
            View skillView = getLayoutInflater().inflate(R.layout.skill_item, null);
            setupSkill(skillView, skill);
            skillListLayout.addView(skillView);
        }
    }

    private void setupSkill(@NonNull View skillView, final Skill skill) {
        ((TextView) skillView.findViewById(R.id.skill_type))
                .setText(skill.type+":");
        ((TextView) skillView.findViewById(R.id.skill_name))
                .setText(formatSkill(skill.names));
        skillView.findViewById(R.id.edit_skill_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SkillEditActivity.class);
                intent.putExtra(SkillEditActivity.KEY_SKILL, skill);
                startActivityForResult(intent, REQ_CODE_EDIT_SKILL);
            }
        });
    }

    private String formatSkill(List<String> skills){
        StringBuilder sb = new StringBuilder();
        for (String item: skills) {
            sb.append(item).append(", ");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void loadData() {
        BasicInfo savedBasicInfo = ModelUtils.read(this,
                                                   MODEL_BASIC_INFO,
                                                   new TypeToken<BasicInfo>(){});
        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = ModelUtils.read(this,
                                                         MODEL_EDUCATIONS,
                                                         new TypeToken<List<Education>>(){});
        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;

        List<Experience> savedExperience = ModelUtils.read(this,
                                                           MODEL_EXPERIENCES,
                                                           new TypeToken<List<Experience>>(){});
        experiences = savedExperience == null ? new ArrayList<Experience>() : savedExperience;

        List<Project> savedProjects = ModelUtils.read(this,
                                                     MODEL_PROJECTS,
                                                     new TypeToken<List<Project>>(){});
        projects = savedProjects == null ? new ArrayList<Project>() : savedProjects;

        List<Skill> savedSkills = ModelUtils.read(this,
                MODEL_SKILLS,
                new TypeToken<List<Skill>>(){});
        skills = savedSkills == null ? new ArrayList<Skill>() : savedSkills;
    }

    public static String formatItems(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item: items) {
            sb.append(' ').append('-').append(' ').append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void updateBasicInfo(BasicInfo basicInfo) {
        ModelUtils.save(this, MODEL_BASIC_INFO, basicInfo);

        this.basicInfo = basicInfo;
        setupBasicInfo();
    }

    private void updateEducation(Education education) {
        boolean found = false;
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, education.id)) {
                found = true;
                educations.set(i, education);
                break;
            }
        }

        if (!found && !isEduNull(education)) {
            educations.add(education);
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    private boolean isEduNull(Education edu){
        return edu.school.equals("") && edu.courses.size()==0 && edu.major.equals("");
    }

    private boolean isExperienceNull(Experience exp){
        return exp.company.equals("");
    }

    private void updateExperience(Experience experience) {
        boolean found = false;
        for (int i = 0; i < experiences.size(); ++i) {
            Experience e = experiences.get(i);
            if (e.id.equals(experience.id)) {
                found = true;
                experiences.set(i, experience);
                break;
            }
        }

        if (!found && !isExperienceNull(experience)) {
            experiences.add(experience);
        }

        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
    }

    private void updateProject(Project project) {
        boolean found = false;
        for (int i = 0; i < projects.size(); ++i) {
            Project p = projects.get(i);
            if (TextUtils.equals(p.id, project.id)) {
                found = true;
                projects.set(i, project);
                break;
            }
        }

        if (!found && !isProjectEmpty(project)) {
            projects.add(project);
        }

        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }


    private boolean isProjectEmpty(Project project){
        return project.name.equals("");
    }

    private void updateSkill(Skill skill){
        boolean found = false;
        for (int i=0; i<skills.size(); ++i){
            Skill s = skills.get(i);
            if(TextUtils.equals(s.id, skill.id)){
                found = true;
                skills.set(i,skill);
                break;
            }
        }

        if(!found && !isSkillEmpty(skill)){
            skills.add(skill);
        }
        ModelUtils.save(this,MODEL_SKILLS,skills);
        setupSkills();
    }

    private boolean isSkillEmpty(Skill skill){
        return skill.type.equals("");
    }

    private void deleteEducation(@NonNull String educationId) {
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, educationId)) {
                educations.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    private void deleteExperience(@NonNull String experienceId) {
        for (int i = 0; i < experiences.size(); ++i) {
            Experience e = experiences.get(i);
            if (TextUtils.equals(e.id, experienceId)) {
                experiences.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
        setupExperiences();
    }

    private void deleteProject(@NonNull String projectId) {
        for (int i = 0; i < projects.size(); ++i) {
            Project p = projects.get(i);
            if (TextUtils.equals(p.id, projectId)) {
                projects.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_PROJECTS, projects);
        setupProjects();
    }

    private void deleteSkill(String skillID){
        for (int i = 0; i < skills.size(); ++i) {
            Skill s = skills.get(i);
            if (TextUtils.equals(s.id, skillID)) {
                skills.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_SKILLS, skills);
        setupSkills();
    }
}
