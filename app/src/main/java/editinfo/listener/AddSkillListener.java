package editinfo.listener;


import java.util.List;

import com.gjzg.bean.SkillsBean;

public interface AddSkillListener {

    void success(List<SkillsBean> skillsBeanList);

    void failure(String failure);
}
