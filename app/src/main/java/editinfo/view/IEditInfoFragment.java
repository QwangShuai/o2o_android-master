package editinfo.view;


import java.util.List;

import com.gjzg.bean.SkillsBean;

public interface IEditInfoFragment {

    void skillSuccess(String json);

    void skillFailure(String failure);

    void showAddSkillSuccess(List<SkillsBean> skillsBeanList);

    void showAddSkillFailure(String failure);

    void showSubmitSuccess(String success);

    void showSubmitFailure(String failure);
}
